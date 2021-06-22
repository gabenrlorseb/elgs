package com.legs.unijet.smartphone.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.legs.unijet.smartphone.R;

import java.util.ArrayList;
import java.util.List;


public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> implements Filterable {
    private final ArrayList<ProjectSample> projectList;

    Bundle bundle;
    Intent intent;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Context context;


    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameProjects;
        public TextView mTitle;





        public ProjectViewHolder(View itemView) {
            super(itemView);
            mNameProjects = itemView.findViewById(R.id.project_name);
            mTitle = itemView.findViewById(R.id.project_subtitle);
        }
    }

    public ProjectAdapter(ArrayList<ProjectSample> projectList){
        this.projectList = projectList;
    }


    public Filter getFilter() {
        return mFilter;
    }

    private final Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProjectSample> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.clear();
                filteredList.addAll(projectList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ProjectSample item : projectList) {
                    if (item.getText1().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            projectList.clear();
            projectList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    @NonNull


    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_list_card, viewGroup, false);
        return new ProjectViewHolder(v);
    }

    @Override
    public int getItemCount() {
        if (projectList==null) {
            return 0;
        }
        else {
            return projectList.size ();
        }

    }



    @Override
    public void onBindViewHolder(@NonNull final ProjectViewHolder projectViewHolder, int i) {

        projectViewHolder.mNameProjects.setText(projectList.get(i).getText1());

        projectViewHolder.mTitle.setText(projectList.get(i).getText2());
        projectViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent i = new Intent (v.getContext(), ProjectDetailsActivity.class);
                        i.putExtra("PName", projectViewHolder.mNameProjects.getText());
                        i.putExtra("group", projectViewHolder.mTitle.getText());
                        v.getContext().startActivity(i);

            }
        });
    }






    public String returnTitle (int position) {
        return projectList.get(position).getText1();
    }
    public ArrayList<String>  returnMailA (int position) {
        return projectList.get(position).getText5();
    }

    public ArrayList<String> returnReci (int position) {
        return projectList.get(position).getText3();
    }
    public  ArrayList<String>  returnNameOwner (int position) {
        return projectList.get(position).getText4();
    }
    public String returnGroup (int position) {
        return projectList.get(position).getText2();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}