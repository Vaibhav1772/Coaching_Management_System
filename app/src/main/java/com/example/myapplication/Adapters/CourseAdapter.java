package com.example.myapplication.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Courses;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    List<Courses> courses;
    OnButtonClickListener listener;
    Context context;
    public CourseAdapter(Context context, List<Courses> courses,OnButtonClickListener listener) {
        this.context = context;
        this.courses= courses;
        this.listener=listener;
    }
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.course_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Courses course = courses.get(position);
        holder.bind(course, listener);
    }

    public interface OnButtonClickListener {
        void onButtonClick(Courses course);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        FloatingActionButton addCourse;
        TextView courseName,description,id;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            addCourse=itemView.findViewById(R.id.add_course);
            courseName=itemView.findViewById(R.id.course_name);
            description=itemView.findViewById(R.id.course_des);
            id=itemView.findViewById(R.id.course_id);
        }
        public void bind(final Courses course, final OnButtonClickListener listener) {
            courseName.setText(course.getCourseName());
            description.setText(course.getDescription());
            id.setText(String.format("%s. ",course.getId().toString()));

            addCourse.setOnClickListener(v->{
                if(listener!=null)
                    listener.onButtonClick(course);
            });
        }
    }

}
