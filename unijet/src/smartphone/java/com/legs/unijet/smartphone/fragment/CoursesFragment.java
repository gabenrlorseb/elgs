package com.legs.unijet.smartphone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.course.CourseAdapter;
import com.legs.unijet.smartphone.course.CourseSample;
import com.legs.unijet.smartphone.course.Course;
import com.legs.unijet.smartphone.courseDetailsActivity.CourseDetailsActivity;
import com.legs.unijet.smartphone.groupDetailsActivity.MembersDetailsActivity;
import com.legs.unijet.smartphone.profile.User;
import com.legs.unijet.smartphone.utils.RecyclerItemClickListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class CoursesFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ImageView item;
    FirebaseDatabase mdb;
    ArrayList<String> reci;
    EditText searchEditText;
    String userId;
    Course course;
    FirebaseUser auth;
    Bundle bundle;
    Intent intent;
    DatabaseReference reference;
    private ArrayList<CourseSample> courseList;
    private ArrayList<Course> courses;
    private ArrayList<String> members;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference db1 = FirebaseDatabase.getInstance ().getReference ();
    TextView notFoundTextView;
    RelativeLayout notFoundLayout;

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
        item = view.findViewById(R.id.courses_search_button);

        searchEditText = view.findViewById(R.id.courses_search_edit_text);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setVisibility(View.VISIBLE);
            }
        });

        notFoundTextView = view.findViewById(R.id.not_found_textview);
        notFoundLayout = view.findViewById(R.id.not_found);

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
        //members = new ArrayList<>();
        courses = new ArrayList();

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
                    buildRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (user==null){
            ViewCourse();
        }

        else if (user.getEmail().contains("@studenti.uniba.it")){

        fragmentStudent();
        } else if (user.getEmail().contains("@uniba.it")){
        fragmentProfessor();
        }


        }


private void fragmentProfessor(){
    courseList = new ArrayList();
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
            }
        }
        @Override
        public void onCancelled (@NonNull DatabaseError error){

        }
    });
}

private void fragmentStudent(){
    courseList = new ArrayList();
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
                        if (user == null) {
                            final Intent i = new Intent (view.getContext (), MembersDetailsActivity.class);
                            String name;
                            ArrayList<String> mail;
                            ArrayList<String> namepass;
                            ArrayList<String> nameowner;

                            i.putExtra ("authorMail", mail = mAdapter.returnProfessor (position));
                            Log.d (TAG, "onDataChange:d " + i);
                            i.putExtra ("name", name = mAdapter.returnTitle (position));

                            i.putExtra ("nameless", namepass =mAdapter.returnReci (position));
                            i.putExtra ("nameowner", nameowner =mAdapter.returnNameOwner (position));
                            System.out.println ("membri:" + namepass);


                            Log.d (TAG, "onDataChange:d " + i);
                            view.getContext ().startActivity (i);

                        }else {
                            Intent i = new Intent (view.getContext (), CourseDetailsActivity.class);
                            i.putExtra ("titleName", mAdapter.returnTitle (position));
                            i.putExtra ("subtitle", mAdapter.returnProfessor (position));
                            view.getContext ().startActivity (i);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //non c'è bisogno
                    }

                })
        );
        if (courses.isEmpty()) {
            notFoundLayout.setVisibility(View.VISIBLE);
            String[] notFoundStrings = getResources().getStringArray(R.array.not_found_strings);
            int randomIndex = new Random().nextInt(notFoundStrings.length);
            String randomName = notFoundStrings[randomIndex];
            notFoundTextView.setText(randomName);
        }
    }



    public void ViewCourse() {
        courseList = new ArrayList();
        mdb=FirebaseDatabase.getInstance ();
        db=mdb.getReference ("courses");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> courses=new ArrayList<> ();
                final ArrayList<String>nameOwners=new ArrayList<> ();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    courses.add(childSnapshot.getKey ());

                     course = childSnapshot.getValue (Course.class);

                    String name = course.getName ();

                    final String[] autor = {course.getEmail()};
                    final String[] mailU = new String[1];
                    reci = course.getMembers ();
                    System.out.println ("::"+reci);
                    db1 = mdb.getReference ("teachers");


                    final String[] nameO = {""};

                    final ArrayList<String> finalNameOWners = new ArrayList<> ();
                    final ArrayList<String> finalMailsOWners = new ArrayList<> ();
                    db1.addValueEventListener (new ValueEventListener () {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                nameOwners.add (childSnapshot.getKey ());
                                User user = childSnapshot.getValue (User.class);
                                mailU[0] =user.getEmail ();

                                if (mailU[0].equals (autor[0])) {
                                    nameO[0] = user.getName () + ( " " ) + user.getSurname ();
                                    autor[0] =user.getEmail();
                                    finalNameOWners.add(nameO[0]);
                                    finalMailsOWners.add(autor[0]);

                                }
                                System.out.println ("nameOwners:" + finalNameOWners);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    courseList.add (new CourseSample (name, autor[0],finalMailsOWners, reci,finalNameOWners));

                    System.out.println ("-:"+courseList);
                    buildRecyclerView();
                }

                Log.d (TAG, "onDataChange:d "+courseList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



    }



}

