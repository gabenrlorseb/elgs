package com.legs.unijet.smartphone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewPostActivity extends AppCompatActivity {

    StorageReference storageReference;
    int postID;

    private static final int SELECT_PICTURE = 1;
    private static final int FILE_SELECT_CODE = 2;

    ArrayList<Bitmap> Images = new ArrayList<>();
    ArrayList<Uri> Documents = new ArrayList<>();
    Post post;

    LinearLayout addedImagesThumbnails;
    LinearLayout addedDocumentsName;

    TextView indicationAttachedImages;
    TextView indicationDocumentsAttached;


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
        final StorageReference fileDatabase2 = FirebaseStorage.getInstance().getReference(reference);


        final TextView postButton = findViewById(R.id.post_now_button);
        indicationAttachedImages = findViewById(R.id.added_images_indication);
        indicationDocumentsAttached = findViewById(R.id.attached_documents_indication);

        addedImagesThumbnails = findViewById(R.id.added_images_thumbnails);
        addedDocumentsName = findViewById(R.id.added_documents_preview);

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
                AlertDialog alertDialog = new AlertDialog.Builder(NewPostActivity.this).create();
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

                        int numberOfDocuments = 0;
                        int numberOfImages = 0;

                        if (!Documents.isEmpty()) {
                            numberOfDocuments = Documents.size();
                            for (int counter = 0; counter < Documents.size(); counter++) {
                                Uri file = Documents.get(counter);
                                fileDatabase1.child(uniqueId + "/document" + counter).putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NewPostActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }

                        if (!Images.isEmpty()) {
                            numberOfImages = Images.size();
                            for (int counter2 = 0; counter2 < Images.size(); counter2++) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                Images.get(counter2).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                fileDatabase2.child(uniqueId + "/pic" + counter2).putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(NewPostActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }

                        long ut2 = System.currentTimeMillis() / 1000L;

                        post = new Post(postID, user.getEmail(), false, numberOfDocuments, numberOfImages, new ArrayList<String>(), ut2, uniqueId, uniqueId,postContent.getText().toString());

                        database2.push().setValue(post, new DatabaseReference.CompletionListener()  {
                            @Override
                            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    System.out.println("Data could not be saved " + databaseError.getMessage());
                                    Toast.makeText (NewPostActivity.this, "ERROR", Toast.LENGTH_SHORT).show ();
                                } else {
                                    Toast.makeText (NewPostActivity.this, "Success", Toast.LENGTH_SHORT).show ();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText (NewPostActivity.this, getString(R.string.error_profile_picture), Toast.LENGTH_SHORT).show ();
                    }
                });
                
                finish();
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
                break;
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    if (intent != null) {
                        // Get the Uri of the selected file
                        Uri uri = intent.getData();
                        // Get the path
                        Documents.add(uri);

                        if (indicationDocumentsAttached.getVisibility() == View.GONE) {
                            indicationDocumentsAttached.setVisibility(View.VISIBLE);
                        }
                        indicationDocumentsAttached.setText(getResources().getQuantityString(R.plurals.d_document_added, Documents.size(), Documents.size()));

                        indicationDocumentsAttached.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (addedDocumentsName.getVisibility() == View.GONE) {
                                    addedDocumentsName.setVisibility(View.VISIBLE);
                                } else {
                                    addedDocumentsName.setVisibility(View.GONE);
                                }
                            }
                        });

                        LinearLayout.LayoutParams imParams =
                                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                        imParams.setMarginEnd(9);
                        TextView newDocument = new TextView(getApplicationContext());
                        newDocument.setTextSize((float) 15.0);
                        String result = uri.getPath();
                        int cut = result.lastIndexOf('/');
                        if (cut != -1) {
                            result = result.substring(cut + 1);
                        }
                        newDocument.setText(result);
                        addedDocumentsName.addView(newDocument,imParams);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);

    }




    void useImage(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        Images.add(bitmap);

        if (indicationAttachedImages.getVisibility() == View.GONE) {
            indicationAttachedImages.setVisibility(View.VISIBLE);
        }

        indicationAttachedImages.setText(getResources().getQuantityString(R.plurals.d_images_added, Images.size(), Images.size()));

        indicationAttachedImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addedImagesThumbnails.getVisibility() == View.GONE) {
                    addedImagesThumbnails.setVisibility(View.VISIBLE);
                } else {
                    addedImagesThumbnails.setVisibility(View.GONE);
                }
            }
        });

        LinearLayout.LayoutParams imParams =
                new LinearLayout.LayoutParams(300, 300, 1);
        imParams.setMarginEnd(9);
        ImageView newImage = new ImageView(getApplicationContext());
        newImage.setImageBitmap(bitmap);

        addedImagesThumbnails.addView(newImage,imParams);

    }



}
