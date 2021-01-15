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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegStudentActivity  extends RegisterActivity {

    private EditText inputName, inputSurname,inputMatricola, inputDateBorn,inputEmail;
    private Spinner inputDepartment,inputGender, inputAteneo;
    DatePickerDialog.OnDateSetListener setListener;
    Button btnRegister;


    Intent intent;
DatabaseReference db;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog LoadingBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.sign_up_student);

        inputName = findViewById (R.id.set_name_student);
        inputSurname = findViewById (R.id.set_surname_student);
        inputDepartment = findViewById (R.id.select_departement_student);
        inputAteneo = findViewById (R.id.select_sede_student);
        inputGender=findViewById(R.id.select_gender_student);
        inputMatricola=findViewById (R.id.set_matricola_student);
        inputDateBorn=findViewById (R.id.set_birth_day_student);
        TextView btn=findViewById(R.id.text_help_student);



        btn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                startActivity(new Intent (RegStudentActivity.this,BaseActivity.class));

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
                        RegStudentActivity.this, new DatePickerDialog.OnDateSetListener () {
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



      //  StringBuilder date = new StringBuilder ();

       // date.append (day.toString());



        auth = FirebaseAuth.getInstance ();
        LoadingBar = new ProgressDialog (RegStudentActivity.this);
        btnRegister = findViewById (R.id.confirm_button_student);
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
                startActivity (new Intent (RegStudentActivity.this, BaseActivity.class));
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
            showError (inputName, getString(R.string.error_name));
        }  else if (surname.isEmpty () || !surname.contains ("")) {
            showError (inputSurname, getString(R.string.error_surname));
        } else if (dipartimento.isEmpty ()) {
            showError2 (inputDepartment, getString(R.string.error_department));
        } else if (gender.isEmpty ()) {
            showError3 (inputGender, getString(R.string.error_gender));
        } else if (ateneo.isEmpty ()) {
            showError4 (inputAteneo, getString(R.string.error_campus));
        }  else {
          db=FirebaseDatabase.getInstance ().getReference ("students").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());

            User student= new User(name,surname,matricola,dipartimento,ateneo,gender,dateBorn);
            if(student == null) {
                Log.d ("TAG", "checkCrededentials: nullo");
            } else {
                Log.d ("TAG", "checkCrededentials: "+student.getName ());
            }
             Toast.makeText (this, "success", Toast.LENGTH_SHORT).show ();


            db.setValue (student);
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
        errorText.setError (getString(R.string.show_error_2));
        //errorText.setTextColor(Color.RED);
    }
    private void showError3(Spinner input, String s) {
        TextView errorText = (TextView) inputGender.getSelectedView ();
        errorText.setError (getString(R.string.show_error_3));
        //errorText.setTextColor(Color.RED);
    }
    private void showError4(Spinner input, String s) {
        TextView errorText = (TextView) inputAteneo.getSelectedView ();
        errorText.setError (getString(R.string.show_error_3));
        //errorText.setTextColor(Color.RED);

    }



}
