package com.legs.unijet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.createGroupActivity.MemberCheckListAdapter;
import com.legs.unijet.createGroupActivity.UserChecklistSample;
import com.legs.unijet.groupDetailsActivity.GroupActivity;
import com.legs.unijet.utils.RecyclerItemClickListener;


import java.util.ArrayList;

public class CoursesFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    FirebaseUser auth;
    Bundle bundle;
    Intent intent;
    DatabaseReference reference;
    private ArrayList<CourseSample> courseList;
    private ArrayList<Course> courses;
    private ArrayList<String> members;
    SwipeRefreshLayout mSwipeRefreshLayout;
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

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.post(new Runnable() {

                    @Override
                    public void run() {

                        mSwipeRefreshLayout.setRefreshing(true);

                        populateList();
                    }
                });
            }
        });

        return view;

    }

    private void populateList() {
        //members = new ArrayList<>();
        courses = new ArrayList();
        courseList = new ArrayList();
        db.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String name = childSnapshot.child("name").getValue(String.class);
                                String department = childSnapshot.child("department").getValue(String.class);
                                String academicYear=  childSnapshot.child("academicYear").getValue(String.class);
                                String email = childSnapshot.child("email").getValue(String.class);
                                courses.add(new Course (name, academicYear, department, email, members));
                                //courseList.add(new CourseSample(namesString, mail));
                            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (user.getEmail().contains("@studenti.uniba.it")){
        fragmentStudent();
        } else if (user.getEmail().contains("uniba.it")){
        fragmentProfessor();
        }


        }


private void fragmentProfessor(){
    db.child("teachers").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            courseList.clear();
            for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                    for (Course course : courses) {
                        if (childSnapshot.child("department").getValue(String.class).equals(course.getDepartment())
                                || user.getEmail().equals(course.getEmail())) {
                            String namesString = course.getName();
                            //TI ODIO + " " + childSnapshot.child("academicYear").getValue(String.class) ;
                            String mail = course.getEmail();

                            courseList.add(new CourseSample(namesString, mail));
                        }

                    }
                }
                buildRecyclerView();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }
        @Override
        public void onCancelled (@NonNull DatabaseError error){

        }
    });
}

private void fragmentStudent(){
    db.child("students").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            courseList.clear();
            for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                    for (Course course : courses) {
                        if (childSnapshot.child("department").getValue(String.class).equals(course.getDepartment())) {
                            String namesString = course.getName();
                            //TI ODIO + " " + childSnapshot.child("academicYear").getValue(String.class) ;
                            String mail = course.getEmail();

                            courseList.add(new CourseSample(namesString, mail));
                        }

                    }
                }
                buildRecyclerView();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }
        @Override
        public void onCancelled (@NonNull DatabaseError error){

        }
    });

}


    private void buildRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.courses_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getContext());
        mAdapter = new CourseAdapter (courseList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i = new Intent(view.getContext(), CourseDetailsActivity.class);
                        i.putExtra("CName", mAdapter.returnTitle(position));
                        i.putExtra("professor", mAdapter.returnProfessor(position));
                        view.getContext().startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //non c'è bisogno
                    }

                })
        );
    }







}

