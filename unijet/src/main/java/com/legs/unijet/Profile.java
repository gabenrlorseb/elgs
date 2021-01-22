package com.legs.unijet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

public class Profile extends Fragment {
    Button logout_button,notifications_button;
    LinearLayout setting;

    FirebaseUser user;
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private View view;

    public Profile() {
//costruttore vuoto
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate(R.layout.myunijet, container, false);

        logout_button = view.findViewById (R.id.logout_button);
        setting=view.findViewById (R.id.settings_button);
        user = FirebaseAuth.getInstance().getCurrentUser ();
        userId=user.getUid ();
        String email=user.getEmail();
        if(email.contains("@studenti.uniba.it")) {
            reference = FirebaseDatabase.getInstance ().getReference ("students");
        }
        else{
            reference = FirebaseDatabase.getInstance ().getReference ("teachers");
        }
        final TextView text_name_surname= (TextView)view.findViewById (R.id.text_name_surname);
        final TextView email_logn_field=(TextView) view.findViewById (R.id.email_logn_field);
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
                //logout (view);
                //TODO: DA RIFARE
            }
        });

        setting.setOnClickListener (new android.view.View.OnClickListener () {
            @Override
            public void onClick(android.view.View v) {
                //startActivity (new Intent (getApplicationContext (), CreateGroup.class));

            }
        });


        return view;
    }


    public void logout (View view){
        FirebaseAuth.getInstance().signOut ();//logout
        //startActivity (new Intent (getApplicationContext (), BaseActivity.class));
        //finish ();
    }


}
