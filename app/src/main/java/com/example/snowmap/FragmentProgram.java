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


public class FragmentProgram extends Fragment {

    String nume;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null){
            nume = bundle.getString("NUME_PARTIE");

        }

        View view =  inflater.inflate(R.layout.fragment_fragment_program, container, false);

        final TextView luV = view.findViewById(R.id.lunivineri);
         final TextView dum = view.findViewById(R.id.duminica);
         final TextView samb = view.findViewById(R.id.sambata);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference partieRef = database.getReference("Partii").child(nume);

        partieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String luniVineri = (String) dataSnapshot.child("Luni-Vineri").getValue();
               String duminica = (String) dataSnapshot.child("Duminica").getValue();
                String sambata = (String) dataSnapshot.child("Sambata").getValue();

                   luV.setText(luniVineri);
                 dum.setText(duminica);

                    samb.setText(sambata);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("nu nu nu", "Failed to read value.");
            }
        });
        // Inflate the layout for this fragment*/
        return view;
    }

}
