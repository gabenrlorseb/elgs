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

public class CreateProject extends AppCompatActivity {
    private EditText inputNameProject;
    private Spinner inputCourse, inputGroup;
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
        DatabaseReference reference;
        super.onCreate (savedInstanceState);
        setContentView (R.layout.create_project);

        inputNameProject = findViewById (R.id.set_name_project);
        inputCourse = findViewById (R.id.select_course);
        inputGroup = findViewById (R.id.select_group);

        firebaseAuth = FirebaseAuth.getInstance ();

        db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myunijet-default-rtdb.firebaseio.com/courses/CI67AHZLu5Z3Teq49qcy1TI3Lz72/2020-2021/Chemstry");





        auth = FirebaseAuth.getInstance ();
        LoadingBar = new ProgressDialog (CreateProject.this);
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
            db.child(project.getGroup()).child(project.getCourse()).
                    child(project.getName()).setValue (project);


            LoadingBar.setTitle (getString (R.string.project_creation));
            LoadingBar.setMessage (getString(R.string.check_credentials));
            LoadingBar.setCanceledOnTouchOutside (false);

        }


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


