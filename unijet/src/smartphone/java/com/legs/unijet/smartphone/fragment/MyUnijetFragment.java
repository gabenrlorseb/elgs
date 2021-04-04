package com.legs.unijet.smartphone.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.legs.unijet.smartphone.LoginActivity;
import com.legs.unijet.smartphone.profile.EditProfile;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.profile.User;
import com.legs.unijet.smartphone.utils.GsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyUnijetFragment extends Fragment {


    FirebaseUser user;
    String userId;

    DatabaseReference reference;
    TextView text_name_surname, email_login_field;

    String name, email, surname, nameFull, memberType;
    User userProfile;
    ImageView profileAvatar;

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

        Button logout_button = view.findViewById (R.id.logout_button);
        LinearLayout settings =view.findViewById (R.id.settings_button);
        Button editProfileButton=view.findViewById(R.id.profile_edit_button);



        profileAvatar = view.findViewById(R.id.member_icon);
        getAvatar(getContext());


        if (savedInstanceState == null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            userId = user.getUid();
            email = user.getEmail();
            if (email.contains("@studenti.uniba.it")) {
                reference = FirebaseDatabase.getInstance().getReference("students");
                memberType = "student";
            } else {
                reference = FirebaseDatabase.getInstance().getReference("teachers");
                memberType = "professor";
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
                String personJsonString = GsonParser.getGsonParser().toJson(userProfile);
                b.putString("PERSON_KEY", personJsonString);
                i.putExtras(b);
                i.putExtra("PERSON_TYPE", memberType);
                startActivity(i);
            }
        });


        settings .setOnClickListener (new android.view.View.OnClickListener () {
            @Override
            public void onClick(android.view.View v) {
                //startActivity (new Intent (getApplicationContext (), CreateGroup.class));

            }
        });


        return view;
    }


    public void logout (){
        FirebaseAuth.getInstance().signOut ();
        startActivity (new Intent(this.getContext(), LoginActivity.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("USER_FULL_NAME", nameFull);
        outState.putString("USER_EMAIL", email);
    }

    public void getAvatar(Context context) {

        final StorageReference[] storageReference = {FirebaseStorage.getInstance().getReference()};

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("students");
        DatabaseReference userRef = ref.child(user.getUid());

        final File localpropic = new File(context.getCacheDir(), "propic" + user.getUid() +".bmp");
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(user.getUid() + ".jpg");
        if (!localpropic.exists()) {
            fileRef.getFile(localpropic).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    profileAvatar.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    profileAvatar.setImageResource(R.drawable.ic_generic_user_avatar);
                }
            });
        } else {
            profileAvatar.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
        }
    }

}