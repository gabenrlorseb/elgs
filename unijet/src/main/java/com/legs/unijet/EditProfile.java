package com.legs.unijet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.legs.unijet.utils.Utils;

public class EditProfile extends AppCompatActivity {

    private String selectedImagePath, filemanagerstring;
    private static final int SELECT_PICTURE = 1;


    @Override
    protected void onCreate (Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.edit_profile_layout);
        Bundle args = getIntent().getExtras();

        String personJsonString = args.getString("PERSON_KEY");
        User person= Utils.getGsonParser().fromJson(personJsonString, User.class);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(person.getName() + " " + person.getSurname());

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String imagePath = getRealPathFromURI(selectedImageUri);
                //Now you have imagePath do whatever you want to do now
            }//end of inner if
        }//end of outer if
    }

    public String getRealPathFromURI(Uri contentUri) {
        //Uri contentUri = Uri.parse(contentURI);

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = null;
        try {
            // Will return "image:x*"
            String wholeID = DocumentsContract.getDocumentId(contentUri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            cursor = getBaseContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, sel, new String[] { id }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String path = null;
        try {
            int column_index = cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index).toString();
            cursor.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return path;
    }

}
