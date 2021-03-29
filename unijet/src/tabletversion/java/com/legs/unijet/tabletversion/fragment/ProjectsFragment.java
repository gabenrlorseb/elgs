package com.legs.unijet.tabletversion.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.tabletversion.course.Course;
import com.legs.unijet.tabletversion.course.CourseSample;
import com.legs.unijet.tabletversion.courseDetailsActivity.CourseDetailsActivity;
import com.legs.unijet.tabletversion.group.Group;
import com.legs.unijet.tabletversion.groupDetailsActivity.GroupActivity;
import com.legs.unijet.tabletversion.profile.User;
import com.legs.unijet.tabletversion.project.Project;
import com.legs.unijet.tabletversion.project.ProjectAdapter;
import com.legs.unijet.tabletversion.project.ProjectDetailsActivity;
import com.legs.unijet.tabletversion.project.ProjectSample;
import com.legs.unijet.tabletversion.utils.RecyclerItemClickListener;
import com.legs.unijet.smartphone.R;

import java.util.ArrayList;

public class ProjectsFragment extends Fragment {
    ImageView item;
    EditText searchEditText;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();;
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    static boolean isSinglePane = true;
    private ArrayList<ProjectSample> projectList;
    private ArrayList<Project> projects;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference db2 = FirebaseDatabase.getInstance().getReference();
    RecyclerView mRecyclerView;
    private ProjectAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate(R.layout.projects_page, container, false);
        populateList();

        item = (ImageView) view.findViewById(R.id.projects_search_button);

        searchEditText = (EditText) view.findViewById(R.id.projects_search_edit_text);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setVisibility(View.VISIBLE);
            }
        });


        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mAdapter.getFilter().filter(s);
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;

    }

    private void populateList() {
        projects = new ArrayList<Project>();


        db.child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projects.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue(String.class);
                    String course = childSnapshot.child("course").getValue(String.class);
                    String group = childSnapshot.child("group").getValue(String.class);
                    projects.add(new Project(name, course, group));

                }

                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (user.getEmail().contains("@studenti.uniba.it")) {
            fragmentStudent();
        } else if (user.getEmail().contains("@uniba.it")) {
            fragmentProfessor();
        }



    }

    private void fragmentStudent(){
        projectList = new ArrayList();
        db.child("groups").addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot){
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    final Group group = childSnapshot.getValue(Group.class);
                    db2.child("students").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            projectList.clear();

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                                    for (Project project : projects) {
                                        if (childSnapshot.child("department").getValue(String.class).equals(group.getDepartment())
                                                && project.getGroup().equals(group.getName())) {
                                            String namesString = project.getName();
                                            //TI ODIO + " " + childSnapshot.child("academicYear").getValue(String.class) ;
                                            String mail = project.getGroup();

                                            projectList.add(new ProjectSample(namesString, mail));
                                        }

                                    }
                                }
                                buildRecyclerView();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });
    }

    private void fragmentProfessor(){
        projectList = new ArrayList();
        db.child("courses").addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot){
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    final Course course = childSnapshot.getValue(Course.class);
                    db2.child("teachers").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            projectList.clear();

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                                    for (Project project : projects) {
                                        if (childSnapshot.child("department").getValue(String.class).equals(course.getDepartment())
                                                || user.getEmail().equals(course.getEmail())) {
                                            String namesString = project.getName();
                                            //TI ODIO + " " + childSnapshot.child("academicYear").getValue(String.class) ;
                                            String mail = project.getGroup();

                                            projectList.add(new ProjectSample(namesString, mail));
                                        }

                                    }
                                }
                                buildRecyclerView();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });
    }

    private void buildRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.projects_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ProjectAdapter(projectList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("PName", mAdapter.returnTitle(position));
                        bundle.putString("group", mAdapter.returnGroup(position));

                        if (isSinglePane) {
                            Fragment fragment;
                            fragment = new ProjectDetailsActivity();
                            fragment.setArguments(bundle);
                            if (searchEditText.getVisibility() == View.VISIBLE) {
                                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(
                                        getContext().INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.commit();
                        } else {
                            getChildFragmentManager().findFragmentById(R.id.fragment_container);

                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //non c'è bisogno
                    }

                })
        );
    }


    }


