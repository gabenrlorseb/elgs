package com.legs.unijet.createGroupActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.legs.unijet.CourseAdapter;
import com.legs.unijet.CourseSample;
import com.legs.unijet.R;

import java.util.ArrayList;

public class GroupAdapter  extends RecyclerView.Adapter<com.legs.unijet.CourseAdapter.CourseViewHolder> {


     private ArrayList<UserSample2> groupsList;


        public static class CourseViewHolder extends RecyclerView.ViewHolder {

            public TextView mGroups;


            public CourseViewHolder(View itemView) {
                super(itemView);
                mGroups = itemView.findViewById(R.id.group_name);

            }
        }

        public GroupAdapter(ArrayList<UserSample2> groupsList){
            this.groupsList = groupsList;
        }




        @NonNull


        @Override
        public com.legs.unijet.CourseAdapter.CourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.courses_sample, viewGroup, false);
            com.legs.unijet.CourseAdapter.CourseViewHolder cvh = new com.legs.unijet.CourseAdapter.CourseViewHolder (v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(@NonNull com.legs.unijet.CourseAdapter.CourseViewHolder courseViewHolder, int i) {
            courseViewHolder.mCourses.setText(groupsList.get(i).getText1());
        }

        @Override
        public int getItemCount() {
            return groupsList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }



