package com.legs.unijet.tabletversion;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.legs.unijet.smartphone.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        public HorizontalScrollView image_area;
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

        if (currentItem.isHasPictures()) {
            HorizontalScrollView layout = (HorizontalScrollView) holder.image_area;
            for (int i = 0; i < currentItem.getNumber_of_pics(); i++) {
                ImageView imageView = new ImageView(holder.author_propic.getContext());
                imageView.setId(i);
                imageView.setPadding(2, 2, 2, 2);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageBitmap(currentItem.getImages().get(i));
                layout.addView(imageView);
            }
        }


        if (currentItem.isHasDocuments()) {
            LinearLayout layout = (LinearLayout) holder.documents_area;
            for (int i = 0; i < currentItem.getNumber_of_pics(); i++) {
                TextView document = new TextView(holder.author_propic.getContext());
                document.setText(currentItem.getDocuments().get(i));
                document.setPadding(10,10,10,10);
                layout.addView(document);
            }
        }


    }



}