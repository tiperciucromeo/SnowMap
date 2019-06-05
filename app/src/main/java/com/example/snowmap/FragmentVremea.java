package com.example.snowmap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


public class FragmentVremea extends Fragment {
    private static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "e3d615ef42f3ddb53d8f49e24373b97a";

    private String nume;

    private double longitudine;
    private double latitudine;

    TextView[] descriere = new TextView[3];
    TextView[] tempMin = new TextView[3];
    TextView[] tempMax = new TextView[3];
    TextView[] umiditate = new TextView[3];
    TextView[] vant = new TextView[3];

    TextView descriere1,descriere2,descriere3,tempmin1,tempmin2,tempmin3,tempmax1,tempmax2,tempmax3,
            umiditate1,umiditate2,umiditate3,vant1,vant2,vant3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null){
            nume = bundle.getString("NUME_PARTIE");

        }

        fetchLongLat();

        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_fragment_vremea, container, false);
        descriere[0] = v.findViewById(R.id.descJSON);
        descriere[1] = v.findViewById(R.id.descriereJSON2);
        descriere[2] = v.findViewById(R.id.descriereJSON3);
        tempMin[0] = v.findViewById(R.id.tempminJSON);
        tempMin[1] = v.findViewById(R.id.tempminJSON2);
        tempMin[2] = v.findViewById(R.id.tempminJSON3);
        tempMax[0] = v.findViewById(R.id.tempmaxJSON);
        tempMax[1] = v.findViewById(R.id.tempmaxJSON2);
        tempMax[2] = v.findViewById(R.id.tempmaxJSON3);
        umiditate[0] = v.findViewById(R.id.umiditateJSON);
        umiditate[1] = v.findViewById(R.id.umiditateJSON2);
        umiditate[2] = v.findViewById(R.id.umiditateJSON3);
        vant[0] =  v.findViewById(R.id.vantJSON);
        vant[1] =  v.findViewById(R.id.vantJSON2);
        vant[2] =  v.findViewById(R.id.vantJSON3);

         return v;
    }

    public void fetchLongLat(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference partieRef = database.getReference("Partii").child(nume);

        partieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                longitudine = (double) dataSnapshot.child("Long").getValue();
                latitudine = (double) dataSnapshot.child("Lat").getValue();

                Thread wheatherThread = new Thread(new WheatherThread());
                wheatherThread.start();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("nu nu nu", "Failed to read value.");
            }
        });
    }

    public void paintUI(final ArrayList<WeatherInfo> weather) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < weather.size(); ++i) {
                    descriere[i].setText(weather.get(i).getDescription());
                    tempMin[i].setText(Double.toString(weather.get(i).getMinTemperature()) + " \u00b0C");
                    tempMax[i].setText(Double.toString(weather.get(i).getMaxTemperature()) + " \u00b0C");
                    umiditate[i].setText(Double.toString(weather.get(i).getHumidity()) + " %");
                    vant[i].setText(Double.toString(weather.get(i).getSpeedWind()) + " m/s");
                }
            }
        });
    }

    public class WheatherThread implements Runnable {

        private ArrayList<WeatherInfo> weatherInfo = new ArrayList<>();

        @Override
        public void run() {
            fetchWeather();
        }

        public void fetchWeather(){
            try {
                URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, latitudine, longitudine));
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

                InputStream inStream;
                int status = connection.getResponseCode();
                if (status != HttpURLConnection.HTTP_OK) {
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
                JSONObject data = new JSONObject(content);

                // This value will be 404 if the request was not
                // successful
                if (data.getInt("cod") != 200) {
                    Log.e("Invalid return code", "Return code for weather API call: " + data.getInt("cod"));
                    return;
                }

                parseWeatherInfo(data);
            }catch (Exception e){
                Log.e("WHEA", e.getMessage());
            }
        }

        public void parseWeatherInfo(JSONObject data) {
            try {
                JSONArray list =  data.getJSONArray("list");

                JSONObject firstDayInfo = list.getJSONObject(0);
                JSONObject secondDayInfo = list.getJSONObject(8);
                JSONObject thirdDayInfo = list.getJSONObject(16);

                JSONObject mainFirstDay = firstDayInfo.getJSONObject("main");
                JSONObject mainSecondDay = secondDayInfo.getJSONObject("main");
                JSONObject mainThirdDay = thirdDayInfo.getJSONObject("main");

                WeatherInfo firstDay = new WeatherInfo(
                        mainFirstDay.getDouble("temp_min"),
                        mainFirstDay.getDouble("temp_max"),
                        mainFirstDay.getDouble("humidity"),
                        firstDayInfo.getJSONObject("wind").getDouble("speed"),
                        firstDayInfo.getJSONArray("weather").getJSONObject(0).getString("description")
                );

                WeatherInfo secondDay = new WeatherInfo(
                        mainSecondDay.getDouble("temp_min"),
                        mainSecondDay.getDouble("temp_max"),
                        mainSecondDay.getDouble("humidity"),
                        secondDayInfo.getJSONObject("wind").getDouble("speed"),
                        secondDayInfo.getJSONArray("weather").getJSONObject(0).getString("description")
                );

                WeatherInfo thirdDay = new WeatherInfo(
                        mainThirdDay.getDouble("temp_min"),
                        mainThirdDay.getDouble("temp_max"),
                        mainThirdDay.getDouble("humidity"),
                        thirdDayInfo.getJSONObject("wind").getDouble("speed"),
                        thirdDayInfo.getJSONArray("weather").getJSONObject(0).getString("description")
                );

                weatherInfo.add(firstDay);
                weatherInfo.add(secondDay);
                weatherInfo.add(thirdDay);

                FragmentVremea.this.paintUI(weatherInfo);
            } catch (Exception e) {
                Log.e("WHEA", e.getMessage());
            }
        }
    }
}
