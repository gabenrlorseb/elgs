package com.legs.unijet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegProfActivity extends AppCompatActivity {


    private EditText inputName, inputSurname,inputMatricola, inputDateBorn;
    private Spinner inputDepartment,inputGender, inputAteneo;
    DatePickerDialog.OnDateSetListener setListener;
    Button btnRegister;
    DatabaseReference db;
    FirebaseAuth auth;


    Intent intent;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog LoadingBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.sign_up_teacher);

        inputName = findViewById (R.id.set_name_teacher);
        inputSurname = findViewById (R.id.set_surname_teacher);
        inputDepartment = findViewById (R.id.select_departement_teacher);
        inputAteneo = findViewById (R.id.select_sede_teacher);
        inputGender=findViewById(R.id.select_gender_teacher);
        inputMatricola=findViewById (R.id.set_matricola_teacher);
        inputDateBorn=findViewById (R.id.set_birth_day_teacher);
        TextView btn=findViewById(R.id.text_help_teacher);



        btn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                startActivity(new Intent (RegProfActivity.this,BaseActivity.class));

            }
        });
        firebaseAuth = FirebaseAuth.getInstance ();



        Calendar calendar= Calendar.getInstance ();
        final int day=calendar.get (Calendar.DAY_OF_MONTH);
        final int month= calendar.get (Calendar.MONTH);
        final int year= calendar.get (Calendar.YEAR);
        inputDateBorn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog (
                        RegProfActivity.this, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String date= day+"/"+ month+"/"+year;
                        inputDateBorn.setText(date);

                    }
                },day,month,year);
                datePickerDialog.show();
            }
        });






        auth = FirebaseAuth.getInstance ();
        LoadingBar = new ProgressDialog (RegProfActivity.this);
        btnRegister = findViewById (R.id.confirm_button_teacher);
        btnRegister.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                checkCrededentials ();
            }
        });



        btnRegister.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                checkCrededentials ();
                startActivity (new Intent (RegProfActivity.this, BaseActivity.class));
            }
        });





    }


    void checkCrededentials() {

        String name = inputName.getText ().toString ();
        String surname = inputSurname.getText ().toString ();
        String matricola = inputMatricola.getText ().toString ();
        String dipartimento = inputDepartment.getSelectedItem ().toString ();
        String ateneo = inputAteneo.getSelectedItem ().toString ();
        String gender=inputGender.getSelectedItem ().toString ();
        String dateBorn=inputDateBorn.getText ().toString ();
        if (name.isEmpty () || !name.contains ("")) {
            showError (inputName, "Your name is not valid");
        }  else if (surname.isEmpty () || !surname.contains ("")) {
            showError (inputSurname, "Your surname is not valid");
        } else if (dipartimento.isEmpty ()) {
            showError2 (inputDepartment, "Your name of dipartimento is not valid");
        } else if (gender.isEmpty ()) {
            showError3 (inputDepartment, "Your selection is not valid");
        } else if (ateneo.isEmpty ()) {
            showError4 (inputAteneo, "Your name of ateneo is not valid");
        }  else {
            db=FirebaseDatabase.getInstance ().getReference ("teachers").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras ();
            String email = bundle.getString ("email");
            User teacher= new User(name,surname,matricola,dipartimento,ateneo,gender,dateBorn,email);
            if(teacher == null) {
                Log.d ("TAG", "checkCrededentials: nullo");
            } else {
                Log.d ("TAG", "checkCrededentials: "+teacher.getName ());
            }
            Toast.makeText (this, "success", Toast.LENGTH_SHORT).show ();

            db.setValue (teacher);

            LoadingBar.setTitle ("Registration");
            LoadingBar.setMessage ("please wait check your credentials");
            LoadingBar.setCanceledOnTouchOutside (false);


        }


    }

    private void showError(EditText input, String s) {
        input.setError (s);
        input.requestFocus ();

    }

    private void showError2(Spinner input, String s) {
        TextView errorText = (TextView) inputDepartment.getSelectedView ();
        errorText.setError ("Name of ");

    }
    private void showError3(Spinner input, String s) {
        TextView errorText = (TextView) inputGender.getSelectedView ();
        errorText.setError ("Error not valid");

    }
    private void showError4(Spinner input, String s) {
        TextView errorText = (TextView) inputAteneo.getSelectedView ();
        errorText.setError ("Error not valid");
        //errorText.setTextColor(Color.RED);

    }



}
