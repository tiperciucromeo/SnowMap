package com.example.snowmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context mContext) {
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window,null);
        this.mContext = mContext;

    }

    private void renderWindowText(Marker marker,View view){
        String title = marker.getTitle();

        TextView numePartie = view.findViewById(R.id.numePartieMarker);
        if(!title.equals("")){
            numePartie.setText(title);
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }
}
