package com.legs.unijet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

public class Profile extends AppCompatActivity {
    Button logout_button,notifications_button;
TextView setting;

    FirebaseUser user;
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.myunijet);
        logout_button = findViewById (R.id.logout_button);
        setting=findViewById (R.id.setting);
        user = FirebaseAuth.getInstance().getCurrentUser ();
        userId=user.getUid ();
        String email=user.getEmail();
        if(email.contains("@studenti.uniba.it")) {
            reference = FirebaseDatabase.getInstance ().getReference ("students");
        }
        else{
            reference = FirebaseDatabase.getInstance ().getReference ("teachers");
        }
        final TextView text_name_surname= (TextView)findViewById (R.id.text_name_surname);
        final TextView email_logn_field=(TextView) findViewById (R.id.email_logn_field);
        reference.child (userId).addListenerForSingleValueEvent (new ValueEventListener () {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue (User.class);
                if (userProfile != null) {
                    String name = userProfile.name;
                    String email = user.getEmail ();

                    String surname = userProfile.surname;
                    String nameFull = name + "  " + surname;
                    text_name_surname.setText (nameFull.toUpperCase ());
                    email_logn_field.setText (email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logout_button.setOnClickListener (new android.view.View.OnClickListener () {
            @Override
            public void onClick(android.view.View v) {
                logout (view);




            }
        });
        setting.setOnClickListener (new android.view.View.OnClickListener () {
            @Override
            public void onClick(android.view.View v) {
                startActivity (new Intent (getApplicationContext (), CreateGroup.class));



            }
        });





    }
    public void logout (View view){
        FirebaseAuth.getInstance ().signOut ();//logout
        startActivity (new Intent (getApplicationContext (), BaseActivity.class));
        finish ();
    }


}
