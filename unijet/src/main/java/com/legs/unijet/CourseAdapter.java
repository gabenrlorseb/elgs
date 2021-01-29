package com.legs.unijet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private ArrayList<CourseSample> coursesList;


    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        public TextView mCourses;
        public TextView mProfessors;


        public CourseViewHolder(View itemView) {
            super(itemView);
            mCourses = itemView.findViewById(R.id.course_name);
            mProfessors = itemView.findViewById(R.id.course_professor);

        }
    }

    CourseAdapter( ArrayList<CourseSample> coursesList){
        this.coursesList = coursesList;
    }




    @NonNull


    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.courses_sample, viewGroup, false);
        CourseViewHolder cvh = new CourseViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder courseViewHolder, int i) {
        courseViewHolder.mCourses.setText(coursesList.get(i).getText1());
        courseViewHolder.mProfessors.setText(coursesList.get(i).getText2());
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    }

