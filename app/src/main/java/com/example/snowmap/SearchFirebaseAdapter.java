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

public class SearchFirebaseAdapter extends RecyclerView.Adapter<SearchFirebaseAdapter.SearchViewHolder> {
    Context context;


    private ArrayList<String> fullNamePartii;

    public SearchFirebaseAdapter(Context context, ArrayList<String> fullNamePartii) {
        this.context = context;
        this.fullNamePartii = fullNamePartii;
    }

    @NonNull
    @Override
    public SearchFirebaseAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layoutsearch,viewGroup,false);
        return new SearchFirebaseAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, final int i) {
        searchViewHolder.partii_name.setText(fullNamePartii.get(i));

        searchViewHolder.layoutsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetaliiPartie.class);
                intent.putExtra("NUME_PARTIE", fullNamePartii.get(i));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return fullNamePartii.size();
    }



    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView partii_name;
        RelativeLayout layoutsearch;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            partii_name = itemView.findViewById(R.id.name_text);
            layoutsearch = itemView.findViewById(R.id.list_layoutsearch);
        }
        }
    }


