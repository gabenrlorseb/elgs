package com.legs.unijet.smartphone.feedback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.legs.unijet.smartphone.R;

import java.util.ArrayList;
import java.util.UUID;

public class NewFeedbackActivity extends AppCompatActivity {

    StorageReference storageReference;
    int postID;

    private static final int SELECT_PICTURE = 1;
    private static final int FILE_SELECT_CODE = 2;

   Feedback feedback;

    LinearLayout addedImagesThumbnails;
    LinearLayout addedDocumentsName;

    TextView indicationAttachedImages;
    TextView indicationDocumentsAttached;
    RatingBar rating;
    boolean successful = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.new_feedback);
        final Bundle args = getIntent().getExtras();

        storageReference = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String reference = "feedbacks/" + args.getString("key") + "/";
        final DatabaseReference database2 = FirebaseDatabase.getInstance().getReference(reference);

        final StorageReference fileDatabase1 = FirebaseStorage.getInstance().getReference(reference);
        final StorageReference fileDatabase2 = FirebaseStorage.getInstance().getReference(reference);


        final TextView postButton = findViewById(R.id.post_now_button);
        indicationAttachedImages = findViewById(R.id.added_images_indication);
        indicationDocumentsAttached = findViewById(R.id.attached_documents_indication);

        addedImagesThumbnails = findViewById(R.id.added_images_thumbnails);
        addedDocumentsName = findViewById(R.id.added_documents_preview);
        rating = findViewById(R.id.rating);




        final EditText postContent = findViewById(R.id.post_content);
        postContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    postButton.setVisibility(View.GONE);
                } else if (s.length() == 1) {
                    postButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(NewFeedbackActivity.this).create();
                alertDialog.setTitle(getString(R.string.back_warning));
                alertDialog.setMessage(getString(R.string.warning_back_content));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alertDialog.show();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successful = false;


                final String uniqueId = UUID.randomUUID().toString();

                assert user != null;

                int numberOfDocuments = 0;
                int numberOfImages = 0;



                long ut2 = System.currentTimeMillis() / 1000L;

                feedback = new Feedback(user.getEmail(), false,  new ArrayList<String>(), ut2, uniqueId, uniqueId,postContent.getText().toString(), rating.getRating());
                final DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("likes/");


                database2.push().setValue(feedback, new DatabaseReference.CompletionListener()  {
                    @Override
                    public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            System.out.println("Data could not be saved " + databaseError.getMessage());
                            Toast.makeText (NewFeedbackActivity.this, "ERROR", Toast.LENGTH_SHORT).show ();
                        } else {
                            ArrayList<String> setPreLikes = new ArrayList<>();
                            setPreLikes.add(user.getEmail());
                            database1.child(uniqueId).setValue(setPreLikes);
                            Toast.makeText (NewFeedbackActivity.this, "Success", Toast.LENGTH_SHORT).show ();
                            finish();
                        }
                    }
                });

            }



        });





        super.onCreate(savedInstanceState);
    }
}
