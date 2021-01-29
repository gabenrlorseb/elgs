package com.legs.unijet;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class CreateCourse extends AppCompatActivity {
    private EditText inputNameCourse;
    private Spinner inputDepartment, inputAcademicYear;
    FirebaseAuth auth;
    DatabaseReference db;
    Button btnCreation;

    Bundle bundle;
    Intent intent;

    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userId;


    private ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.create_course);

        inputNameCourse = findViewById (R.id.set_name_course);
        inputDepartment = findViewById (R.id.select_department);
        inputAcademicYear = findViewById (R.id.select_academic_year);

        firebaseAuth = FirebaseAuth.getInstance ();




        auth = FirebaseAuth.getInstance ();
        LoadingBar = new ProgressDialog (CreateCourse.this);
        btnCreation = findViewById (R.id.creation_button);
        btnCreation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                checkCrededentials ();
            }
        });



        btnCreation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                checkCrededentials ();
                startActivity (new Intent (CreateCourse.this, MainActivity.class));
            }
        });





    }


    void checkCrededentials() {

        String name = inputNameCourse.getText ().toString ();
        String department = inputDepartment.getSelectedItem ().toString ();
        String academicYear = inputAcademicYear.getSelectedItem ().toString ();
        if (name.isEmpty () || !name.contains ("")) {
            showError (inputNameCourse, getString(R.string.error_name_course));
        } else if (department.isEmpty ()) {
            showError2 (inputDepartment, getString(R.string.error_department));
        } else if (academicYear.isEmpty ()) {
            showError3 (inputAcademicYear, getString(R.string.error_academic_year));
        }
          else {
            db = FirebaseDatabase.getInstance ().getReference ("courses").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());
            intent = getIntent ();
            bundle = intent.getExtras ();

            String email = bundle.getString ("email");
            Course course = new Course (name, academicYear, department, email);
            if (course == null) {
                Log.d ("TAG", "checkCrededentials: nullo");
            } else {
                Log.d ("TAG", "checkCrededentials: " + course.getName ());
            }
            Toast.makeText (this, "success", Toast.LENGTH_SHORT).show ();
            db.child(course.getAcademicYear()).child(course.getDepartment()).
                    child(course.getName()).setValue (course);
         

            LoadingBar.setTitle (getString (R.string.course_creation));
            LoadingBar.setMessage (getString(R.string.check_credentials));
            LoadingBar.setCanceledOnTouchOutside (false);

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

