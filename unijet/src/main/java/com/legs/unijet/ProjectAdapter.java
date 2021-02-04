package com.legs.unijet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<com.legs.unijet. ProjectAdapter. ProjectViewHolder> {

        private ArrayList<ProjectSample> projectList;


        public static class  ProjectViewHolder extends RecyclerView.ViewHolder {

            public TextView mNameProjects;
            public TextView mTitle;


            public  ProjectViewHolder(View itemView) {
                super(itemView);
                mNameProjects = itemView.findViewById(R.id.project_name);
                mTitle = itemView.findViewById(R.id.project_subtitle);

            }
        }

    ProjectAdapter( ArrayList<ProjectSample> projectList){
            this.projectList = projectList;
        }




        @NonNull
        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_list_card, viewGroup, false);
            ProjectViewHolder cvh = new ProjectViewHolder (v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int i) {
            projectViewHolder.mNameProjects.setText(projectList.get(i).getText1());
            projectViewHolder.mTitle.setText(projectList.get(i).getText2());
        }

        @Override
        public int getItemCount() { return projectList.size() ; }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }




