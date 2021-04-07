package com.legs.unijet.smartphone.profile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.legs.unijet.smartphone.LoginActivity;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.registerActivity.RegisterActivityStart;
import com.legs.unijet.smartphone.utils.GsonParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    final int PIC_CROP = 2;
    User person;
    String userType;
    Bitmap bitmap;
    Uri selectedImageUri;
    ImageView headerProPic;
    StorageReference storageReference;
    DatabaseReference ref;

    @Override
    protected void onCreate (Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.edit_profile_layout);

        Bundle args = getIntent().getExtras();

        String personJsonString = args.getString("PERSON_KEY");
        person = GsonParser.getGsonParser().fromJson(personJsonString, User.class);

        headerProPic = findViewById(R.id.header);
        storageReference = FirebaseStorage.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        final File localpropic = new File(getApplicationContext().getCacheDir(), "propic" + user.getUid() +".bmp");
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(user.getUid() + ".jpg");
        if (!localpropic.exists()) {
            fileRef.getFile(localpropic).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    headerProPic.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    headerProPic.setImageResource(R.drawable.ic_generic_user_avatar);
                }
            });
        } else {
            headerProPic.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
        }


        userType = args.getString("PERSON_TYPE");

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(person.getName() + " " + person.getSurname());

        TextView memberIndication = findViewById(R.id.toolbar_additional_infos);
        memberIndication.setText(userType.toUpperCase());


        TextView departmentIndication = findViewById(R.id.department_indication);
        departmentIndication.setText(person.getDepartment());

        TextView emailIndication = findViewById(R.id.email_indication);
        TextView toolBarShowEmail = findViewById(R.id.toolbar_subtitle);
        emailIndication.setText(person.getEmail());
        toolBarShowEmail.setText(person.getEmail());


        FloatingActionButton setProPicFab = findViewById(R.id.edit_profile_picture_fab);
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

        LinearLayout changePassowrd = findViewById(R.id.third_option);
        changePassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity (new Intent (EditProfile.this, ChangePasswordActivity.class));
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
                    updateUserPropic(bp);
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

    private void updateUserPropic(final Bitmap bitmap) {
        //Upload su firebase storage
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(user.getUid() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        fileRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditProfile.this, getString(R.string.propic_change_success), Toast.LENGTH_SHORT).show();
                headerProPic = findViewById(R.id.header);
                headerProPic.setImageBitmap(bitmap);
                final File f = new File(getApplicationContext().getCacheDir(), "propic" + user.getUid() +".bmp");
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, getString(R.string.error_profile_picture), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
