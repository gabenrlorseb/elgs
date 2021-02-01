package com.legs.unijet;

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
import com.legs.unijet.createGroupActivity.UserSample;

import java.util.ArrayList;

public class CreateProject extends AppCompatActivity {
    private EditText inputNameProject;
    private Spinner inputCourse, inputGroup;
    FirebaseAuth auth;
    DatabaseReference db;
    Button btnCreation;
    ArrayList <String> courses;

    Bundle bundle;
    Intent intent;

    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
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
                checkCrededentials ();
            }
        });



        btnCreation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                checkCrededentials ();
                startActivity (new Intent (CreateProject.this, MainActivity.class));
            }
        });





    }


    void checkCrededentials() {

        String name = inputNameProject.getText ().toString ();
        String course = inputCourse.getSelectedItem ().toString ();
        String group = inputGroup.getSelectedItem ().toString ();
        if (name.isEmpty () || !name.contains ("")) {
            showError (inputNameProject, getString(R.string.error_name_project));
        } else if (course.isEmpty ()) {
            showError2 (inputCourse, getString(R.string.error_name_course));
        } else if (group.isEmpty ()) {
            showError3 (inputGroup, getString(R.string.error_name_group));
        }
        else {
            db = FirebaseDatabase.getInstance ().getReference ("courses").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());
            intent = getIntent ();
            bundle = intent.getExtras ();



            Project project = new Project (name, course, group);
            if (project == null) {
                Log.d ("TAG", "checkCrededentials: nullo");
            } else {
                Log.d ("TAG", "checkCrededentials: " + project.getName ());
            }
            Toast.makeText (this, "success", Toast.LENGTH_SHORT).show ();



            LoadingBar.setTitle (getString (R.string.project_creation));
            LoadingBar.setMessage (getString(R.string.check_credentials));
            LoadingBar.setCanceledOnTouchOutside (false);

        }


    }

private void populateSpinnerCourse() {
    db = FirebaseDatabase.getInstance().getReference();
    courses = new ArrayList<>();
    db.child("courses").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                for (DataSnapshot childSnapshot2 : childSnapshot.getChildren()) {
                    for (DataSnapshot childSnapshot3 : childSnapshot2.getChildren()) {

                        for (DataSnapshot childSnapshot4 : childSnapshot3.getChildren()) {

                            String spinnerName = childSnapshot4.child("name").getValue(String.class) + " "
                                    + childSnapshot4.child("academicYear").getValue(String.class);
                            courses.add(spinnerName);
                        }
                    }
                }
            }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CreateProject.this, android.R.layout.simple_spinner_item, courses);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        inputCourse.setAdapter(arrayAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}

private void populateSpinnerGroup (){

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


