package com.example.snowmap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DetaliiPartie extends MainActivity {


    private ArrayList<File> Listapoze = new ArrayList<>();




    private String numePartie;
    Fragment fragment;
    File localFile;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalii_partie);


        Button butonInapoi = findViewById(R.id.butonInapoi);
        TextView partiaCutare = findViewById(R.id.partiaCutare);
        final Button hartaPartiei = findViewById(R.id.hartaPartiei);

        hartaPartiei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hartaPartie();
            }
        });

        butonInapoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inapoiLaHarta();
            }
        }); //la click , ma va duce inapoi la lista de partii

        numePartie = getIntent().getStringExtra("NUME_PARTIE"); //iau numele partiei  din lista ca sa personalizez detaliile
        Log.w("Extra12345", numePartie); //verificare daca imi ia bine
        partiaCutare.setText(numePartie);

        getImages();

    }

    public void inapoiLaHarta() { // aceasta metoda ma duce inapoi la Pagina principala cu partia
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void hartaPartie(){
        Intent intent = new Intent(this,HartaPartiei.class);
        intent.putExtra("NUME_PARTIE", numePartie);
        startActivity(intent);
    }

    //ASTEA O SA MI LE IAU DIN BAZA DE DATE
    private void getImages(){
        //trebuie sa imi ia din baza de date poze si sa mi le adauge in Listapoze
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference pozeRef = database.getReference("Partii").child(this.numePartie).child("Poze");

        //sterge asta daca nu merge persistenta
      //  pozeRef.keepSynced(true);

        pozeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> pozeList = (List<String>)dataSnapshot.getValue();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                for (String nume : pozeList) {
                    StorageReference pozaRef = storage.getReference().child(nume);
                    try {
                        String fileName = nume.substring(nume.indexOf('/') + 1);
                        localFile = File.createTempFile(fileName, "");
                        Listapoze.add(localFile);
                    } catch (Exception e) {
                        Log.d("nu,nu", e.getMessage());
                    }
                    pozaRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            initRecyclerView();
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("DEBUG", "failed to download img from firebase storage");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("nu nu nu", "Failed to read value.");
            }
        });

    }

    private void initRecyclerView(){
        Log.w("DEBUGING", "initRecyclerView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPoze);
        recyclerView.setLayoutManager(layoutManager);
        HorizontalRecyclerViewPoze adapter = new HorizontalRecyclerViewPoze(this,Listapoze);
        recyclerView.setAdapter(adapter);
    }


    public void trimitereDateFragment (){
        Log.w("NUME", numePartie);
        Bundle bundle = new Bundle();
        bundle.putString("NUME_PARTIE",numePartie);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_place,fragment,null);
        ft.commitNow();
    }

    public void ChangeFragment(View view) {

        if (view == findViewById(R.id.butonDetalii)) {
            fragment = new FragmentDetalii();
            trimitereDateFragment();
        }
        if (view == findViewById(R.id.butonVremea)) {
            fragment = new FragmentVremea();
            trimitereDateFragment();

        }
        if (view == findViewById(R.id.butonPreturi)) {
            fragment = new FragmentPreturi();
            trimitereDateFragment();

        }
        if (view == findViewById(R.id.butonProgram)) {
            fragment = new FragmentProgram();
            trimitereDateFragment();

        }
    }


}
