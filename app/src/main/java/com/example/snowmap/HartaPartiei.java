package com.example.snowmap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HartaPartiei extends MainActivity {
    private String numePartie;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.harta_partiei);

        Button butonInapoi = findViewById(R.id.butonInapoi);
        TextView partiaCutare = findViewById(R.id.partiaCutare);
       butonInapoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               inapoiLaHarta();
            }
        });

       numePartie = getIntent().getStringExtra("NUME_PARTIE"); //iau numele partiei  din lista ca sa personalizez detaliile
       Log.w("Extra12345", numePartie); //verificare daca imi ia bine
       partiaCutare.setText(numePartie);

    }
    public void inapoiLaHarta() {
        Intent intent = new Intent(this,DetaliiPartie.class);
        intent.putExtra("NUME_PARTIE", numePartie);
        startActivity(intent);
    }
}
