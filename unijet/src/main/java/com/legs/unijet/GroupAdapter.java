package com.legs.unijet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {


     private ArrayList<CourseSample> groupsList;


        public static class GroupViewHolder extends RecyclerView.ViewHolder {

            public TextView mGroups;
            public TextView mOwners;


            public GroupViewHolder(View itemView) {
                super(itemView);
                mGroups = itemView.findViewById(R.id.course_name);
                mOwners = itemView.findViewById(R.id.course_professor);

            }
        }

        public GroupAdapter(ArrayList<CourseSample> groupsList){
            this.groupsList = groupsList;
        }




        @NonNull


        @Override
        public com.legs.unijet.GroupAdapter.GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.courses_sample, viewGroup, false);
            com.legs.unijet.GroupAdapter.GroupViewHolder cvh = new com.legs.unijet.GroupAdapter.GroupViewHolder (v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(@NonNull com.legs.unijet.GroupAdapter.GroupViewHolder groupViewHolder, int i) {
            groupViewHolder.mGroups.setText(groupsList.get(i).getText1());
            groupViewHolder.mOwners.setText(groupsList.get(i).getText2());
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



