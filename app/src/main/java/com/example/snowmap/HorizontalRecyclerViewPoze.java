package com.example.snowmap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;


import com.bumptech.glide.Glide;
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
import java.util.List;

public class HorizontalRecyclerViewPoze extends RecyclerView.Adapter<HorizontalRecyclerViewPoze.ViewHolder>  {

    private ArrayList<File> Listapoze;
    private Context mContext;

    
    private ArrayList<File>  listapozeDialog = new ArrayList<>();
    File localFile;
    private String numePartie; //ca sa stiu de unde din baza de date sunt 
    

    //private ArrayList<String> listapozeDialog = new ArrayList<>();

    public HorizontalRecyclerViewPoze(Context mContext,ArrayList<File> poze) { //am schimbat aici din arraylist de string in file
        this.Listapoze = poze;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.w("DEBUGING", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_poze_item,
                parent,false);
        return new ViewHolder(view);
// metoda asta e la fel pentru toate recyclerViews , nu trebuie sa inteleg mare lucru din ea
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.w("DEBUGING", "onBindView");
        //trebuie sa iterez aici lista de obiecte ca sa adaug

        Glide.with(mContext).asBitmap().load(Listapoze.get(position)).fitCenter().into(holder.poza);

        holder.poza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = (ImageView)v;

                Dialog myDialog = new Dialog(mContext);
                myDialog.setContentView(R.layout.custom_dialog);

                ImageView pozaDialog = (ImageView)myDialog.findViewById(R.id.pozaDialog);
                pozaDialog.setImageDrawable(image.getDrawable());

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Listapoze.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView poza;
        LinearLayout parent_layoutpoza;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poza = itemView.findViewById(R.id.poza);
            parent_layoutpoza = itemView.findViewById(R.id.parent_layoutpoza);

        }
    }


}

