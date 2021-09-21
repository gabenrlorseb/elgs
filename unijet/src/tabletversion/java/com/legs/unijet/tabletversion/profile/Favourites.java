package com.legs.unijet.tabletversion.profile;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.post.PostAdapter;
import com.legs.unijet.tabletversion.post.PostSample;

import java.util.ArrayList;


public class Favourites extends AppCompatActivity {

    ArrayList<PostSample> posts;

    RecyclerView mRecyclerView;
    DatabaseReference db= FirebaseDatabase.getInstance ().getReference ();



    @Override
    protected void onCreate (Bundle savedInstance) {

        setContentView(R.layout.simple_activity_recyclerview_list);

        Toolbar actionBar = findViewById(R.id.toolbar);
        actionBar.setTitle("Favourites");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        populateList(user.getUid());

        super.onCreate(savedInstance);
    }

    private void populateList(String uid) {
        posts = new ArrayList<>();
        db.child("favourites/" + uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                    PostSample postToBeAdded = childSnapshot.getValue(PostSample.class);
                    posts.add(postToBeAdded);
                }
                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview_details);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        PostAdapter mAdapter = new PostAdapter(posts);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }




    }

