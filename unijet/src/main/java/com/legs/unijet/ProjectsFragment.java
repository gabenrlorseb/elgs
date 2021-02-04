package com.legs.unijet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.createGroupActivity.GroupAdapter;
import com.legs.unijet.createGroupActivity.UserSample2;

import java.util.ArrayList;

public class ProjectsFragment extends Fragment {


    FirebaseUser project;
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private ArrayList<ProjectSample> projectList;
    DatabaseReference db = FirebaseDatabase.getInstance ().getReference ();
    RecyclerView mRecyclerView;
    private ProjectAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate (R.layout.projects_page, container, false);
        populateList ();

        return view;

    }

    private void populateList() {
        projectList = new ArrayList<ProjectSample> ();
        db.child ("projects").addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren ()) {
                    for (DataSnapshot childSnapshot2 : childSnapshot.getChildren ()) {
                        for (DataSnapshot childSnapshot3 : childSnapshot2.getChildren ()) {

                            for (DataSnapshot childSnapshot4 : childSnapshot3.getChildren ()) {

                                String name = childSnapshot4.child ("name").getValue (String.class);
                                String group = childSnapshot4.child ("group").getValue (String.class);
                                Log.d ("", "onDataChange: " + group);
                                projectList.add (new ProjectSample (name, group));


                            }
                        }
                    }
                }

                buildRecyclerView ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buildRecyclerView() {
        mRecyclerView = getView ().findViewById (R.id.projects_list);
        mRecyclerView.setHasFixedSize (true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getContext ());
        mAdapter = new ProjectAdapter (projectList);
        mRecyclerView.setLayoutManager (mLayoutManager);
        mRecyclerView.setAdapter (mAdapter);
    }
}

