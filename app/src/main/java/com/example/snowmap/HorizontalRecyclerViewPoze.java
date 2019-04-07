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

import java.util.ArrayList;

public class HorizontalRecyclerViewPoze extends RecyclerView.Adapter<HorizontalRecyclerViewPoze.ViewHolder>  {

    private ArrayList<String> Listapoze;
    private Context mContext;

    private ArrayList<String> listapozeDialog = new ArrayList<>();

    public HorizontalRecyclerViewPoze(Context mContext,ArrayList<String> poze) {
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
        Glide.with(mContext).asBitmap().load(Listapoze.get(position)).into(holder.poza);


       //cand dau click pe layout sa imi deschida un dialog
        holder.parent_layoutpoza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        //DE AICI INCEPE SMEcheria
        //trebuie sa apelez aici getImages()

        Dialog myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.custom_dialog);

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

        //acest view holder imi tine in memorie fiecare widget

        ImageView poza;
        LinearLayout parent_layoutpoza;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poza = itemView.findViewById(R.id.poza);
            parent_layoutpoza = itemView.findViewById(R.id.parent_layoutpoza);

        }
    }




    //ASTEA O SA MI LE IAU DIN BAZA DE DATE
    private void getImages(){
        Log.w("DEBUGING", "getImages");

        listapozeDialog.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");

        listapozeDialog.add("https://i.redd.it/tpsnoz5bzo501.jpg");

        listapozeDialog.add("https://i.redd.it/qn7f9oqu7o501.jpg");

        listapozeDialog.add("https://i.redd.it/j6myfqglup501.jpg");

        listapozeDialog.add("https://i.redd.it/0h2gm1ix6p501.jpg");

        listapozeDialog.add("https://i.redd.it/k98uzl68eh501.jpg");

        listapozeDialog.add("https://i.redd.it/glin0nwndo501.jpg");

        listapozeDialog.add("https://i.redd.it/obx4zydshg601.jpg");

        listapozeDialog.add("https://i.imgur.com/ZcLLrkY.jpg");

    }


}

