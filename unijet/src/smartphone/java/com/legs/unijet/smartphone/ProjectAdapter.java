package com.legs.unijet.smartphone;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.smartphone.createGroupActivity.CreateGroupStart;

import java.util.ArrayList;
import java.util.List;


public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> implements Filterable {
    private ArrayList<ProjectSample> projectList;

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

    ProjectAdapter( ArrayList<ProjectSample> projectList){
        this.projectList = projectList;
    }


    public Filter getFilter() {
        return mFilter;
    }

    private Filter mFilter = new Filter() {
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
        ProjectViewHolder cvh = new ProjectViewHolder(v);
        return cvh;
    }

    @Override
    public int getItemCount() {
        return projectList.size();
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

    public String returnGroup (int position) {
        return projectList.get(position).getText2();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}