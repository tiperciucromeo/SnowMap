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


public class FragmentPreturi extends Fragment {
    String nume;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null){
            nume = bundle.getString("NUME_PARTIE");

        }

        View view =  inflater.inflate(R.layout.fragment_fragment_preturi, container, false);

        final TextView unu = view.findViewById(R.id.ourcare);
        final TextView jumate = view.findViewById(R.id.jumatezi);
        final TextView una = view.findViewById(R.id.ozi);
        final TextView trei = view.findViewById(R.id.treizile);
        final TextView sapte = view.findViewById(R.id.saptezile);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference partieRef = database.getReference("Partii").child(nume);

        partieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ourcare = (String) dataSnapshot.child("Preturi").child("ourcare").getValue();
                String jumatezi = (String) dataSnapshot.child("Preturi").child("jumate").getValue();
               String unazi = (String) dataSnapshot.child("Preturi").child("ozi").getValue();
               String treiz = (String) dataSnapshot.child("Preturi").child("treizile").getValue();
               String saptez = (String) dataSnapshot.child("Preturi").child("saptezile").getValue();

                 unu.setText(ourcare);
                 jumate.setText(jumatezi);
                 una.setText(unazi);
                 trei.setText(treiz);
                 sapte.setText(saptez);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("nu nu nu", "Failed to read value.");
            }
        });
        // Inflate the layout for this fragment*/
        return view;
    } }

