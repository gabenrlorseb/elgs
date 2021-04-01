package com.legs.unijet.tabletversion.comment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.legs.unijet.tabletversion.utils.CommentUtils;

import java.util.ArrayList;

public class CommentActivity  extends AppCompatActivity implements CommentUtils.FinishCallback<Boolean>  {
    private ArrayList<com.legs.unijet.tabletversion.comment.CommentSample> fetchedPosts;

    CommentUtils postFetcher;
    RecyclerView recyclerViewBacheca;
    TextView name;
    ImageView image;
    TextView content;


    ProgressDialog dialog;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.comment_page);
        recyclerViewBacheca = findViewById(R.id.recyclerview_posts);
        name = findViewById(R.id.member_name);
        content = findViewById(R.id.post_text);
        image = findViewById(R.id.member_icon);

        Intent i = getIntent();
        Bitmap bitmap = (Bitmap) i.getParcelableExtra("authorImage");

        final Bundle args = getIntent().getExtras();



        name.setText(args.getString("author"));
        image.setImageBitmap(bitmap);
        content.setText(args.getString("postContent"));
        final String UID = args.getString("UID");
        String reference = "groups/" + UID + "/";
        String reference2 = "courses/" + UID + "/";
        String reference3 = "projects/" + UID + "/";

        database.child(reference).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {



                    postFetcher = new CommentUtils(args.getString("key"), recyclerViewBacheca, getApplicationContext(), "students");
                    postFetcher.run();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child(reference2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {



                    postFetcher = new CommentUtils(args.getString("key"), recyclerViewBacheca, getApplicationContext(), "students");
                    postFetcher.run();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child(reference3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {



                    postFetcher = new CommentUtils(args.getString("key"), recyclerViewBacheca, getApplicationContext(), "students");
                    postFetcher.run();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        RelativeLayout postLayout = findViewById(R.id.area_post);
        postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (CommentActivity.this, NewCommentActivity.class);
                i.putExtra("key", args.getString("key"));
                startActivity(i);
            }
        });

        EditText postNow = findViewById(R.id.post_update_editText);
        postNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (CommentActivity.this, NewCommentActivity.class);
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


