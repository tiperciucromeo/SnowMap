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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ListaPartii  extends MainActivity {

    private ArrayList<String> denumire = new ArrayList<>();
    private ArrayList<String> distanta = new ArrayList<>();


    Button butonInapoi;
    ImageButton ziar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partii);

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
                }

                initRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DEBUGING", "Failed to read value.", databaseError.toException());
            }
        });

    }

    public void inapoiLaHarta() { // aceasta metoda ma duce inapoi la Pagina principala cu partia
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Log.w("plm", "Failed to read value.");
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecyclerViewAdapterLista adapter = new RecyclerViewAdapterLista(this,denumire,distanta);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.w("DEBUGING", "Apeleaza metoda initRecycler");
    }


}
