package com.example.tpkkoja.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpkkoja.Content.UploadPatrolRisk;
import com.example.tpkkoja.Content.UploadPatrolSafe;
import com.example.tpkkoja.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecycerviewAdapter";

    private ArrayList<String> names = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<String> names) {
        this.names = names;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG,"onCreateViewHodler: called ");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.nama.setText(names.get(i));
        viewHolder.nama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (i){
                    case 0:
                        context.startActivity(new Intent(context,UploadPatrolSafe.class));
                        break;
                    case 1:
                        context.startActivity(new Intent(context, UploadPatrolRisk.class));
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.list_nama);

        }
    }
}
