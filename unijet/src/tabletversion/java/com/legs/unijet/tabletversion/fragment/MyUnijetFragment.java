package com.legs.unijet.tabletversion.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


import com.legs.unijet.tabletversion.LoginActivity;
import com.legs.unijet.tabletversion.post.PostAdapter;
import com.legs.unijet.tabletversion.post.PostSample;
import com.legs.unijet.tabletversion.profile.EditProfile;
import com.legs.unijet.tabletversion.profile.User;
import com.legs.unijet.tabletversion.utils.GsonParser;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.utils.MailUtils;

import java.io.File;
import java.util.ArrayList;

public class MyUnijetFragment extends Fragment {


    FirebaseUser user;
    String userId;

    DatabaseReference reference;
    TextView text_name_surname, email_login_field;

    String name, email, surname, nameFull, memberType;
    User userProfile;
    ImageView profileAvatar;

    FloatingActionButton fab;

    ArrayList<PostSample> posts;

    RecyclerView mRecyclerView;
    DatabaseReference db= FirebaseDatabase.getInstance ().getReference ();


    public MyUnijetFragment() {
//costruttore vuoto
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public android.view.View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final android.view.View view = inflater.inflate(R.layout.myunijet, container, false);

        ConstraintLayout favouritesTab = view.findViewById(R.id.left_text_view);




        Button logout_button = view.findViewById (R.id.logout_button);
        Button editProfileButton=view.findViewById(R.id.profile_edit_button);

        fab = getActivity().findViewById(R.id.fab);



        profileAvatar = view.findViewById(R.id.member_icon);
        getAvatar(getContext());


        if (savedInstanceState == null) {
            user = FirebaseAuth.getInstance().getCurrentUser();

            if (user==null){
                userId = null;
                email = null;
                reference=null;

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    favouritesTab.setVisibility(View.INVISIBLE);
                }

            } else {
                userId = user.getUid();
                email = user.getEmail();
                populateList(userId, view);

                if (MailUtils.checkDomainStudents(email)) {
                    reference = FirebaseDatabase.getInstance().getReference("students");
                    memberType = "student";
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("teachers");
                    memberType = "subtitle";
                }

            }



            text_name_surname = view.findViewById(R.id.text_name_surname);
            email_login_field = view.findViewById(R.id.email_logn_field);

            if (reference==null){

            } else {
                reference.child (userId).addListenerForSingleValueEvent (new ValueEventListener () {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userProfile = snapshot.getValue (User.class);
                        if (userProfile != null) {

                            name = userProfile.name;
                            email = user.getEmail ();
                            surname = userProfile.surname;
                            nameFull = name + " " + surname;
                            text_name_surname.setText (nameFull.toUpperCase ());
                            email_login_field.setText (email);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } else {
            if (user != null) {
                text_name_surname.setText(savedInstanceState.getString("USER_FULL_NAME"));
                email_login_field.setText(savedInstanceState.getString("USER_EMAIL"));
            }
        }

        logout_button.setOnClickListener (new android.view.View.OnClickListener () {
            @Override
            public void onClick(android.view.View v) {
                logout ();
            }
        });

        if (user!=null) {
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
        }




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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (user == null) {

        } else {

            final File localpropic = new File(context.getCacheDir(), "propic" + user.getUid() + ".bmp");
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

    private void populateList(String uid, final android.view.View view) {
        posts = new ArrayList<>();
        db.child("favourites/" + uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                    PostSample postToBeAdded = childSnapshot.getValue(PostSample.class);
                    posts.add(postToBeAdded);
                }
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    buildRecyclerView(view);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void buildRecyclerView(android.view.View view) {
        mRecyclerView = view.findViewById(R.id.favourites_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        PostAdapter mAdapter = new PostAdapter(posts);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}