package com.legs.unijet;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.legs.unijet.createGroupActivity.UserSample;
import com.legs.unijet.groupDetailsActivity.GroupActivity;

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
    public void onBindViewHolder(@NonNull final com.legs.unijet.GroupAdapter.GroupViewHolder groupViewHolder, int i) {
        final CourseSample currentItem = groupsList.get(i);
        groupViewHolder.mGroups.setText(groupsList.get(i).getText1());
        groupViewHolder.mOwners.setText(groupsList.get(i).getText2());

        groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), GroupActivity.class);
                i.putExtra("GName", groupViewHolder.mGroups.getText());
                i.putExtra("owner", groupViewHolder.mOwners.getText());
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public String returnTitle (int position) {
        return groupsList.get(position).getText1();
    }

    public String returnOwner (int position) {
        return groupsList.get(position).getText2();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}



