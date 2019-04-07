package com.example.snowmap;

import android.annotation.SuppressLint;
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

public class FragmentDetalii extends Fragment {

    String nume = "romeo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null){
           nume = bundle.getString("NUME_PARTIE");

        }
        Log.d("nu", nume);

        View view =  inflater.inflate(R.layout.fragment_fragment_detalii, container, false);

        final TextView detalii = view.findViewById(R.id.detalii);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference partieRef = database.getReference("Partii").child(nume);


        partieRef.addValueEventListener(new ValueEventListener() {
            @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String details = (String) dataSnapshot.child("Detalii").getValue();
                    detalii.setText(details);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("nu nu nu", "Failed to read value.");
                }
            });


      return view;
    }
}
