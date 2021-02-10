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
                                ArrayList<String> members = childSnapshot.child("members").getValue(ArrayList.class);
                                courses.add(new Course (name, academicYear, department, email, members));
                                //courseList.add(new CourseSample(namesString, mail));
                            }

                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for  (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if(user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                        for (Course course : courses) {
                            if (childSnapshot.child("department").getValue(String.class).equals(course.getDepartment())) {
                                String namesString = course.getName() + " " + course.getAcademicYear();
                                String mail = getString(R.string.professor) + " " + course.getEmail();
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
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(final View view, final int position) {
                        final AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getContext());
                        alertbox.setTitle(view.getContext().getString(R.string.sign_up_course));
                        alertbox.setMessage(view.getContext().getString(R.string.would_course));
                        alertbox.setPositiveButton(view.getContext().getString(R.string.YES), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i = new Intent (view.getContext(), CourseDetailsActivity.class);
                                i.putExtra("CName", mAdapter.returnTitle(position));
                                i.putExtra("professor", mAdapter.returnProfessor(position));

                                view.getContext().startActivity(i);
                            }
                        });


                        alertbox.setNegativeButton(view.getContext().getString(R.string.NO), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert= alertbox.create();
                        alert.show();
                        /*Intent i = new Intent(view.getContext(), CourseDetailsActivity.class);
                        i.putExtra("CName", mAdapter.returnTitle(position));
                        i.putExtra("professor", mAdapter.returnProfessor(position));
                        view.getContext().startActivity(i);*/
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //non c'è bisogno
                    }

                })
        );
    }
}
