package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Batches;
import com.example.myapplication.R;

import java.util.List;

public class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.BatchHolder> {

    private final List<Batches> batchesList;
    private final Context context;
    private final OnItemClickListener listener;
    public BatchAdapter(Context context, List<Batches> list, OnItemClickListener listener) {
        this.context=context;
        this.batchesList=list;
        this.listener=listener;
    }
    @NonNull
    @Override
    public BatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BatchHolder(LayoutInflater.from(context).inflate(R.layout.batch_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BatchHolder holder, int position) {

        Batches batches=batchesList.get(position);
        holder.bind(batches,listener);

    }

    public interface OnItemClickListener {
        void onItemClick(Batches batches);
    }

    @Override
    public int getItemCount() {
        return batchesList.size();
    }
    public static class BatchHolder extends RecyclerView.ViewHolder{

        TextView id,name;
        public BatchHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.batch_id);
            name=itemView.findViewById(R.id.batch_name);
        }
        public void bind(Batches batches,OnItemClickListener listener){
            id.setText(String.format("%s.",batches.getId()));
            name.setText(batches.getClassroom());
           itemView.setOnClickListener(v->{
               if(listener!=null) {
                   listener.onItemClick(batches);
               }
           });
        }
    }
}
