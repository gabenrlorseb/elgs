package com.legs.unijet.tabletversion.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.legs.unijet.tabletversion.post.Post;
import com.legs.unijet.tabletversion.post.PostAdapter;
import com.legs.unijet.tabletversion.post.PostSample;
import com.legs.unijet.tabletversion.profile.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class BachecaUtils implements Runnable {

    RecyclerView rvBacheca;
    Context context;
    String type;
    String usertype;

    public BachecaUtils(String groupUID, RecyclerView rvBacheca, Context context, String usertype) {
        this.context = context;
        this.groupUID = groupUID;
        this.rvBacheca = rvBacheca;
        this.usertype = usertype;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isRunning;

    public ArrayList<PostSample> getFetchedPosts() {
        return fetchedPosts;
    }

    public void setFetchedPosts(ArrayList<PostSample> fetchedPosts) {
        this.fetchedPosts = fetchedPosts;
    }

    public ArrayList<PostSample> fetchedPosts;

    String  groupUID;
    StorageReference storageReference;




    @Override
    public void run() {
        setRunning(true);
        storageReference = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("posts");
        final DatabaseReference database3 = FirebaseDatabase.getInstance().getReference(usertype);


        final StorageReference reference1 = FirebaseStorage.getInstance().getReference("posts");

        fetchedPosts = new ArrayList<>();


        database2.child(this.groupUID).orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    final Post newPost = postSnapshot.getValue(Post.class);


                    final Bitmap[] authorBitmap = new Bitmap[1];

                    final int numberOfPics = newPost.getHasPicture();
                    final int numberOfDocs = newPost.getHasDocument();
                    final int[] numberOfLikes = new int[1];
                    final int[] numberOfComments = new int[1];


                    final String[] authorName = new String[1];
                    final String[] authorKey = new String[1];

                    Log.v("IDPOST", String.valueOf(groupUID));


                    database.child("likes/" + newPost.getCommentSectionID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            numberOfLikes[0] = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    database.child("comments/" + newPost.getCommentSectionID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            numberOfComments[0] = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                    final PostSample postToBeAdded = new PostSample(authorBitmap[0], authorName[0], newPost.getContent(), numberOfPics, numberOfDocs, newPost.getCommentSectionID(), groupUID, newPost.getTimestamp(), numberOfLikes[0], false, numberOfComments[0]);



                    database3.orderByChild("email").equalTo(newPost.getAuthor()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot3) {
                            for (DataSnapshot datas : snapshot3.getChildren()) {
                                User user = datas.getValue(User.class);
                                authorKey[0] = datas.getKey();
                                Log.v("AVVISO", "UTENTE TROVATO SU " + datas.getKey());
                                StringBuilder newSB = new StringBuilder();
                                newSB.append(user.getName());
                                newSB.append(" ");
                                newSB.append(user.getSurname());
                                authorName[0] = newSB.toString();
                                final int[] numberOfComments = {0};



                                postToBeAdded.setAuthor_name(authorName[0]);

                                postToBeAdded.setAuthor_propic(authorBitmap[0]);

                                database.child("comments").orderByKey().equalTo(newPost.getCommentSectionID()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        numberOfComments[0] = (int) snapshot.getChildrenCount();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                                fetchedPosts.add(postToBeAdded);




                            }

                            Collections.sort(fetchedPosts, new CustomComparator());

                            Log.v("Attenzione", String.valueOf(fetchedPosts.size()));
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                            PostAdapter postAdapter = new PostAdapter(fetchedPosts);
                            rvBacheca.setLayoutManager(mLayoutManager);
                            rvBacheca.setHasFixedSize(true);
                            rvBacheca.setAdapter(postAdapter);

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public interface FinishCallback<T> {
        void onComplete(T result);
    }




}