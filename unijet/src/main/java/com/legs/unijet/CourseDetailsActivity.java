package com.legs.unijet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseDetailsActivity extends AppCompatActivity {
    Bundle bundle;
    Intent intent;
    CollapsingToolbarLayout nameCourse;
    TextView mailProfessor;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<Course> courses;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsing_toolbar_layout_sample);
        nameCourse = findViewById(R.id.collapsing_toolbar);
        mailProfessor = findViewById(R.id.toolbar_subtitle);

        courses = new ArrayList<>();


        db.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot childSnapshot2 : childSnapshot.getChildren()) {
                        for (DataSnapshot childSnapshot3 : childSnapshot2.getChildren()) {
                            for (DataSnapshot childSnapshot4 : childSnapshot3.getChildren()) {
                                String name = childSnapshot4.child("name").getValue(String.class);
                                String department = childSnapshot4.child("department").getValue(String.class);
                                String academicYear=  childSnapshot4.child("academicYear").getValue(String.class);
                                String email = childSnapshot4.child("email").getValue(String.class);
                                courses.add(new Course (name, academicYear, department, email));
                                //courseList.add(new CourseSample(namesString, mail));
                            }
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}


