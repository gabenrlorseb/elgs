package com.legs.unijet.tabletversion.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.legs.unijet.tabletversion.feedback.Feedback;
import com.legs.unijet.tabletversion.feedback.FeedbackAdapter;
import com.legs.unijet.tabletversion.feedback.FeedbackSample;
import com.legs.unijet.tabletversion.post.Post;
import com.legs.unijet.tabletversion.post.PostAdapter;
import com.legs.unijet.tabletversion.post.PostSample;
import com.legs.unijet.tabletversion.profile.User;

import java.util.ArrayList;
import java.util.Collections;

public class FeedbackUtils {
    RecyclerView rvBacheca;
    Context context;
    String usertype;

    public FeedbackUtils(String groupUID, RecyclerView rvBacheca, Context context) {
        this.context = context;
        this.groupUID = groupUID;
        this.rvBacheca = rvBacheca;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isRunning;

    public ArrayList<FeedbackSample> getFetchedPosts() {
        return fetchedPosts;
    }

    public void setFetchedPosts(ArrayList<FeedbackSample> fetchedPosts) {
        this.fetchedPosts = fetchedPosts;
    }

    public ArrayList<FeedbackSample> fetchedPosts;

    String  groupUID;
    StorageReference storageReference;


    public void run() {
        setRunning(true);
        storageReference = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("feedbacks");


        final StorageReference reference1 = FirebaseStorage.getInstance().getReference("feedbacks");

        fetchedPosts = new ArrayList<>();


        database2.child(this.groupUID).orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    final Feedback newFeedback = postSnapshot.getValue(Feedback.class);


                    final Bitmap[] authorBitmap = new Bitmap[1];


                    final int[] numberOfLikes = new int[1];
                    final int[] numberOfComments = new int[1];


                    final String[] authorName = new String[1];
                    final String[] authorKey = new String[1];



                    database.child("likes/" + newFeedback.getCommentSectionID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            numberOfLikes[0] = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    database.child("comments/" + newFeedback.getCommentSectionID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            numberOfComments[0] = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    if (newFeedback.getAuthor().contains("@studenti.uniba.it")) {
                        usertype = "students";
                    } else {
                        usertype = "teachers";
                    }

                    DatabaseReference database3 = FirebaseDatabase.getInstance().getReference(usertype);



                    final FeedbackSample postToBeAdded = new FeedbackSample(authorBitmap[0], authorName[0], newFeedback.getContent(), newFeedback.getCommentSectionID(), groupUID, newFeedback.getTimestamp(), numberOfLikes[0], false, numberOfComments[0], newFeedback.getRating());



                    database3.orderByChild("email").equalTo(newFeedback.getAuthor()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot3) {
                            for (DataSnapshot datas : snapshot3.getChildren()) {
                                User user = datas.getValue(User.class);
                                authorKey[0] = datas.getKey();
                                StringBuilder newSB = new StringBuilder();
                                newSB.append(user.getName());
                                newSB.append(" ");
                                newSB.append(user.getSurname());
                                authorName[0] = newSB.toString();
                                final int[] numberOfComments = {0};



                                postToBeAdded.setAuthor_name(authorName[0]);

                                postToBeAdded.setAuthor_propic(authorBitmap[0]);

                                database.child("comments").orderByKey().equalTo(newFeedback.getCommentSectionID()).addValueEventListener(new ValueEventListener() {
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


                            Collections.reverse(fetchedPosts);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                            FeedbackAdapter feedbackAdapter = new FeedbackAdapter(fetchedPosts);
                            rvBacheca.setLayoutManager(mLayoutManager);
                            rvBacheca.setHasFixedSize(true);
                            rvBacheca.setAdapter(feedbackAdapter);

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
