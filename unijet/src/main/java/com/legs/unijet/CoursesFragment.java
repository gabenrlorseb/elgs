package com.legs.unijet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.createGroupActivity.MemberAdapter;
import com.legs.unijet.createGroupActivity.UserSample;

import java.util.ArrayList;

public class CoursesFragment extends Fragment {
    FirebaseUser course;
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private ArrayList<CourseSample> courseList;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    RecyclerView mRecyclerView;
    private CourseAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate(R.layout.courses_page, container, false);
        populateList();

        return view;

    }

    private void populateList() {
        courseList = new ArrayList<CourseSample>();
        db.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot childSnapshot2 : childSnapshot.getChildren()) {
                        for (DataSnapshot childSnapshot3 : childSnapshot2.getChildren()) {

                            for (DataSnapshot childSnapshot4 : childSnapshot3.getChildren()) {
                                String namesString = childSnapshot4.child("name").getValue(String.class) +
                                        " " +
                                        childSnapshot4.child("academicYear").getValue(String.class);
                                String mail = childSnapshot4.child("email").getValue(String.class);
                                courseList.add(new CourseSample(namesString, mail));
                            }
                        }
                    }


                }
                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buildRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.courses_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CourseAdapter(courseList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
