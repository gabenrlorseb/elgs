package com.legs.unijet.smartphone.project;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import com.legs.unijet.smartphone.group.Group;
import com.legs.unijet.smartphone.post.NewPostActivity;
import com.legs.unijet.smartphone.post.PostAdapter;
import com.legs.unijet.smartphone.post.PostSample;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.groupDetailsActivity.MembersDetailsActivity;

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
import com.legs.unijet.smartphone.profile.User;
import com.legs.unijet.smartphone.utils.BachecaUtils;
import com.legs.unijet.smartphone.utils.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectDetailsActivity extends AppCompatActivity implements BachecaUtils.FinishCallback<Boolean>{

    private static final int SELECT_PICTURE = 1;
    final int PIC_CROP = 2;
    Project project;
    String projectUID;
    String reference = "projects";
    String groupUID;
    Bitmap bitmap;
    Uri selectedImageUri;
    StorageReference storageReference;
    ImageView headerProPic;
    Boolean isAuthor;
    RecyclerView recyclerViewBacheca;
    TextView rating;
    private PostAdapter postAdapter;

    BachecaUtils postFetcher;


    ProgressDialog dialog;


    @Override
    protected void onCreate (Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.collapsing_toolbar_layout_sample);
        recyclerViewBacheca = findViewById(R.id.recyclerview_posts);
        rating = findViewById(R.id.toolbar_additional_infos);

        final Bundle args = getIntent().getExtras();

        final ImageView groupPic = findViewById(R.id.header);

        final int[] NumberOfMembers = new int[1];
        headerProPic = findViewById(R.id.header);


        final FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final ImageView profileAvatar = findViewById(R.id.member_icon);


        final File localpropic = new File(getCacheDir(), "propic" + CurrentUser.getUid() +".bmp");
        StorageReference fileRef1 = FirebaseStorage.getInstance().getReference().child(CurrentUser.getUid() + ".jpg");
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

        final File projectProPic = new File(getCacheDir(), "propic" + projectUID +".bmp");
        StorageReference fileRef2 = FirebaseStorage.getInstance().getReference().child(projectUID + ".jpg");
        if (!projectProPic.exists()) {
            fileRef2.getFile(projectProPic).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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
            headerProPic.setImageBitmap(BitmapFactory.decodeFile(projectProPic.getAbsolutePath()));
        }

        isAuthor = false;

        storageReference = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();


        database.child(reference).orderByChild("name").equalTo(args.getString("PName")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
                    project = postSnapshot.getValue(Project.class);
                    projectUID = postSnapshot.getKey();
                    postFetcher = new BachecaUtils(projectUID, recyclerViewBacheca, getApplicationContext());
                    postFetcher.run();
                    String group = project.getGroup();



                    final String[] groupName = new String[1];

                    DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();
                    database2.child("groups").orderByChild("name").equalTo(project.getGroup()).addValueEventListener (new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {

                            for (DataSnapshot childSnapshot:snapshot2.getChildren()) {
                                final Group group;
                                group = childSnapshot.getValue(Group.class);
                                if (user.getEmail().equals(group.getAuthor())) {
                                    isAuthor = true;
                                }
                                groupUID = snapshot2.getKey();

                                groupName[0] = group.getName();
                                final String[] groupAuthorName = new String[1];
                                DatabaseReference database3 = FirebaseDatabase.getInstance().getReference();
                                database3.child("students").orderByChild("email").equalTo(group.getAuthor()).addValueEventListener (new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        for (DataSnapshot childSnapshot:snapshot2.getChildren()) {
                                            User user;
                                            user = childSnapshot.getValue(User.class);
                                            groupAuthorName[0] = user.getName() + " " + user.getSurname();
                                            TextView groupIndication = findViewById(R.id.toolbar_subtitle);
                                            groupIndication.setText(groupName[0]);
                                            groupIndication.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Bundle b = new Bundle();
                                                    b.putSerializable("groupRecipients", group.getRecipients());

                                                    Intent intent = new Intent(ProjectDetailsActivity.this, MembersDetailsActivity.class);
                                                    intent.putExtras(b);
                                                    if (!isAuthor) {
                                                        intent.putExtra("author", group.getAuthor());
                                                        intent.putExtra("author_name", groupAuthorName[0]);
                                                    } else {
                                                        intent.putExtra("author", getString(R.string.you));
                                                        intent.putExtra("author_name", "you");
                                                    }
                                                    intent.putExtra("name", group.getName());
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
                    collapsingToolbar.setTitle(project.getName());
                    final DatabaseReference database3 = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference database4 = FirebaseDatabase.getInstance().getReference();
                    database3.child(reference).orderByChild("name").equalTo(args.getString("PName")).addListenerForSingleValueEvent(new ValueEventListener() {
                        final FloatingActionButton fab = findViewById(R.id.common_fab);
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
                                project = postSnapshot.getValue(Project.class);
                                projectUID = postSnapshot.getKey();
                                database4.child("groups").orderByChild("name").equalTo(project.getGroup()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        for (final DataSnapshot postSnapshot : snapshot2.getChildren()) {
                                            Group group;
                                            group = postSnapshot.getValue(Group.class);
                                            ArrayList<String> groupMembers = group.getRecipients();
                                            if (groupMembers.contains(user.getEmail()) || user.getEmail().equals(group.getAuthor())){
                                                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_settings);
                                                fab.setImageDrawable(myDrawable);
                                                fab.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final PopupMenu authorMenu;
                                                        authorMenu = new PopupMenu(ProjectDetailsActivity.this, fab);
                                                        MenuInflater inflater = authorMenu.getMenuInflater();
                                                        inflater.inflate(R.menu.project_author_menu, authorMenu.getMenu());
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
                                                                    case R.id.remove_project_tab:
                                                                        Intent intent3 = new Intent(ProjectDetailsActivity.this, MainActivity.class);
                                                                        //String courseUID = postSnapshot.getKey();
                                                                        database3.child("projects").child(projectUID).removeValue();
                                                                        startActivity(intent3);
                                                                    default:
                                                                        return false;
                                                                }
                                                            }
                                                        });
                                                        authorMenu.show();
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            database.child("feedbacks").child(projectUID).addValueEventListener(new ValueEventListener() {


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




                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ERRORE", "loadPost:onCancelled", error.toException());
            }
        });

        RelativeLayout postLayout = findViewById(R.id.area_post);
        postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (ProjectDetailsActivity.this, NewPostActivity.class);
                i.putExtra("key", projectUID);
                startActivity(i);
            }
        });

        EditText postNow = findViewById(R.id.post_update_editText);
        postNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (ProjectDetailsActivity.this, NewPostActivity.class);
                i.putExtra("key", projectUID);
                startActivity(i);
            }
        });
        TextView rating = findViewById(R.id.toolbar_additional_infos);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (ProjectDetailsActivity.this, FeedbackActivity.class);
                i.putExtra("key", projectUID);
                i.putExtra("reference",reference);
                i.putExtra("Name", args.getString("PName"));
                startActivity(i);
            }
        });

        final StorageReference fileRef = storageReference.child(projectUID + ".jpg");

        File cachedProPic = getBaseContext().getFilesDir();
        final File f = new File(cachedProPic, projectUID + ".jpg");
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
                        Toast.makeText(ProjectDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                bitmap = BitmapFactory.decodeStream(fis);
                groupPic.setImageBitmap(bitmap);
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
        DatabaseReference ref = database.getReference("groups");
        DatabaseReference userRef = ref.child(user.getUid());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference fileRef = storageReference.child(userRef + ".jpg");
        fileRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProjectDetailsActivity.this, getString(R.string.propic_change_success), Toast.LENGTH_SHORT).show();
                headerProPic = findViewById(R.id.header);
                headerProPic.setImageBitmap(bitmap);
                final File f = new File(getBaseContext().getFilesDir(), projectUID + "jpg");
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
                Toast.makeText(ProjectDetailsActivity.this, getString(R.string.error_profile_picture), Toast.LENGTH_SHORT).show();
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

