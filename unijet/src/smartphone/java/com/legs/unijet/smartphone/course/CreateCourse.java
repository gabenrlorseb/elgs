package com.legs.unijet.smartphone.course;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.legs.unijet.smartphone.utils.MainActivity;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.createGroupActivity.MemberCheckListAdapter;

import java.util.ArrayList;


public class CreateCourse extends AppCompatActivity {
    private EditText inputNameCourse;
    private Spinner inputDepartment, inputAcademicYear;
    private MemberCheckListAdapter mAdapter;
    TextView addedMembers;
    ArrayList<String> addedIDStudents;
    ArrayList<String> students;
    FirebaseAuth auth;
    DatabaseReference db;
    Button btnCreation;
    final StringBuilder studentsIDList = new StringBuilder();

    Bundle bundle;
    Intent intent;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.create_course);
        Bundle b = getIntent().getExtras();

        inputNameCourse = findViewById (R.id.set_name_course);
        inputDepartment = findViewById (R.id.select_department);
        inputAcademicYear = findViewById (R.id.select_academic_year);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();




        auth = FirebaseAuth.getInstance ();
        ProgressDialog loadingBar = new ProgressDialog(CreateCourse.this);
        btnCreation = findViewById (R.id.creation_button);




        btnCreation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                createCourse ();
                startActivity(new Intent(CreateCourse.this, MainActivity.class));
            }
        });





    }

    void createCourse() {
        final String name = inputNameCourse.getText ().toString ();
        final String department = inputDepartment.getSelectedItem ().toString ();
        final String academicYear = inputAcademicYear.getSelectedItem ().toString ();
        final String email = user.getEmail();
        final ArrayList<String> students = new ArrayList<>();
        students.add(email);
        if (name.isEmpty () || !name.contains ("")) {
            showError (inputNameCourse, getString(R.string.error_name_course));
        } else if (department.isEmpty ()) {
            showError2 (inputDepartment, getString(R.string.error_department));
        } else if (academicYear.isEmpty ()) {
            showError3 (inputAcademicYear, getString(R.string.error_academic_year));

        } else {


                  DatabaseReference courseReference = FirebaseDatabase.getInstance ().getReference("courses");
                    Course course = new Course (name, academicYear, department, email, students);
                    courseReference.push().setValue(course, new DatabaseReference.CompletionListener()  {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Data could not be saved " + databaseError.getMessage());
                                Toast.makeText (CreateCourse.this, "ERROR", Toast.LENGTH_SHORT).show ();
                            } else {
                                DatabaseReference dr2 = FirebaseDatabase.getInstance ().getReference ("members").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());

                                Toast.makeText (CreateCourse.this, "success", Toast.LENGTH_SHORT).show ();
                            }
                        }
                    });

                            }


    }


    private void showError(EditText input, String s) {
        input.setError (s);
        input.requestFocus ();

    }

    private void showError2(Spinner input, String s) {
        TextView errorText = (TextView) inputDepartment.getSelectedView ();
        errorText.setError (getString(R.string.show_error_2));
        //errorText.setTextColor(Color.RED);
    }
    private void showError3(Spinner input, String s) {
        TextView errorText = (TextView) inputAcademicYear.getSelectedView ();
        errorText.setError (getString(R.string.show_error_3));
        //errorText.setTextColor(Color.RED);
    }


    }

