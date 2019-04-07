package com.example.snowmap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentVremea extends Fragment {
    private double longitudine;
    private double latitudine;
    private static final String OPEN_WEATHER_MAP_URL = "api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}";
    private static final String OPEN_WEATHER_MAP_API = "50eafd6972eeb0f707d2f29b6abbfabd";

    TextView selectCity, cityField, detailsField, currentTemperatureField, humidity_field,
            pressure_field, weatherIcon, updatedField;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_fragment_vremea, container, false);
        cityField = v.findViewById(R.id.city);
        detailsField = v.findViewById(R.id.description);
        currentTemperatureField = v.findViewById(R.id.temperature);
        humidity_field = v.findViewById(R.id.umiditate);
        pressure_field = v.findViewById(R.id.presiune);
        updatedField = v.findViewById(R.id.update);

        selectCity.setText("MERGEEEE!!!");

         return v;



    }

}
