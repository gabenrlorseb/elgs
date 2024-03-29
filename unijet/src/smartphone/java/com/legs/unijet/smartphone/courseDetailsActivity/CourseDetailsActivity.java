package com.legs.unijet.smartphone.courseDetailsActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legs.unijet.smartphone.feedback.FeedbackActivity;
import com.legs.unijet.smartphone.post.NewPostActivity;
import com.legs.unijet.smartphone.post.PostAdapter;
import com.legs.unijet.smartphone.post.PostSample;
import com.legs.unijet.smartphone.utils.MainActivity;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.profile.User;
import com.legs.unijet.smartphone.course.Course;
import com.legs.unijet.smartphone.groupDetailsActivity.MembersDetailsActivity;
import com.legs.unijet.smartphone.utils.BachecaUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class  CourseDetailsActivity extends AppCompatActivity implements BachecaUtils.FinishCallback<Boolean>{

    private static final int SELECT_PICTURE = 1;
    final int PIC_CROP = 2;
    Course course;
    String courseUID;
    String reference = "courses";
    Bitmap bitmap;
    Uri selectedImageUri;
    StorageReference storageReference;
    ImageView headerProPic;
    Boolean isAuthor;
    RecyclerView recyclerViewBacheca;

    BachecaUtils postFetcher;
    ProgressDialog dialog;
    TextView rating;
    RelativeLayout postLayout;
    EditText postNow;





    @Override
    protected void onCreate (Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.collapsing_toolbar_layout_sample);
        recyclerViewBacheca = findViewById(R.id.recyclerview_posts);
        rating = findViewById(R.id.toolbar_additional_infos);
        postLayout = findViewById(R.id.area_post);
        postNow = findViewById(R.id.post_update_editText);


        final Bundle args = getIntent().getExtras();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final ImageView headerProPic = findViewById(R.id.header);

        final ImageView profileAvatar = findViewById(R.id.member_icon);


        final File localpropic = new File(getCacheDir(), "propic" + user.getUid() +".bmp");
        StorageReference fileRef1 = FirebaseStorage.getInstance().getReference().child(user.getUid() + ".jpg");
        if (!localpropic.exists()) {
            fileRef1.getFile(localpropic).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    profileAvatar.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    profileAvatar.setImageResource(R.drawable.ic_generic_user_avatar);
                }
            });
        } else {
            profileAvatar.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
        }

        final File courseProPic = new File(getCacheDir(), "propic" + courseUID +".bmp");
        StorageReference fileRef2 = FirebaseStorage.getInstance().getReference().child(courseUID + ".jpg");
        if (!courseProPic.exists()) {
            fileRef2.getFile(courseProPic).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    headerProPic.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    headerProPic.setImageResource(R.drawable.ic_generic_group_avatar);
                }
            });
        } else {
            headerProPic.setImageBitmap(BitmapFactory.decodeFile(courseProPic.getAbsolutePath()));
        }





        final int[] NumberOfMembers = new int[1];

        isAuthor = false;

        storageReference = FirebaseStorage.getInstance().getReference();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();


        database.child(reference).orderByChild("name").equalTo(args.getString("titleName")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
                    course = postSnapshot.getValue(Course.class);
                    if (user.getEmail().equals(course.getEmail())) {
                        isAuthor = true;
                    }
                    else{
                     postLayout.setVisibility(View.GONE);
                     postNow.setVisibility(View.GONE);
                    }
                    courseUID = postSnapshot.getKey();
                    postFetcher = new BachecaUtils(courseUID, recyclerViewBacheca, getApplicationContext());
                    postFetcher.run();
                    ArrayList<String> addedMails = course.getMembers();

                    NumberOfMembers[0] = addedMails.size();




                    final String[] courseAuthorName = new String[1];
                    DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();
                    database2.child("teachers").orderByChild("email").equalTo(course.getEmail()).addValueEventListener (new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {

                            for (DataSnapshot childSnapshot:snapshot2.getChildren()) {

                                final User userd;
                                userd = childSnapshot.getValue(User.class);
                                courseAuthorName[0] = userd.getName() + " " + userd.getSurname();
                                TextView memberIndication = findViewById(R.id.toolbar_subtitle);
                                memberIndication.setText(getResources().getQuantityString(R.plurals.members, NumberOfMembers[0], NumberOfMembers[0]));
                                memberIndication.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle b = new Bundle();
                                        b.putSerializable("groupRecipients", course.getMembers());

                                        Intent intent = new Intent(CourseDetailsActivity.this, MembersDetailsActivity.class);
                                        intent.putExtras(b);
                                        if (!isAuthor) {
                                            intent.putExtra("author", course.getEmail());
                                            intent.putExtra("author_name", courseAuthorName[0]);
                                        } else {
                                            intent.putExtra("author", getString(R.string.you));
                                            intent.putExtra("author_name", "you");
                                        }
                                        intent.putExtra("name", course.getName());
                                        startActivity(intent);




                                    }


                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
                    collapsingToolbar.setTitle(course.getName());

                    final DatabaseReference database3 = FirebaseDatabase.getInstance().getReference();
                    database3.child(reference).orderByChild("name").equalTo(args.getString("titleName")).addListenerForSingleValueEvent(new ValueEventListener() {


                    final FloatingActionButton fab = findViewById(R.id.common_fab);
                        public void onDataChange(@NonNull final DataSnapshot snapshot) {
                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
                                course = postSnapshot.getValue(Course.class);
                                if (user.getEmail().equals(course.getEmail())) {
                                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_settings);
                                    fab.setImageDrawable(myDrawable);
                                    fab.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final PopupMenu authorMenu;
                                            authorMenu = new PopupMenu(CourseDetailsActivity.this, fab);
                                            MenuInflater inflater = authorMenu.getMenuInflater();
                                            inflater.inflate(R.menu.course_author_menu, authorMenu.getMenu());
                                            authorMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch (item.getItemId()) {
                                                        case R.id.change_pic_tab:
                                                            Intent intent = new Intent();
                                                            intent.setType("image/*");
                                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                                            startActivityForResult(Intent.createChooser(intent,
                                                                    "Select Picture"), SELECT_PICTURE);
                                                            return true;
                                                        case R.id.members_tab:
                                                            Intent intent2 = new Intent(CourseDetailsActivity.this, AuthorCourseManageActivity.class);
                                                            Bundle b = new Bundle();
                                                            b.putSerializable("groupRecipients", course.getMembers());
                                                            intent2.putExtras(b);
                                                            intent2.putExtra("name", course.getName());
                                                            startActivity(intent2);
                                                            return true;
                                                        case R.id.remove_course_tab:
                                                            Intent intent3 = new Intent(CourseDetailsActivity.this, MainActivity.class);
                                                            String courseUID = postSnapshot.getKey();
                                                            database3.child("courses").child(courseUID).removeValue();
                                                            startActivity(intent3);
                                                        default:
                                                            return false;
                                                    }

                                                }
                                            });
                                            authorMenu.show();

                                            }





                                    });


                                } else {
                                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_add_24);
                                    fab.setImageDrawable(myDrawable);
                                    fab.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            PopupMenu studentMenu;


                                            studentMenu = new PopupMenu(CourseDetailsActivity.this, fab);
                                            MenuInflater inflater = studentMenu.getMenuInflater();
                                            inflater.inflate(R.menu.course_student_menu, studentMenu.getMenu());
                                            studentMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                            public boolean onMenuItemClick(MenuItem item) {
                                                switch (item.getItemId()) {
                                                    case R.id.student_tab:
                                                        String courseUID = postSnapshot.getKey();
                                                        ArrayList<String> courseSubscribers = course.getMembers();
                                                        if(courseSubscribers.contains(user.getEmail())){
                                                            courseSubscribers.remove(user.getEmail());
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetailsActivity.this);
                                                            builder.setMessage(R.string.course_elimination)
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            //do things
                                                                        }
                                                                    });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();
                                                        } else {
                                                            courseSubscribers.add(user.getEmail());
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetailsActivity.this);
                                                            builder.setMessage(R.string.course_registration)
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            //do things
                                                                        }
                                                                    });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();
                                                        }
                                                        database3.child("courses").child(courseUID).child("members").setValue(courseSubscribers);
                                                        return true;
                                                    default:
                                                        return false;
                                                }

                                            }
                                        });

                                            studentMenu.show();
                                        }


                                    });
                                }
                            }
                            database.child("feedbacks").child(courseUID).addValueEventListener(new ValueEventListener() {


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
                                    if (total == 0 && count == 0){
                                        average = 0;
                                    }
                                    rating.setText(String.valueOf(average));
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



/*
                    TextView toolBarShowEmail = findViewById(R.id.toolbar_additional_infos);
                    toolBarShowEmail.setText(group.getAuthor());*/

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ERRORE", "loadPost:onCancelled", error.toException());
            }
        });



        postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (CourseDetailsActivity.this, NewPostActivity.class);
                i.putExtra("key", courseUID);
                startActivity(i);
            }
        });


        postNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (CourseDetailsActivity.this, NewPostActivity.class);
                i.putExtra("key", courseUID);
                startActivity(i);
            }
        });



        TextView rating = findViewById(R.id.toolbar_additional_infos);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (CourseDetailsActivity.this, FeedbackActivity.class);
                i.putExtra("key", courseUID);
                i.putExtra("reference",reference);
                i.putExtra("Name", args.getString("titleName"));
                startActivity(i);
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
                fileRef.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
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
                        Toast.makeText(CourseDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                bitmap = BitmapFactory.decodeStream(fis);
                headerProPic.setImageBitmap(bitmap);
            }




        }






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
        DatabaseReference ref = database.getReference("courses");
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
                final File f = new File(getBaseContext().getFilesDir(), courseUID + "jpg");
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

    @Override
    public void onComplete() {
        dialog.dismiss();
        ArrayList<PostSample> fetchedPosts = new ArrayList<>();
        fetchedPosts.addAll(postFetcher.getFetchedPosts());



    }

}
