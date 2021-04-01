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
import com.legs.unijet.tabletversion.comment.Comment;
import com.legs.unijet.tabletversion.comment.CommentAdapter;
import com.legs.unijet.tabletversion.comment.CommentSample;
import com.legs.unijet.tabletversion.feedback.Feedback;
import com.legs.unijet.tabletversion.feedback.FeedbackAdapter;
import com.legs.unijet.tabletversion.feedback.FeedbackSample;
import com.legs.unijet.tabletversion.profile.User;

import java.util.ArrayList;
import java.util.Collections;

public class CommentUtils {
    RecyclerView rvBacheca;
    Context context;
    String type;
    String usertype;

    public CommentUtils(String groupUID, RecyclerView rvBacheca, Context context, String usertype) {
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

    public ArrayList<CommentSample> getFetchedPosts() {
        return fetchedPosts;
    }

    public void setFetchedPosts(ArrayList<CommentSample> fetchedPosts) {
        this.fetchedPosts = fetchedPosts;
    }

    public ArrayList<CommentSample> fetchedPosts;

    String groupUID;
    StorageReference storageReference;


    public void run() {
        setRunning(true);
        storageReference = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("comments");
        final DatabaseReference database3 = FirebaseDatabase.getInstance().getReference(usertype);


        final StorageReference reference1 = FirebaseStorage.getInstance().getReference("comments");

        fetchedPosts = new ArrayList<>();


        database2.child(this.groupUID).orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    final Comment newComment = postSnapshot.getValue(Comment.class);


                    final Bitmap[] authorBitmap = new Bitmap[1];


                    final int[] numberOfLikes = new int[1];


                    final String[] authorName = new String[1];
                    final String[] authorKey = new String[1];

                    Log.v("IDPOST", String.valueOf(groupUID));




                    final CommentSample postToBeAdded = new CommentSample(authorBitmap[0], authorName[0], newComment.getContent(), newComment.getCommentSectionId(),groupUID, newComment.getTimestamp(), numberOfLikes[0], false);


                    database3.orderByChild("email").equalTo(newComment.getAuthor()).addListenerForSingleValueEvent(new ValueEventListener() {
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




                                Log.v("AVVISO", "STO PER AGGIUNGER ALL'ARRAYLIST RECYCLERVIEW");

                                fetchedPosts.add(postToBeAdded);


                            }


                            Collections.reverse(fetchedPosts);
                            Log.v("Attenzione", String.valueOf(fetchedPosts.size()));
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                            CommentAdapter commentAdapter = new CommentAdapter(fetchedPosts);
                            rvBacheca.setLayoutManager(mLayoutManager);
                            rvBacheca.setHasFixedSize(true);
                            rvBacheca.setAdapter(commentAdapter);


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
