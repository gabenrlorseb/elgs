 package com.legs.unijet.groupDetailsActivity;

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
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.google.android.gms.tasks.OnFailureListener;
 import com.google.android.gms.tasks.OnSuccessListener;
 import com.google.android.material.appbar.CollapsingToolbarLayout;
 import com.google.android.material.floatingactionbutton.FloatingActionButton;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
 import com.google.firebase.database.ChildEventListener;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;
 import com.google.firebase.storage.FileDownloadTask;
 import com.google.firebase.storage.FirebaseStorage;
 import com.google.firebase.storage.StorageReference;
 import com.google.firebase.storage.UploadTask;
 import com.legs.unijet.Group;
 import com.legs.unijet.Post;
 import com.legs.unijet.PostAdapter;
 import com.legs.unijet.PostSample;
 import com.legs.unijet.R;
 import com.legs.unijet.User;

 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.util.ArrayList;

 public class  GroupActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    final int PIC_CROP = 2;
    Group group;
    String  groupUID;
    Bitmap bitmap;
    Uri selectedImageUri;
    StorageReference storageReference;
    ImageView headerProPic;
    Boolean isAuthor;
     RecyclerView recyclerViewBacheca;
     private PostAdapter postAdapter;

     private ArrayList<PostSample> fetchedPosts;




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
        final DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();
        final StorageReference reference1 = FirebaseStorage.getInstance().getReference("posts");

        final FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();



        database.child("groups").orderByChild("name").equalTo(args.getString("GName")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
                    group = postSnapshot.getValue(Group.class);
                    if (user.getEmail().equals(group.getAuthor())) {
                        isAuthor = true;
                    }
                    groupUID = snapshot.getKey();
                    ArrayList<String> addedMails = group.getRecipients();
                    NumberOfMembers[0] = addedMails.size() + 1;
                    final String[] groupAuthorName = new String[1];

                    database2.child("students").orderByChild("email").equalTo(group.getAuthor()).addValueEventListener (new ValueEventListener() {
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
                                        Log.v("VALORE NOME", groupAuthorName[0]);
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

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ERRORE", "loadPost:onCancelled", error.toException());
            }
        });

        recyclerViewBacheca = findViewById(R.id.recyclerview_posts);

        database.child("posts/" + groupUID).orderByChild("id").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final ArrayList<Bitmap> fetchedImages = new ArrayList<>();
                final ArrayList<String> fetchedDocs = new ArrayList<>();

                final Bitmap[] authorBitmap = new Bitmap[1];

                boolean hasPictures = false;
                boolean hasDocuments = false;

                Post newPost = snapshot.getValue(Post.class);
                int PostID = newPost.getID();
                int numberOfPics = newPost.getHasPicture();
                final int numberOfDocs = newPost.getHasDocument();
                final String[] authorName = new String[1];

                final String[] authorKey = new String[1];

                database.child("students").orderByChild("email").equalTo(newPost.getAuthor()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        authorKey[0] = snapshot.getKey();
                        authorName[0] = user.getName() + user.getSurname();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (numberOfPics != 0) {
                    hasPictures = true;
                    for (int i=0; i<numberOfPics ; i ++ ) {
                        reference1.child(groupUID + "/" + PostID + "pic" + numberOfPics).getBytes(2048 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap tempImageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                fetchedImages.add(tempImageBitmap);
                            }
                        });
                    }
                }



                if (numberOfDocs != 0) {
                    hasDocuments = true;
                    for (int i=0; i<numberOfDocs ; i ++ ) {
                        final ArrayList<Uri> newArrayList = null;
                        reference1.child(groupUID + "/" + PostID + "document" + numberOfDocs).getFile(newArrayList.get(numberOfDocs)).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                String result = newArrayList.get(numberOfDocs).getPath();
                                int cut = result.lastIndexOf('/');
                                if (cut != -1) {
                                    result = result.substring(cut + 1);
                                }
                                fetchedDocs.add(result);
                            }
                        });
                    }
                }

                File cachedProPic = getBaseContext().getFilesDir();
                final File f = new File(cachedProPic, authorKey[0] + ".jpg");
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(f);
                    authorBitmap[0] = BitmapFactory.decodeFile(f.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (fis != null) {
                    reference1.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.v("AVVISO", "Il file è stato scaricato dal database");
                            authorBitmap[0] = BitmapFactory.decodeFile(f.getAbsolutePath());
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
                            Toast.makeText(GroupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            authorBitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_generic_user_avatar);
                        }
                    });
                }

                boolean liked = false;

                if (newPost.getLikes().contains(CurrentUser.getEmail())) {
                    liked = true;
                }

                final int[] numberOfComments = {0};

                database.child("comments").orderByKey().equalTo(newPost.getCommentSectionID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        numberOfComments[0] = (int) snapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                fetchedPosts.add(new PostSample(authorBitmap[0], authorName[0], newPost.getContent(), numberOfPics, numberOfDocs, fetchedDocs, fetchedImages, newPost.getTimestamp(), newPost.getLikes().size(), hasPictures, hasDocuments, liked, numberOfComments[0]));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                        Toast.makeText(GroupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    protected void buildBacheca () {
        recyclerViewBacheca.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        postAdapter = new PostAdapter(fetchedPosts);
        recyclerViewBacheca.setLayoutManager(mLayoutManager);
        recyclerViewBacheca.setAdapter(postAdapter);
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

}
