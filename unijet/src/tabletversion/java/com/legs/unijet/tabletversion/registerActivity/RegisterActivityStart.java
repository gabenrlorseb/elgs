package com.legs.unijet.tabletversion.registerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.legs.unijet.tabletversion.BaseActivity;
import com.legs.unijet.smartphone.R;

public class RegisterActivityStart extends AppCompatActivity {
    TextView btn;

    private EditText inputPassword, inputEmail, inputConfirmPassword;
    Button btnNext;
    FirebaseAuth auth;
    TextView haveAccount;
    private ProgressDialog LoadingBar;
    DatabaseReference dbReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.sign_up);

        inputEmail = findViewById (R.id.set_email);
        inputPassword = findViewById (R.id.set_password);
        inputConfirmPassword = findViewById (R.id.set_confirm_password);
        auth=FirebaseAuth.getInstance () ;
// Write a message to the database

        LoadingBar= new ProgressDialog (RegisterActivityStart.this);
        btnNext=findViewById (R.id.next_button);

        btnNext.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                checkCrededentials();


                //btnNext.setOnClickListener (view -> startActivity (new Intent (RegisterActivityStart.this, LoginActivity.class)));
            }
        });
        auth=FirebaseAuth.getInstance();
        LoadingBar=new ProgressDialog(RegisterActivityStart.this);

        haveAccount=findViewById(R.id.text_help);

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (RegisterActivityStart.this, BaseActivity.class);
                startActivity(intent);
            }
        });
    }




    void checkCrededentials() {
        final String email = inputEmail.getText ().toString ();
        String password = inputPassword.getText ().toString ();
        String confirmPassword = inputConfirmPassword.getText ().toString ();
        Log.d ("", "checkCrededentials: email = " + email);
        Log.d ("", "checkCrededentials: email empty = " + email.isEmpty ());

        Log.d ("", "checkCrededentials: contains studentiuniba = " + !email.contains ("@studenti.uniba.it"));

        Log.d ("", "checkCrededentials: contains uniba = " + !email.contains ("@uniba.it"));

        if (email.isEmpty () && (!email.contains ("@studenti.uniba.it") || !email.contains ("@uniba.it"))) {
            showError (inputEmail, getString(R.string.error_email));
        }/*else if (auth.){

        }*/
            else if (password.isEmpty () || password.length () < 7) {
            showError (inputPassword, getString(R.string.error_password));
        } else if (confirmPassword.isEmpty () || !confirmPassword.equals (password)) {
            showError (inputConfirmPassword, getString(R.string.error_confirm_password));
        } else {
            /*LoadingBar.setTitle ("Registration");
            LoadingBar.setMessage ("please wait check your credentials");
            LoadingBar.setCanceledOnTouchOutside (false);
            LoadingBar.show ();*/


            auth.createUserWithEmailAndPassword (email,password).addOnCompleteListener (new OnCompleteListener<AuthResult> () {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d ("TAG", "onComplete: entered");
                    if(task.isSuccessful ()){
                        Log.d ("TAG", "onComplete: success");
                        if(email.contains("@studenti.uniba.it")){
                            Log.d ("TAG", "onComplete: equals studenti uniba");
                            Toast.makeText (RegisterActivityStart.this, "", Toast.LENGTH_SHORT).show ();
                            Intent intent=new Intent (RegisterActivityStart.this,RegStudentActivity.class);
                            intent.putExtra ("email", email);
                            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity (intent);
                        }else if(email.contains("@uniba.it")){
                            Log.d ("TAG", "onComplete: equals uniba.it");
                            Toast.makeText (RegisterActivityStart.this, "  Successfully Registration ", Toast.LENGTH_SHORT).show ();
                            Intent intent=new Intent (RegisterActivityStart.this,RegProfActivity.class);
                            intent.putExtra ("email",email);
                            intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity (intent);
                        }
                    }
                    else{
                        Log.d ("TAG", "onComplete: failed");
                        Toast.makeText (RegisterActivityStart.this,task.getException ().toString (), Toast.LENGTH_SHORT).show ();
                    }
                }
            });
        }


    }
    private void showError (EditText input, String s){
        input.setError (s);
        input.requestFocus ();

    }

}

