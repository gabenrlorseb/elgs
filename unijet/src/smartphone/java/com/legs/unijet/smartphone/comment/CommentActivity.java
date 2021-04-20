package com.legs.unijet.smartphone.comment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.group.Group;
import com.legs.unijet.smartphone.utils.CommentUtils;

import java.io.File;
import java.util.ArrayList;

public class CommentActivity  extends AppCompatActivity implements CommentUtils.FinishCallback<Boolean>  {

    CommentUtils postFetcher;
    RecyclerView recyclerViewBacheca;
    TextView name;
    ImageView image, authorImage;
    TextView content;
    FirebaseUser fbUser;
    RelativeLayout postLayout;
    EditText postNow;


    ProgressDialog dialog;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.comment_page);
        recyclerViewBacheca = findViewById(R.id.recyclerview_posts);
        name = findViewById(R.id.member_name);
        content = findViewById(R.id.post_text);
        authorImage = findViewById(R.id.member_icon);
        image = findViewById(R.id.member_icon_1);
        postLayout = findViewById(R.id.area_post);
        postNow = findViewById(R.id.post_update_editText);

        Intent i = getIntent();

        final Bundle args = getIntent().getExtras();

        File outputDir = getApplicationContext().getCacheDir();

        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent().hasExtra("authorBitmap")) {
            authorImage.setImageBitmap(BitmapFactory.decodeFile(args.getString("authorBitmap")));
        } else {
            authorImage.setImageResource(R.drawable.ic_generic_user_avatar);
        }

        final File localpropic = new File(outputDir, "propic" + fbUser.getUid()  +".bmp");
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(fbUser.getUid() + ".jpg");
        if (!localpropic.exists()) {
            fileRef.getFile(localpropic).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    image.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    image.setImageResource(R.drawable.ic_generic_user_avatar);
                }
            });
        } else {
            image.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
        }

        name.setText(args.getString("author"));

        content.setText(args.getString("postContent"));
        final String UID = args.getString("UID");
        String reference = "groups/" + UID + "/";
        String reference2 = "courses/" + UID + "/";
        String reference3 = "projects/" + UID + "/";

        database.child(reference).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
               Group group = snapshot.getValue(Group.class);

                    if (!fbUser.getEmail().equals(group.getAuthor()) && !group.getRecipients().contains(fbUser.getEmail()))
                    {
                        postLayout.setVisibility(View.GONE);
                        postNow.setVisibility(View.GONE);
                    }


                    postFetcher = new CommentUtils(args.getString("key"), recyclerViewBacheca, getApplicationContext());
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



                    postFetcher = new CommentUtils(args.getString("key"), recyclerViewBacheca, getApplicationContext());
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



                    postFetcher = new CommentUtils(args.getString("key"), recyclerViewBacheca, getApplicationContext());
                    postFetcher.run();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (CommentActivity.this, NewCommentActivity.class);
                i.putExtra("key", args.getString("key"));
                startActivity(i);
            }
        });


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
        ArrayList<CommentSample> fetchedPosts = new ArrayList<>();
        fetchedPosts.addAll(postFetcher.getFetchedPosts());



    }

}
