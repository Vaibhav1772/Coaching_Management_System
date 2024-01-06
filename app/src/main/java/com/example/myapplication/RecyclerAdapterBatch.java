package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterBatch extends RecyclerView.Adapter<RecyclerAdapterBatch.ViewHolder> {

    ArrayList<BatchModel>courseModelArrayList;
    Context context;
    RecyclerAdapterBatch(Context context,ArrayList<BatchModel>courseModelArrayList){
        this.context=context;
        this.courseModelArrayList=courseModelArrayList;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.frag_batches,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(courseModelArrayList.get(position).name);
        holder.idx.setText((courseModelArrayList.get(position).idx));
        holder.sdate.setText(courseModelArrayList.get(position).sdate);
        holder.edate.setText(courseModelArrayList.get(position).edate);
        holder.schdl.setText(courseModelArrayList.get(position).days);
        holder.teach.setText(courseModelArrayList.get(position).teachers);
    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,idx,sdate,edate,schdl,teach;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.batch_name);
            idx = itemView.findViewById(R.id.batch_id);
            sdate = itemView.findViewById(R.id.start_date);
            edate = itemView.findViewById(R.id.end_date);
            schdl = itemView.findViewById(R.id.schedule);
            teach = itemView.findViewById(R.id.teachers);
        }

    }

}
