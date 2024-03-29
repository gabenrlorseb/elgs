 package com.legs.unijet.smartphone.groupDetailsActivity;

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
 import com.legs.unijet.smartphone.R;
 import com.legs.unijet.smartphone.feedback.FeedbackActivity;
 import com.legs.unijet.smartphone.group.Group;
 import com.legs.unijet.smartphone.post.NewPostActivity;
 import com.legs.unijet.smartphone.post.PostAdapter;
 import com.legs.unijet.smartphone.post.PostSample;
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

 public class  GroupActivity extends AppCompatActivity implements BachecaUtils.FinishCallback<Boolean> {

    private static final int SELECT_PICTURE = 1;
    final int PIC_CROP = 2;
    Group group;
    String groupUID;
    String usertype;
     String reference = "groups";
    Bitmap bitmap;
    Uri selectedImageUri;
    StorageReference storageReference;
    ImageView headerProPic;
    Boolean isAuthor;
     RecyclerView recyclerViewBacheca;
     TextView rating;
     RelativeLayout postLayout;
     EditText postNow;

     private PostAdapter postAdapter;

     BachecaUtils postFetcher;

     ProgressDialog dialog;

    @Override
    protected void onCreate (Bundle savedInstance) {


        super.onCreate(savedInstance);
        setContentView(R.layout.collapsing_toolbar_layout_sample);

        recyclerViewBacheca = findViewById(R.id.recyclerview_posts);
rating = findViewById(R.id.toolbar_additional_infos);
        postLayout = findViewById(R.id.area_post);
        postNow = findViewById(R.id.post_update_editText);


        final Bundle args = getIntent().getExtras();

        rating.setText(String.valueOf(args.getFloat("rating")));

        final ImageView groupPic = findViewById(R.id.header);

        final int[] NumberOfMembers = new int[1];

        isAuthor = false;

        storageReference = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference database3 = FirebaseDatabase.getInstance().getReference("students");
        final DatabaseReference database4 = FirebaseDatabase.getInstance().getReference();

        final StorageReference reference1 = FirebaseStorage.getInstance().getReference("posts");

        final ImageView profileAvatar = findViewById(R.id.member_icon);

        headerProPic = findViewById(R.id.header);


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

        final File groupProPic = new File(getCacheDir(), "propic" + groupUID +".bmp");
        StorageReference fileRef2 = FirebaseStorage.getInstance().getReference().child(groupUID + ".jpg");
        if (!groupProPic.exists()) {
            fileRef2.getFile(groupProPic).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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
            headerProPic.setImageBitmap(BitmapFactory.decodeFile(groupProPic.getAbsolutePath()));
        }





        database.child(reference).orderByChild("name").equalTo(args.getString("titleName")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {

                    group = postSnapshot.getValue(Group.class);

                    if (user.getEmail().equals(group.getAuthor())) {
                        isAuthor = true;
                    }

                    if (!user.getEmail().equals(group.getAuthor()) && !group.getRecipients().contains(user.getEmail()))
                    {
                        postLayout.setVisibility(View.GONE);
                        postNow.setVisibility(View.GONE);
                    }
                    groupUID = postSnapshot.getKey();

                    postFetcher = new BachecaUtils(groupUID, recyclerViewBacheca, getApplicationContext());
                    postFetcher.run();

                    if (user.getEmail().contains("@studenti.uniba.it")) {
                        usertype = "students";
                    } else {
                        usertype = "teachers";
                    }




                    ArrayList<String> addedMails = group.getRecipients();
                    NumberOfMembers[0] = addedMails.size() + 1;
                    final String[] groupAuthorName = new String[1];




                    database2.child(usertype).orderByChild("email").equalTo(group.getAuthor()).addValueEventListener (new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            for (DataSnapshot childSnapshot:snapshot2.getChildren()) {
                                    User user;
                                    user = childSnapshot.getValue(User.class);
                                    groupAuthorName[0] = user.getName() + " " + user.getSurname();
                                TextView memberIndication = findViewById(R.id.toolbar_subtitle);
                                memberIndication.setText(getResources().getQuantityString(R.plurals.members, NumberOfMembers[0], NumberOfMembers[0]));
                                memberIndication.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle b = new Bundle();
                                        b.putSerializable("groupRecipients", group.getRecipients());

                                        Intent intent = new Intent(GroupActivity.this, MembersDetailsActivity.class);
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





                    CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
                    collapsingToolbar.setTitle(group.getName());


                    final DatabaseReference database3 = FirebaseDatabase.getInstance().getReference();
                    database3.child(reference).orderByChild("name").equalTo(args.getString("titleName")).addListenerForSingleValueEvent(new ValueEventListener() {


                        final FloatingActionButton fab = findViewById(R.id.common_fab);
                        public void onDataChange(@NonNull final DataSnapshot snapshot) {
                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {

                                group = postSnapshot.getValue(Group.class);
                                if (user.getEmail().equals(group.getAuthor())) {
                                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_settings);
                                    fab.setImageDrawable(myDrawable);
                                    fab.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final PopupMenu authorMenu;
                                            authorMenu = new PopupMenu(GroupActivity.this, fab);
                                            MenuInflater inflater = authorMenu.getMenuInflater();
                                            inflater.inflate(R.menu.group_author_menu, authorMenu.getMenu());
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
                                                            Intent intent2 = new Intent(GroupActivity.this, AuthorGroupManageActivity.class);
                                                            Bundle b = new Bundle();
                                                            b.putSerializable("groupRecipients", group.getRecipients());
                                                            intent2.putExtras(b);
                                                            intent2.putExtra("name", group.getName());
                                                            startActivity(intent2);
                                                            return true;
                                                        case R.id.remove_group_tab:
                                                            Intent intent3 = new Intent(GroupActivity.this, MainActivity.class);
                                                            String groupUID = postSnapshot.getKey();
                                                            database3.child("groups").child(groupUID).removeValue();
                                                            startActivity(intent3);

                                                            return true;
                                                        default:
                                                            return false;
                                                    }

                                                }
                                            });
                                            authorMenu.show();

                                        }

                                    });


                                } else if (group.getRecipients().contains(user.getEmail())) {
                                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_settings);
                                    fab.setImageDrawable(myDrawable);
                                    fab.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final PopupMenu componentMenu;
                                            componentMenu = new PopupMenu(GroupActivity.this, fab);
                                            MenuInflater inflater = componentMenu.getMenuInflater();
                                            inflater.inflate(R.menu.group_component_menu, componentMenu.getMenu());
                                            componentMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch (item.getItemId()) {
                                                        case R.id.leave_group:
                                                            Intent intent = new Intent(GroupActivity.this, MainActivity.class);
                                                            String groupUID = postSnapshot.getKey();
                                                            ArrayList<String> courseSubscribers = group.getRecipients();
                                                            courseSubscribers.remove(user.getEmail());
                                                            database3.child("groups").child(groupUID).child("recipients").setValue(courseSubscribers);
                                                            startActivity(intent);
                                                            return true;
                                                        default:
                                                            return false;
                                                    }

                                                }
                                            });
                                            componentMenu.show();

                                        }
                                    });

                                } else {
                                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_baseline_add_24);
                                    fab.setImageDrawable(myDrawable);
                                    fab.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            PopupMenu studentMenu;
                                            studentMenu = new PopupMenu(GroupActivity.this, fab);
                                            MenuInflater inflater = studentMenu.getMenuInflater();
                                            inflater.inflate(R.menu.group_student_menu, studentMenu.getMenu());
                                            studentMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch (item.getItemId()) {
                                                        case R.id.student_tab:
                                                            String groupUID = postSnapshot.getKey();
                                                            ArrayList<String> groupSubscribers = group.getRecipients();
                                                            groupSubscribers.add(user.getEmail());
                                                            database3.child("groups").child(groupUID).child("recipients").setValue(groupSubscribers);
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

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                database.child("feedbacks").child(groupUID).addValueEventListener(new ValueEventListener() {

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
                Log.w("ERRORE", "loadPost:onCancelled", error.toException());
            }
        });
        


        postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (GroupActivity.this, NewPostActivity.class);
                i.putExtra("key", groupUID);
                startActivity(i);
            }
        });


        postNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (GroupActivity.this, NewPostActivity.class);
                i.putExtra("key", groupUID);
                startActivity(i);
            }
        });

        TextView rating = findViewById(R.id.toolbar_additional_infos);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (GroupActivity.this, FeedbackActivity.class);
                i.putExtra("key", groupUID);
                i.putExtra("reference",reference);
                i.putExtra("Name", args.getString("titleName"));
                startActivity(i);
            }
        });

        //Intent i = new Intent(GroupActivity.this, PostAdapter.class);
        //i.putExtra("reference" ,reference);





        final StorageReference fileRef = storageReference.child(groupUID + ".jpg");

        File cachedProPic = getBaseContext().getFilesDir();
        final File f = new File(cachedProPic, groupUID + ".jpg");
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
                        Toast.makeText(GroupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
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
        DatabaseReference userRef = ref.child(groupUID);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference fileRef = storageReference.child(userRef + ".jpg");
        fileRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(GroupActivity.this, getString(R.string.propic_change_success), Toast.LENGTH_SHORT).show();
                headerProPic = findViewById(R.id.header);
                headerProPic.setImageBitmap(bitmap);
                final File f = new File(getBaseContext().getFilesDir(), groupUID + "jpg");
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
                Toast.makeText(GroupActivity.this, getString(R.string.error_profile_picture), Toast.LENGTH_SHORT).show();
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
