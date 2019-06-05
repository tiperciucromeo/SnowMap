package com.example.snowmap;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class SnowMap extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
