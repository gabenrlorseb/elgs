package com.legs.unijet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.legs.unijet.utils.Utils;

public class MyUnijetFragment extends Fragment {
    Button logout_button,notifications_button, editProfileButton;
    LinearLayout setting;

    FirebaseUser user;
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private View view;
    TextView text_name_surname, email_login_field;

    String name, email, surname, nameFull;
    User userProfile;


    public MyUnijetFragment() {
//costruttore vuoto
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final android.view.View view = inflater.inflate(R.layout.myunijet, container, false);

        logout_button = view.findViewById (R.id.logout_button);
        setting=view.findViewById (R.id.settings_button);
        editProfileButton=view.findViewById(R.id.profile_edit_button);

        if (savedInstanceState == null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            userId = user.getUid();
            email = user.getEmail();
            if (email.contains("@studenti.uniba.it")) {
                reference = FirebaseDatabase.getInstance().getReference("students");
            } else {
                reference = FirebaseDatabase.getInstance().getReference("teachers");
            }
            text_name_surname = view.findViewById(R.id.text_name_surname);
            email_login_field = view.findViewById(R.id.email_logn_field);

            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userProfile = snapshot.getValue(User.class);
                    if (userProfile != null) {

                        name = userProfile.name;
                        email = user.getEmail();
                        surname = userProfile.surname;
                        nameFull = name + " " + surname;
                        text_name_surname.setText(nameFull.toUpperCase());
                        email_login_field.setText(email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            text_name_surname = view.findViewById(R.id.text_name_surname);
            email_login_field = view.findViewById(R.id.email_logn_field);
            text_name_surname.setText(savedInstanceState.getString("USER_FULL_NAME"));
            email_login_field.setText(savedInstanceState.getString("USER_EMAIL"));
        }

        logout_button.setOnClickListener (new android.view.View.OnClickListener () {
            @Override
            public void onClick(android.view.View v) {
                logout ();
            }
        });
        editProfileButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), EditProfile.class);
                Bundle b = new Bundle();
                String personJsonString = Utils.getGsonParser().toJson(userProfile);
                b.putString("PERSON_KEY", personJsonString);
                i.putExtras(b);
                startActivity(i);
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


    public void logout (){
        FirebaseAuth.getInstance().signOut ();
        startActivity (new Intent(this.getContext(), BaseActivity.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("USER_FULL_NAME", nameFull);
        outState.putString("USER_EMAIL", email);
    }


}