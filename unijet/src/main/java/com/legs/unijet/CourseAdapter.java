package com.legs.unijet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.legs.unijet.createGroupActivity.CreateGroupStart;

import java.util.ArrayList;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private ArrayList<CourseSample> coursesList;
private Context context;


    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView mCourses;
        public TextView mProfessors;
        public LinearLayout mLayout;




        public CourseViewHolder(View itemView) {
            super(itemView);
            mCourses = itemView.findViewById(R.id.course_name);
            mProfessors = itemView.findViewById(R.id.course_professor);
            mLayout = itemView.findViewById(R.id.course_layout);
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
            courseViewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(v.getContext(), CourseDetailsActivity.class));
                }
            });
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