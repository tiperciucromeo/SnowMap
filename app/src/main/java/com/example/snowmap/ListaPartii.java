package com.example.snowmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ListaPartii  extends MainActivity {

    private ArrayList<String> denumire = new ArrayList<>();
    private ArrayList<String> distantaFinal = new ArrayList<>();

    private Map<String, String> distanta = new TreeMap<>();

    private double latCurenta;
    private double lonCurenta;

    Button butonInapoi;
    ImageButton ziar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partii);

        Intent intent = getIntent();

        latCurenta = intent.getDoubleExtra("latCurenta",0.0);
        lonCurenta = intent.getDoubleExtra("lonCurenta",0.0);


        initLayout();
        ziar =  findViewById(R.id.newspaper);
        butonInapoi = findViewById(R.id.butonInapoi);

        ziar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openNewsPaper();
            }
        });



        butonInapoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inapoiLaHarta();

            }
        }); //la click , ma va duce inapoi la harta



    }

    public void openNewsPaper() { // deschide webview-ul cu partii
        Intent intent = new Intent(this, NewsPaper.class);
        startActivity(intent);
        Log.w("DEBUGING", "open news papaer");

    }



    private void initLayout () {
        Log.w("DEBUGING", "Intra in metoda initImage.");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference partiiRef = database.getReference().child("Partii");
        partiiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.w("DEBUGING", "Intra in metoda onDataChage.");
                denumire.clear();
                Map<String, Object> mapPartii = (Map<String, Object>) dataSnapshot.getValue();

                //incerc sa ordonez
                Map<String,Object> treeMap = new TreeMap<>(mapPartii);

                for (Map.Entry<String, Object> entry : treeMap.entrySet()){
                    denumire.add(entry.getKey());

                    Map<String, Object> infoDenumire = (Map<String, Object>)entry.getValue();
                    double lat = (double)infoDenumire.get("Lat");
                    double lon = (double)infoDenumire.get("Long");

                    computeDistanceTo(entry.getKey(), lat, lon);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DEBUGING", "Failed to read value.", databaseError.toException());
            }
        });

    }

    public void computeDistanceTo(String denumire, double lat, double lon) {
        Thread distanceThread = new Thread(new DistanceThread(denumire, lat, lon));
        distanceThread.start();
    }

    public void inapoiLaHarta() { // aceasta metoda ma duce inapoi la Pagina principala cu partia
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Log.w("plm", "Failed to read value.");
    }

    private void initRecyclerView(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                distantaFinal.clear();
                for (String nume : denumire) {
                    String distanceAsText = distanta.get(nume);
                    distantaFinal.add(distanceAsText);
                }
                RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
                RecyclerViewAdapterLista adapter = new RecyclerViewAdapterLista(ListaPartii.this,denumire,distantaFinal);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ListaPartii.this));
                Log.w("DEBUGING", "Apeleaza metoda initRecycler");
            }
        });
    }

    public class DistanceThread implements Runnable {
        private double lat;
        private double lon;
        private String denumire;

        private static final String API_KEY = "AIzaSyA9GwNpUa_EPN5zkpNl0lEvsTT6hY19W0I";
        private static final String REQ_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&key=%s";

        public DistanceThread(String denumire, double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
            this.denumire = denumire;
        }

        @Override
        public void run() {
            try {
                boolean error = false;
                String formattedURL = String.format(REQ_URL,
                        ListaPartii.this.latCurenta,
                        ListaPartii.this.lonCurenta,
                        lat,
                        lon,
                        API_KEY);
                URL url = new URL(formattedURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream inStream;
                int status = connection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
                    error = true;
                    inStream = connection.getErrorStream();
                } else {
                    inStream = connection.getInputStream();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null) {
                    json.append(tmp).append("\n");
                }
                reader.close();

                String content = json.toString();
                if (error) {
                    Log.e("DIST", content);
                    return;
                }

                JSONObject data = new JSONObject(content);
                parseDistance(data);
            }catch (Exception e){
                Log.e("DIST", e.getMessage());
            }
        }

        public void parseDistance(JSONObject obj) {
            try {
                JSONArray routes = obj.getJSONArray("routes");
                JSONObject bestRoute = routes.getJSONObject(0);
                JSONArray legs = bestRoute.getJSONArray("legs");
                JSONObject legsInfo  = legs.getJSONObject(0);
                JSONObject distance = legsInfo.getJSONObject("distance");
                String distanceAsText = distance.getString("text");
                ListaPartii.this.distanta.put(denumire, distanceAsText);

                if (ListaPartii.this.distanta.size() == ListaPartii.this.denumire.size()) {
                    ListaPartii.this.initRecyclerView();
                }
            } catch (Exception e) {
                Log.e("DIST", e.getMessage());
            }
        }
    }


}
