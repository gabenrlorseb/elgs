package com.legs.unijet.tabletversion.post;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.comment.CommentActivity;
import com.legs.unijet.tabletversion.utils.DeleteConfirmation;
import com.legs.unijet.tabletversion.utils.SlidingImagesAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import static com.legs.unijet.smartphone.R.menu.post_menu;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> implements View.OnCreateContextMenuListener {
    private final ArrayList<PostSample> sampleList;
    private Context context;

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView author_propic;
        public TextView author_name;
        public TextView post_content;
        public TextView date_time;
        public TextView number_of_comments;
        public TextView number_of_likes;
        public EditText comment;
        public boolean liked;
        public ImageView like;
        public ViewPager image_area;
        public LinearLayout documents_area;
        public ImageView manage_post;


        public PostViewHolder(View itemView) {
            super(itemView);
            author_propic = itemView.findViewById(R.id.member_icon);
            author_name = itemView.findViewById(R.id.member_name);
            post_content = itemView.findViewById(R.id.post_text);
            date_time = itemView.findViewById(R.id.date_time);
            number_of_comments = itemView.findViewById(R.id.comments_number);
            number_of_likes = itemView.findViewById(R.id.likes_number);
            like = itemView.findViewById(R.id.like_button);
            image_area = itemView.findViewById(R.id.post_images);
            documents_area = itemView.findViewById(R.id.documents_area);
            comment = itemView.findViewById(R.id.comment_compose_box);
            manage_post = itemView.findViewById(R.id.manage_button);
        }

    }

    public PostAdapter(ArrayList<PostSample> exampleList) {
        this.sampleList = exampleList;
    }


    @Override
    public int getItemCount() {
        return sampleList.size();
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_bacheca_sample, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {

        File outputDir = holder.author_propic.getContext().getCacheDir();

        final PostSample currentItem = sampleList.get(position);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        final File localpropic = new File(outputDir, "propic" + currentItem.getAuthor_key() +".bmp");
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(currentItem.getAuthor_key() + ".jpg");
        if (!localpropic.exists()) {
            fileRef.getFile(localpropic).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    holder.author_propic.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.author_propic.setImageResource(R.drawable.ic_generic_user_avatar);
                }
            });
        } else {
            holder.author_propic.setImageBitmap(BitmapFactory.decodeFile(localpropic.getAbsolutePath()));
        }



        holder.author_name.setText(currentItem.getAuthor_name());
        holder.post_content.setText(currentItem.getPost_content());
        holder.number_of_likes.setText(Integer.toString(currentItem.getLikes()));
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
        String date = sdf.format(currentItem.getTimestamp() * 1000);
        holder.date_time.setText(date);




        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (v.getContext(), CommentActivity.class);
                if (localpropic.exists()) {
                    i.putExtra("authorBitmap", localpropic.getAbsolutePath());
                }
                i.putExtra("author", currentItem.getAuthor_name());
                i.putExtra("key", currentItem.getIdentifier());
                i.putExtra("UID", currentItem.getBachecaIdentifier());
                i.putExtra("postContent", currentItem.getPost_content());
                v.getContext().startActivity(i);
            }
        });

        final DatabaseReference[] likeRef = new DatabaseReference[1];


        FirebaseDatabase.getInstance().getReference().child("likes/" + currentItem.getIdentifier()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                holder.number_of_likes.setText(String.valueOf((int) snapshot.getChildrenCount()));

                for (DataSnapshot inSnapshot : snapshot.getChildren()) {
                    if (Objects.equals(inSnapshot.getValue(String.class), currentUser.getEmail())) {
                        likeRef[0] = inSnapshot.getRef();
                        holder.like.setColorFilter(Color.argb(255,255,0,0));
                        holder.liked = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.manage_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(holder.author_propic.getContext(), holder.manage_post);

                popup.inflate(post_menu);

                Menu popupMenu = popup.getMenu();

                if (!currentItem.getAuthor_key().equals(currentUser.getUid())) {
                    popup.getMenu().removeItem(R.id.delete);
                }

                if (currentItem.getHasPictures() == 0 || currentItem.getHasDocuments() == 0) {
                    popup.getMenu().removeItem(R.id.download_contents);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                DialogFragment deleteConfirm = new DeleteConfirmation();
                                Bundle bundle = new Bundle();
                                bundle.putString("bacheca", "posts/" + currentItem.getBachecaIdentifier() + "/" + currentItem.getKey());
                                deleteConfirm.setArguments(bundle);
                                FragmentManager manager = ((AppCompatActivity)holder.author_propic.getContext()).getSupportFragmentManager();
                                deleteConfirm.show(manager, "DeleteConfirmation");
                                return true;
                            default:
                                throw new IllegalStateException("Unexpected value: " + item.getItemId());
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.liked) {
                    int nightModeFlags =
                            holder.author_propic.getContext().getResources().getConfiguration().uiMode &
                                    Configuration.UI_MODE_NIGHT_MASK;
                    switch (nightModeFlags) {
                        case Configuration.UI_MODE_NIGHT_YES:
                            holder.like.setColorFilter(Color.argb(255, 255, 255, 255));
                            break;
                        case Configuration.UI_MODE_NIGHT_NO:
                        case Configuration.UI_MODE_NIGHT_UNDEFINED:
                            holder.like.setColorFilter(Color.argb(255, 0, 0, 0));
                            break;
                    }
                    holder.liked = false;
                    likeRef[0].removeValue();
                } else {
                    holder.like.setColorFilter(Color.argb(255,255,0,0));
                    final DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("likes/" + currentItem.getIdentifier());
                    database1.push().setValue(currentUser.getEmail());
                }
            }
        });


        FirebaseDatabase.getInstance().getReference().child("comments/" + currentItem.getIdentifier()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.number_of_comments.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        if (currentItem.getHasPictures() > 0) {

            final ViewPager layout = holder.image_area;
            layout.setVisibility(View.VISIBLE);

            final ArrayList<Bitmap> fetchedImages = new ArrayList<>();


            for (int i = 0; i < currentItem.getHasPictures(); i++) {
                final ImageView imageView = new ImageView(holder.author_propic.getContext());
                imageView.setId(i);
                imageView.setPadding(2, 2, 2, 2);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.drawable.ic_file_document);
                StorageReference fs = FirebaseStorage.getInstance().getReference("posts/" + currentItem.getBachecaIdentifier() + "/" + currentItem.getIdentifier()).child("pic" + i);

                final File localFile = new File(outputDir, "pic" + i + currentItem.getTimestamp() +".bmp");
                if (localFile.exists()) {
                    Bitmap tempImage= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    fetchedImages.add(tempImage);
                } else {
                    fetchedImages.add(BitmapFactory.decodeResource(holder.author_propic.getContext().getResources(), R.drawable.ic_ui_image));
                    fs.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap tempImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            fetchedImages.remove(0);
                            fetchedImages.add(tempImage);
                            layout.setAdapter(new SlidingImagesAdapter(holder.author_propic.getContext(), fetchedImages));
                            layout.setCurrentItem(0);
                        }
                    });

                }
                layout.setAdapter(new SlidingImagesAdapter(holder.author_propic.getContext(), fetchedImages));
                layout.setCurrentItem(0);
            }
        } else {
            holder.image_area.setVisibility(View.GONE);
        }


        if (currentItem.getHasDocuments() > 0) {
            final ArrayList<StorageReference> listOfDocs = new ArrayList<>();
            final LinearLayout layout = holder.documents_area;
            StorageReference fs = FirebaseStorage.getInstance().getReference("posts/" + currentItem.getBachecaIdentifier() + "/" + currentItem.getIdentifier());
            fs.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    listOfDocs.addAll(listResult.getItems());
                    for (int i = 0; i < listOfDocs.size(); i++) {

                        final LinearLayout nestedLayout = new LinearLayout(holder.author_propic.getContext());
                        nestedLayout.setOrientation(LinearLayout.HORIZONTAL);

                        ImageView documentIcon = new ImageView(nestedLayout.getContext());
                        documentIcon.setImageResource(R.drawable.ic_file_document);
                        documentIcon.requestLayout();

                        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, holder.itemView.getResources().getDisplayMetrics());
                        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, holder.itemView.getResources().getDisplayMetrics());

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) width, (int) height);
                        documentIcon.setLayoutParams(lp);
                        documentIcon.setPadding(40,0,0,10);
                        nestedLayout.addView(documentIcon);

                        final TextView document = new TextView(holder.author_propic.getContext());
                        document.setPadding(18, 10, 10, 10);
                        document.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                        document.setText(listOfDocs.get(i).getName());
                        document.setTypeface(null, Typeface.BOLD);
                        nestedLayout.addView(document);

                        layout.addView(nestedLayout);

                    }
                }
            });


        } else {
            holder.documents_area.setVisibility(View.GONE);
        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "SMS");

    }



}