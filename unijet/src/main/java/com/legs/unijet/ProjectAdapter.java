package com.legs.unijet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.legs.unijet.createGroupActivity.CreateGroupStart;

import java.util.ArrayList;


public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
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




    @NonNull


    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_list_card, viewGroup, false);
        ProjectViewHolder cvh = new ProjectViewHolder(v);
        return cvh;
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



    @Override
    public int getItemCount() {
        return projectList.size();
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