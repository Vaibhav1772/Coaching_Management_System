package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<CourseModel>courseModelArrayList;
    Context context;
    CardView cardView;
    RecyclerAdapter(Context context,ArrayList<CourseModel>courseModelArrayList){
        this.context=context;
        this.courseModelArrayList=courseModelArrayList;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.course_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(courseModelArrayList.get(position).name);
        System.out.println((courseModelArrayList.get(position).idx));
        holder.idx.setText((courseModelArrayList.get(position).idx));
        holder.des.setText(courseModelArrayList.get(position).des);
    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,idx,des;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.course_name);
            idx = itemView.findViewById(R.id.course_id);
            des = itemView.findViewById(R.id.course_des);
            cardView=itemView.findViewById(R.id.cardView);

        }

    }

}
