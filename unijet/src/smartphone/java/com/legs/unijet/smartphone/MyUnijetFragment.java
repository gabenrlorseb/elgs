package com.legs.unijet.smartphone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.Toast;

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
import com.google.firebase.database.core.view.View;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
        startActivity (new Intent(this.getContext(), BaseActivity.class));
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
        final StorageReference[] fileRef = {storageReference[0].child(userRef + ".jpg")};

        final Bitmap[] bitmap = new Bitmap[1];
        File cachedProPic = context.getFilesDir();
        final File f = new File(cachedProPic, "profile-pic.jpg");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            bitmap[0] = BitmapFactory.decodeFile(f.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fis != null) {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (!netInfo.isConnected()) {
                Log.v("AVVISO", "File has been found in cache");
                fileRef[0].getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.v("AVVISO", "Il file è stato scaricato dal database");
                        bitmap[0] = BitmapFactory.decodeFile(f.getAbsolutePath());
                        FileOutputStream fos;
                        try {
                            fos = new FileOutputStream(f);
                            bitmap[0].compress(Bitmap.CompressFormat.JPEG, 90, fos);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("AVVISO", "File could not be fetched from database");
                    }
                });
            } else {
                Log.v("AVVISO", "File has been found in cache and internet is not available");
                bitmap[0] = BitmapFactory.decodeStream(fis);
                profileAvatar.setImageBitmap(bitmap[0]);
            }
        }
    }

}