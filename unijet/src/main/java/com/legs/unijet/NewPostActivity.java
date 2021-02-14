package com.legs.unijet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class NewPostActivity extends AppCompatActivity {

    StorageReference storageReference;
    int postID;

    private static final int SELECT_PICTURE = 1;
    private static final int FILE_SELECT_CODE = 2;

    ArrayList<Bitmap> NumberOfImages;
    ArrayList<Uri> NumberOfDocuments;
    Post post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.new_post);
        final Bundle args = getIntent().getExtras();

        storageReference = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String reference = "posts/" + args.getString("key");
        final DatabaseReference database1 = FirebaseDatabase.getInstance().getReference(reference);
        final DatabaseReference database2 = FirebaseDatabase.getInstance().getReference(reference);

        final StorageReference fileDatabase1 = FirebaseStorage.getInstance().getReference(reference);


        final TextView postButton = findViewById(R.id.post_now_button);

        final LinearLayout attachImage = findViewById(R.id.attach_image);
        attachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        final LinearLayout attachDocument = findViewById(R.id.attach_document);
        attachDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] mimeTypes =
                        {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                "text/plain",
                                "application/pdf",
                                "application/zip"};

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }

                startActivityForResult(Intent.createChooser(intent,
                        "Select Document"), FILE_SELECT_CODE);
            }
        });

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
                //TODO: avviso tornare indietro
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database1.orderByChild("ID").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Post lastPost = snapshot.getValue(Post.class);
                            assert lastPost != null;
                            postID = lastPost.getID() + 1;
                        } else {
                            postID = 0;
                        }

                        String uniqueId = UUID.randomUUID().toString();

                        assert user != null;
                        post = new Post(postID, user.getEmail(), postContent.getText().toString(), NumberOfDocuments.size(), NumberOfImages.size(), false, 0, uniqueId);

                        for (int counter = 0; counter < NumberOfImages.size(); counter++) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            NumberOfImages.get(counter).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();
                            fileDatabase1.child("documents").putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(NewPostActivity.this, getString(R.string.propic_change_success), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(NewPostActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        for (int counter = 0; counter < NumberOfDocuments.size(); counter++) {
                            Uri file = NumberOfDocuments.get(counter);
                            fileDatabase1.child("pictures").putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(NewPostActivity.this, getString(R.string.propic_change_success), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(NewPostActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        database2.push().setValue(post, new DatabaseReference.CompletionListener()  {
                            @Override
                            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    System.out.println("Data could not be saved " + databaseError.getMessage());
                                    Toast.makeText (NewPostActivity.this, "ERROR", Toast.LENGTH_SHORT).show ();
                                } else {
                                    Toast.makeText (NewPostActivity.this, "success", Toast.LENGTH_SHORT).show ();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //TODO errore database (?)
                    }
                });


            }
        });





        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    if (intent != null) {
                        // Get the URI of the selected file
                        final Uri uri = intent.getData();
                        try {
                            useImage(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    if (intent != null) {
                        // Get the Uri of the selected file
                        Uri uri = intent.getData();
                        // Get the path
                        NumberOfDocuments.add(uri);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);

    }




    void useImage(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
    }



}
