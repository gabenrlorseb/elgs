package com.legs.unijet.tabletversion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.smartphone.R;

import java.util.ArrayList;

public class CreateProject extends AppCompatActivity {
    private EditText inputNameProject;
    private Spinner inputCourse, inputGroup;
    FirebaseAuth auth;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference db2;
    Button btnCreation;
    ArrayList <String>  coursess,spinnerCourses, groups;
    ArrayList <Course> courses;

    Bundle bundle;
    Intent intent;

    private FirebaseAuth firebaseAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;


    private ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference reference;
        super.onCreate (savedInstanceState);
        setContentView (R.layout.create_project);
        populateSpinnerCourse();
        populateSpinnerGroup();

        inputNameProject = findViewById (R.id.set_name_project);
        inputCourse = findViewById (R.id.select_course);
        inputGroup = findViewById (R.id.select_group);







        auth = FirebaseAuth.getInstance ();
        LoadingBar = new ProgressDialog (CreateProject.this);
        btnCreation = findViewById (R.id.creation_button_project);
        btnCreation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                createProject ();
            }
        });



        btnCreation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                createProject ();
                startActivity (new Intent (CreateProject.this, MainActivity.class));
            }
        });





    }

    void createProject() {
        final String name = inputNameProject.getText ().toString ();
         String course = inputCourse.getSelectedItem ().toString ();
         String group = inputGroup.getSelectedItem ().toString ();
        final String email = user.getEmail();
        final ArrayList<String> students = new ArrayList<>();
        students.add(email);
        if (name.isEmpty () || !name.contains ("")) {
            showError (inputNameProject, getString(R.string.error_name_course));
        } else if (course.isEmpty ()) {
            showError2 (inputCourse, getString(R.string.error_department));
        } else if (group.isEmpty ()) {
            showError3 (inputGroup, getString(R.string.error_academic_year));
        } else {
            DatabaseReference courseReference = FirebaseDatabase.getInstance ().getReference("projects");
            Project project = new Project (name, course, group);
            courseReference.push().setValue(project, new DatabaseReference.CompletionListener()  {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                        Toast.makeText (CreateProject.this, "ERROR", Toast.LENGTH_SHORT).show ();
                    } else {
                        DatabaseReference dr2 = FirebaseDatabase.getInstance ().getReference ("members").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());
                        Toast.makeText (CreateProject.this, "success", Toast.LENGTH_SHORT).show ();
                    }
                }
            });
        }
    }


    private void populateSpinnerCourse() {

        courses = new ArrayList<>();
        spinnerCourses = new ArrayList<>();
        db.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue(String.class);
                    String department = childSnapshot.child("department").getValue(String.class);
                    String academicYear=  childSnapshot.child("academicYear").getValue(String.class);
                    String email = childSnapshot.child("email").getValue(String.class);
                    courses.add(new Course (name, academicYear, department, email, coursess));
                }
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
                                String spinnerName = course.getName() + " " + course.getAcademicYear();
                                spinnerCourses.add(spinnerName);
                            }
                        }
                    }
                }ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CreateProject.this, android.R.layout.simple_spinner_item, spinnerCourses);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                inputCourse.setAdapter(arrayAdapter);
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
                        String spinnerName = course.getName() + " " + course.getAcademicYear();
                        spinnerCourses.add(spinnerName);
                    }
                }
            }
        }ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CreateProject.this, android.R.layout.simple_spinner_item, spinnerCourses);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        inputCourse.setAdapter(arrayAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}

private void populateSpinnerGroup (){
    db = FirebaseDatabase.getInstance().getReference();
    groups = new ArrayList<>();
    db.child("groups").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (user.getEmail().equals(childSnapshot.child("author").getValue(String.class))) {
                        String spinnerName = childSnapshot.child("name").getValue(String.class);
                        groups.add(spinnerName);
                    }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CreateProject.this, android.R.layout.simple_spinner_item, groups);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                inputGroup.setAdapter(arrayAdapter);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }


    private void showError(EditText input, String s) {
        input.setError (s);
        input.requestFocus ();

    }

    private void showError2(Spinner input, String s) {
        TextView errorText = (TextView) inputCourse.getSelectedView ();
        errorText.setError (getString(R.string.show_error_2));
        //errorText.setTextColor(Color.RED);
    }
    private void showError3(Spinner input, String s) {
        TextView errorText = (TextView) inputGroup.getSelectedView ();
        errorText.setError (getString(R.string.show_error_3));
        //errorText.setTextColor(Color.RED);
    }


}


