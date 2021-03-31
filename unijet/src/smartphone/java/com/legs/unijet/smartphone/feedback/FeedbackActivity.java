package com.legs.unijet.smartphone.feedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.course.Course;
import com.legs.unijet.smartphone.utils.FeedbackUtils;

import java.util.ArrayList;

public class FeedbackActivity  extends AppCompatActivity implements FeedbackUtils.FinishCallback<Boolean>  {
    private ArrayList<FeedbackSample> fetchedPosts;

    FeedbackUtils postFetcher;
    Feedback feedback;
    Course course;
    String  courseUID;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView recyclerViewBacheca;
    TextView name;
    TextView averageNumber;

    ProgressDialog dialog;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.feedback_page);
        recyclerViewBacheca = findViewById(R.id.recyclerview_posts);
        name =findViewById(R.id.placeholder_text);
        averageNumber = findViewById(R.id.average_number);


        final Bundle args = getIntent().getExtras();
        name.setText(args.getString("Name"));
        String reference = args.getString("reference");
        final String UID = args.getString("key");

        database.child(reference).orderByChild("name").equalTo(args.getString("Name")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {



                    postFetcher = new FeedbackUtils(UID, recyclerViewBacheca, getApplicationContext(), "students");
                    postFetcher.run();

                }

                database.child("feedbacks").child(UID).addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        float total = 0;
                        float count = 0;
                        float average;
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            float rating = postSnapshot.child("rating").getValue(Float.class);
                            total += rating;
                            count++;
                        }
                        average = total / count;
                        averageNumber.setText(String.valueOf(average));
                    }

//Intent i = new Intent(FeedbackActivity.this, GroupActivity.class);
//i.putExtra("rating", average);


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        RelativeLayout postLayout = findViewById(R.id.area_post);
        postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (FeedbackActivity.this, NewFeedbackActivity.class);
                i.putExtra("key", args.getString("key"));
                startActivity(i);
            }
        });

        EditText postNow = findViewById(R.id.post_update_editText);
        postNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (FeedbackActivity.this, NewFeedbackActivity.class);
                i.putExtra("key", args.getString("key"));
                startActivity(i);
            }
        });
    }

    @Override
    public void onComplete(Boolean result) {
        dialog.dismiss();
        fetchedPosts = new ArrayList<>();
        fetchedPosts.addAll(postFetcher.getFetchedPosts());



    }

}
