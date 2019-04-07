package com.example.snowmap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HorizontalRecyclerViewDialog extends RecyclerView.Adapter<HorizontalRecyclerViewDialog.ViewHolder>  {


    private ArrayList<String> listapozeDialog;
    private Context mContext;

    public HorizontalRecyclerViewDialog( Context mContext,ArrayList<String> listapoze) {
        listapozeDialog = listapoze;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.w("DEBUGING", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dialog,
                parent,false);
        return new HorizontalRecyclerViewDialog.ViewHolder(view);
// metoda asta e la fel pentru toate recyclerViews , nu trebuie sa inteleg mare lucru din ea
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).asBitmap().load(listapozeDialog.get(position)).into(holder.pozaDialog);


    }

    @Override
    public int getItemCount() {
        return listapozeDialog.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //acest view holder imi tine in memorie fiecare widget

        ImageView pozaDialog;
        LinearLayout parent_layoutpozaDialog; //asta il folosesc doar daca vreau sa dau click pe dialog si sa am un eveniment pe el


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pozaDialog = itemView.findViewById(R.id.pozaDialog);
            parent_layoutpozaDialog = itemView.findViewById(R.id.parent_layoutpozaDialog);

        }
    }
}
