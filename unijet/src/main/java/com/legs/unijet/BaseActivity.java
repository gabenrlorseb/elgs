package com.legs.unijet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class BaseActivity extends AppCompatActivity {
    TextView btn;
    EditText inputEmail,inputPassword;
    Button btnLogin;
    ProgressDialog LoadingBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.basic_layout_main_activity);
        setContentView(R.layout.login);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
  //      AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
    //            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
      //          .build();
       // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
       // NavigationUI.setupWithNavController(navView, navController);

    TextView btn=findViewById(R.id.text_help);
    inputEmail = findViewById (R.id.set_email);
    inputPassword = findViewById (R.id.set_password);
    btnLogin= findViewById (R.id.confirm_button);

        btn.setOnClickListener(new View.OnClickListener() {



        @Override
        public void onClick(View v) {
            checkCrededentials();
            startActivity(new Intent (BaseActivity.this,RegisterActivity.class));

        }
    });
    auth= FirebaseAuth.getInstance();


    LoadingBar=new ProgressDialog(BaseActivity.this);
}

    private void checkCrededentials() {
        String email = inputEmail.getText ().toString ();
        String password = inputPassword.getText ().toString ();

        if (email.isEmpty () || !email.contains ("@studenti.uniba.it")) {
            showError (inputEmail, "Your email is not valid");
        } else if (password.isEmpty () || password.length () < 7) {
            showError (inputPassword, "Your password  must be 7 characters");
        }
        else {
            LoadingBar.setTitle ("Login");
            LoadingBar.setMessage ("please wait check your credentials");
            LoadingBar.setCanceledOnTouchOutside (false);
            LoadingBar.show ();
            auth.createUserWithEmailAndPassword (email,password).addOnCompleteListener (new OnCompleteListener<AuthResult> () {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful ()){
                        LoadingBar.dismiss ();
                        Intent intent=new Intent (BaseActivity.this,BaseActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity (intent);
                    }
                    else{
                        Toast.makeText (BaseActivity.this,task.getException ().toString (), Toast.LENGTH_SHORT).show ();
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

