package com.legs.unijet.createGroupActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.legs.unijet.R;

import java.util.ArrayList;

public class GroupAdapter  extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private ArrayList<UserSample2> groupsList;


        public static class GroupViewHolder extends RecyclerView.ViewHolder {

            public TextView mGroups;


            public GroupViewHolder(View itemView) {
                super(itemView);
                mGroups = itemView.findViewById(R.id.group_name);

            }
        }
        public GroupAdapter(ArrayList<UserSample2> groupsList){
            this.groupsList = groupsList;
        }




        @NonNull


        @Override
        public GroupAdapter.GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.groups_sample, viewGroup, false);
            GroupAdapter.GroupViewHolder cvh = new GroupAdapter.GroupViewHolder (v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder groupViewHolder, int i) {
            groupViewHolder.mGroups.setText(groupsList.get(i).getText1());
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



