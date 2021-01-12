package com.legs.unijet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

public class ProfileTeacher extends AppCompatActivity {
    Button logout_button,settings_button,notifications_button;


    FirebaseUser user;
    String userId;
    DatabaseReference reference;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.myunijet);
        logout_button = findViewById (R.id.logout_button);

        user = FirebaseAuth.getInstance().getCurrentUser ();
        reference = FirebaseDatabase.getInstance ().getReference ("teachers");
        userId=user.getUid ();
        final TextView text_name_surname= (TextView)findViewById (R.id.text_name_surname);
        final TextView email_logn_field=(TextView) findViewById (R.id.email_logn_field);

        reference.child(userId).addListenerForSingleValueEvent (new ValueEventListener () {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue (User.class);
                if (userProfile != null) {

                    String name = userProfile.name;
                    String email = user.getEmail();

                    String surname = userProfile.surname;
                    String nameFull= name+"  "+surname;
                    text_name_surname.setText (nameFull.toUpperCase ());
                    email_logn_field.setText (email);
                }else{
                    Log.e ("login","fuori ondatachange");
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






    }
    public void logout (View view){
        FirebaseAuth.getInstance ().signOut ();//logout
        startActivity (new Intent (getApplicationContext (), BaseActivity.class));
        finish ();
    }


}


