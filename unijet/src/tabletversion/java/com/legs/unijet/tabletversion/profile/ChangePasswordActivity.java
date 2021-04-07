package com.legs.unijet.tabletversion.profile;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.legs.unijet.smartphone.R;


public class ChangePasswordActivity extends AppCompatActivity {


    EditText inputOldPassword, inputNewPassword, inputRepeatPassword;
    FirebaseAuth auth;
    Button changePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.change_password);
        Bundle b = getIntent().getExtras();

        inputOldPassword = findViewById (R.id.input_old_password);
        inputNewPassword = findViewById (R.id.input_new_password);
        inputRepeatPassword = findViewById (R.id.repeat_password);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        auth = FirebaseAuth.getInstance ();
        changePassword = findViewById (R.id.creation_button);





        changePassword.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (inputNewPassword.getText().toString().trim().equals("") || inputOldPassword.getText().toString().trim().equals("") || inputRepeatPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill all the forms", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!inputNewPassword.getText().toString().equals(inputRepeatPassword.getText().toString())) {
                    Toast.makeText(getBaseContext(), "The passwords don't match", Toast.LENGTH_LONG).show();
                    return;
                }
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), String.valueOf(inputOldPassword.getText().toString()));

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(inputNewPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getBaseContext(), "Password has been updated", Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                Toast.makeText(getBaseContext(), "There has been an error", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getBaseContext(), "Your current password is wrong", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

            }
        });





    }




    }

