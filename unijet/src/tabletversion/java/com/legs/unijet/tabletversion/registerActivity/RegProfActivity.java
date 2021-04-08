package com.legs.unijet.tabletversion.registerActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.LoginActivity;
import com.legs.unijet.tabletversion.profile.User;
import com.legs.unijet.tabletversion.utils.MainActivity;

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
        inputDateBorn.setInputType(InputType.TYPE_NULL);
        TextView btn=findViewById(R.id.text_help_teacher);



        btn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                startActivity(new Intent (RegProfActivity.this, LoginActivity.class));

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
                Calendar minCal = Calendar.getInstance();
                minCal.set(Calendar.YEAR, minCal.get(Calendar.YEAR) - 80);
                Calendar maxCal = Calendar.getInstance();
                maxCal.set(Calendar.YEAR, maxCal.get(Calendar.YEAR) - 18);
                datePickerDialog.getDatePicker().setMinDate(minCal.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(maxCal.getTimeInMillis());
                datePickerDialog.show();
                datePickerDialog.show();
            }
        });






        auth = FirebaseAuth.getInstance ();

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
                startActivity (new Intent (RegProfActivity.this, LoginActivity.class));
            }
        });





    }


    void checkCrededentials() {

        final String name = inputName.getText ().toString ();
        final String surname = inputSurname.getText ().toString ();
        final String professorID = inputMatricola.getText ().toString ();
        final String department = inputDepartment.getSelectedItem ().toString ();
        final String universityCampus = inputAteneo.getSelectedItem ().toString ();
        final String gender=inputGender.getSelectedItem ().toString ();
        final String dateBorn=inputDateBorn.getText ().toString ();
        if (name.isEmpty () || !name.contains ("")) {
            showError (inputName, getString(R.string.error_name));
        } else if (surname.isEmpty () || !surname.contains ("")) {
            showError(inputSurname, getString(R.string.error_surname));
        } else if (dateBorn.isEmpty ()){
            showError (inputDateBorn, getString(R.string.error_date));
        } else if (department.isEmpty ()) {
            showError2 (inputDepartment, getString(R.string.error_department));
        } else if (gender.isEmpty ()) {
            showError3 (inputGender, getString(R.string.error_gender));
        } else if (universityCampus.isEmpty ()) {
            showError4 (inputAteneo, getString(R.string.error_campus));
        }
        else {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras ();
            final String email = bundle.getString ("email");
            final String pw = bundle.getString("pw");




            final ProgressDialog dialog = ProgressDialog.show(RegProfActivity.this, "",
                    getString(R.string.wait), true);
            dialog.show();



            auth.createUserWithEmailAndPassword (email,pw).addOnCompleteListener (new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful ()){
                        Log.d ("TAG", "onComplete: success");
                        auth.signInWithEmailAndPassword (email, pw).addOnCompleteListener (new OnCompleteListener<AuthResult> () {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful ()) {
                                    SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                                    SharedPreferences.Editor pe = sp.edit();
                                    pe.putBoolean("firstRun", false);
                                    pe.apply();
                                    dialog.dismiss();
                                    db= FirebaseDatabase.getInstance ().getReference ("teachers").child (auth.getCurrentUser ().getUid ());

                                    final User professor= new User(name,surname,professorID,department,universityCampus,gender,dateBorn,email);
                                    db.setValue (professor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText (RegProfActivity.this, "Welcome to UniJet!", Toast.LENGTH_SHORT).show ();
                                            startActivity (new Intent (getApplicationContext (), MainActivity.class));
                                        }
                                    });

                                } else {
                                    dialog.dismiss();
                                    Toast.makeText (RegProfActivity.this,task.getException ().toString (), Toast.LENGTH_SHORT).show ();
                                }

                            }
                        });

                    }
                    else{
                        Log.d ("TAG", "onComplete: failed");
                        Toast.makeText (RegProfActivity.this,R.string.generic_error, Toast.LENGTH_SHORT).show ();
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
