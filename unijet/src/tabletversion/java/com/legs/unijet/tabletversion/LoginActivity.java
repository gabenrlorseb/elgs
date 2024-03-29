package com.legs.unijet.tabletversion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.registerActivity.RegisterActivityStart;
import com.legs.unijet.tabletversion.utils.MainActivity;


public class LoginActivity extends AppCompatActivity {
    EditText inputEmail, inputPassword;
    // --Commented out by Inspection (21/09/2021 18:52):TextView forgotTextLink;
    // --Commented out by Inspection (21/09/2021 18:52):Bu// --Commented out by Inspection (21/09/2021 18:52):tton btnLogin,register_button, btnLoginGuest;
 // --Commented out by Inspection (21/09/2021 18:52):   ProgressDialog LoadingBar;
    FirebaseAuth auth;
    ProgressBar progressBar;
    // --Commented out by Inspection (21/09/2021 18:52):TextView btn;
    PackageManager packageManager;


    SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        // setContentView(R.layout.basic_layout_main_activity);
        setContentView (R.layout.login);
        auth=FirebaseAuth.getInstance();

//        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //      AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        //          .build();
        // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // NavigationUI.setupWithNavController(navView, navController);

        inputEmail = findViewById (R.id.set_email);
        inputPassword = findViewById (R.id.set_password);
        Button btnLogin = findViewById (R.id.confirm_button);
        Button register_button=findViewById (R.id.register_button);
        Button btnLoginGuest=findViewById (R.id.accessInAsGusest);
        //  forgotTextLink=findViewById (R.id.forgotPassword);
        // progressBar=findViewById (R.id.progressBar);


        sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        register_button.setOnClickListener (new View.OnClickListener () {


            @Override
            public void onClick(View v) {

                startActivity (new Intent (LoginActivity.this, RegisterActivityStart.class));

            }
        });

        btnLoginGuest.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (getApplicationContext (), Demo.class));
            }
        });


        btnLogin.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                checkCrededentials ();
                final String email = inputEmail.getText ().toString ();
                String password = inputPassword.getText ().toString ();
                if (TextUtils.isEmpty (email)) {
                    inputEmail.setError ("email is required");
                    return;
                }
                if (TextUtils.isEmpty (password)) {
                    inputPassword.setError ("Please enter a password");
                    return;
                }

                auth.signInWithEmailAndPassword (email, password).addOnCompleteListener (new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ()) {
                            SharedPreferences.Editor pe = sp.edit();
                            pe.putBoolean("firstRun", false);
                            pe.apply();
                            Toast.makeText (LoginActivity.this, "Login  effetuato con sucesso", Toast.LENGTH_SHORT).show ();
                            startActivity (new Intent (getApplicationContext (), MainActivity.class));
                        } else {
                            Log.d ("TAG", "onComplete: failed");
                            Toast.makeText (LoginActivity.this,task.getException ().toString (), Toast.LENGTH_SHORT).show ();
                        }

                    }
                });

            }
        });




    }

    void checkCrededentials() {
        final String email = inputEmail.getText ().toString ();
        String password = inputPassword.getText ().toString ();
        Log.d ("", "checkCrededentials: email = " + email);
        Log.d ("", "checkCrededentials: email empty = " + email.isEmpty ());

        Log.d ("", "checkCrededentials: contains studentiuniba = " + !email.contains ("@studenti.uniba.it"));

        Log.d ("", "checkCrededentials: contains uniba = " + !email.contains ("@uniba.it"));

        if (email.isEmpty () && (!email.contains ("@studenti.uniba.it") || !email.contains ("@uniba.it"))) {
            showError (inputEmail, "Your email is not valid");
        } else if (password.isEmpty () || password.length () < 7) {
            showError (inputPassword, "Your password  must be 7 characters");
        }

    }

    private void showError(EditText input, String s) {
        input.setError (s);
        input.requestFocus ();
    }


}


