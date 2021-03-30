package com.legs.unijet.tabletversion.post;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.utils.SlidingImagesAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final ArrayList<PostSample> sampleList;

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView author_propic;
        public TextView author_name;
        public TextView post_content;
        public TextView date_time;
        public TextView number_of_comments;
        public TextView number_of_likes;
        public boolean liked;
        public ImageView like;
        public ViewPager image_area;
        public LinearLayout documents_area;


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
        final PostSample currentItem = sampleList.get(position);
        holder.author_propic.setImageBitmap(currentItem.getAuthor_propic());
        holder.author_name.setText(currentItem.getAuthor_name());
        holder.post_content.setText(currentItem.getPost_content());
        holder.number_of_likes.setText(Integer.toString(currentItem.getLikes()));
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
        String date = sdf.format(currentItem.getTimestamp() * 1000);
        holder.date_time.setText(date);
        if (holder.liked) {
            holder.like.setColorFilter(Color.argb(255,255,0,0));
        }

        holder.date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.liked) {
                    holder.like.setColorFilter(Color.argb(255,0,0,0));
                    holder.liked = false;
                } else {
                    holder.like.setColorFilter(Color.argb(255,255,0,0));
                    holder.liked = true;
                }
            }
        });

        if (currentItem.getHasPictures() > 0) {

            final ViewPager layout = holder.image_area;
            layout.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            final ArrayList<Bitmap> fetchedImages = new ArrayList<>();


            for (int i = 0; i < currentItem.getHasPictures(); i++) {
                final ImageView imageView = new ImageView(holder.author_propic.getContext());
                imageView.setId(i);
                imageView.setPadding(2, 2, 2, 2);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.drawable.ic_file_document);
                StorageReference fs = FirebaseStorage.getInstance().getReference("posts/" + currentItem.getBachecaIdentifier() + "/" + currentItem.getIdentifier()).child("pic" + i);
                File outputDir = holder.author_propic.getContext().getCacheDir();
                final File localFile = new File(outputDir, "pic" + i + currentItem.getTimestamp() +".bmp");
                if (localFile.exists()) {
                    Bitmap tempImage= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    fetchedImages.add(tempImage);
                } else {
                    fetchedImages.add(BitmapFactory.decodeResource(holder.author_propic.getContext().getResources(), R.drawable.ic_ui_image));
                    fs.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.v("ATTENZIONE", "trovata la reference");
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
                    Log.v("TOTTI", "GOL");
                    listOfDocs.addAll(listResult.getItems());
                    for (int i = 0; i < listOfDocs.size(); i++) {

                        final LinearLayout nestedLayout = new LinearLayout(holder.author_propic.getContext());
                        nestedLayout.setOrientation(LinearLayout.HORIZONTAL);

                        ImageView documentIcon = new ImageView(nestedLayout.getContext());
                        documentIcon.setImageResource(R.drawable.ic_file_document);
                        documentIcon.requestLayout();

                        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, holder.itemView.getResources().getDisplayMetrics());
                        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, holder.itemView.getResources().getDisplayMetrics());

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



}