package com.legs.unijet;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.firebase.storage.UploadTask;
import com.legs.unijet.groupDetailsActivity.GroupActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class CourseDetailsActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    final int PIC_CROP = 2;
    Course course;
    String userType, courseUID;
    Bitmap bitmap;
    Uri selectedImageUri;
    StorageReference storageReference;
    ImageView headerProPic;
    Boolean isAuthor;


    @Override
    protected void onCreate (Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.collapsing_toolbar_layout_sample);

        final Bundle args = getIntent().getExtras();

        final ImageView groupPic = findViewById(R.id.header);

        final int[] NumberOfMembers = new int[1];

        isAuthor = false;

        storageReference = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Log.v ("VALORE", args.getString("CName"));
        Log.v ("VALORE", valueOf(args.getString("CName")));
        database.child("courses").orderByChild("name").equalTo(args.getString("CName")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    course = postSnapshot.getValue(Course.class);
                    courseUID = snapshot.getKey();
                    ArrayList<String> addedMails = course.getMembers();
                    Log.v("VALORE CHILD", course.getName());
                    Log.v("VALORE CHILD", course.getEmail());
                    NumberOfMembers[0] = addedMails.size() + 1;

                    CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
                    collapsingToolbar.setTitle(course.getName());

                    TextView memberIndication = findViewById(R.id.toolbar_additional_infos);

                    memberIndication.setText(valueOf(NumberOfMembers[0]));

                    TextView toolBarShowEmail = findViewById(R.id.toolbar_subtitle);
                    toolBarShowEmail.setText(course.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ERRORE", "loadPost:onCancelled", error.toException());
            }
        });

        final StorageReference fileRef = storageReference.child(courseUID + ".jpg");

        File cachedProPic = getBaseContext().getFilesDir();
        final File f = new File(cachedProPic, courseUID + ".jpg");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fis != null) {
            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (!netInfo.isConnected()) {
                Log.v("AVVISO", "File has been found in cache");
                fileRef.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.v("AVVISO", "Il file è stato scaricato dal database");
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                        FileOutputStream fos;
                        try {
                            fos = new FileOutputStream(f);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("AVVISO", "File could not be fetched from database");
                        Toast.makeText(CourseDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Log.v("AVVISO", "File has been found in cache and internet is not available");
                bitmap = BitmapFactory.decodeStream(fis);
                groupPic.setImageBitmap(bitmap);
            }
        }






        FloatingActionButton setProPicFab = findViewById(R.id.common_fab);
        setProPicFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                selectedImageUri = data.getData();
                performCrop(selectedImageUri);
            }

            if (requestCode == PIC_CROP) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap bp = extras.getParcelable("data");
                    updateGroupProPic(bp);
                }
            }
        }//end of outer if
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 480);
            cropIntent.putExtra("outputY", 480);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException e) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void updateGroupProPic(final Bitmap bitmap) {
        //Upload su firebase storage
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("groups");
        DatabaseReference userRef = ref.child(user.getUid());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference fileRef = storageReference.child(userRef + ".jpg");
        fileRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CourseDetailsActivity.this, getString(R.string.propic_change_success), Toast.LENGTH_SHORT).show();
                headerProPic = findViewById(R.id.header);
                headerProPic.setImageBitmap(bitmap);
                final File f = new File(getBaseContext().getFilesDir(), "profile-pic.jpg");
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CourseDetailsActivity.this, getString(R.string.error_profile_picture), Toast.LENGTH_SHORT).show();
            }
        });

    }
}


