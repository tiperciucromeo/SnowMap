package com.example.snowmap;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapterLista extends RecyclerView.Adapter<RecyclerViewAdapterLista.ViewHolder> {

    private ArrayList<String> mDenumire;
    private ArrayList<String> mDistanta;
    private Context mContext;

    public RecyclerViewAdapterLista( Context mContext,ArrayList<String> mDenumire, ArrayList<String> mDistanta) {
        this.mDenumire = mDenumire;
        this.mDistanta = mDistanta;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,
                parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
// metoda asta e la fel pentru toate recyclerViews , nu trebuie sa inteleg mare lucru din ea
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.denumire.setText(mDenumire.get(position));

        holder.distanta.setText(mDistanta.get(position));


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetaliiPartie.class);
                intent.putExtra("NUME_PARTIE", mDenumire.get(position));
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
       return mDenumire.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //acest view holder imi tine in memorie fiecare widget
        TextView denumire;
        TextView distanta;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            denumire = itemView.findViewById(R.id.denumire);
            distanta = itemView.findViewById(R.id.distanta);
            relativeLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
