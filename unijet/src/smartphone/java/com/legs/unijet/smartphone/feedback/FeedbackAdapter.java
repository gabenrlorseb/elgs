package com.legs.unijet.smartphone.feedback;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.legs.unijet.smartphone.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private final ArrayList<FeedbackSample> sampleList;
    public static class FeedbackViewHolder extends  RecyclerView.ViewHolder{

        public ImageView author_propic;
        public TextView author_name;
        public TextView post_content;
        public TextView date_time;
        public TextView number_of_comments;
        public TextView number_of_likes;
        public RatingBar rating;
        public boolean liked;
        public ImageView like;


        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            author_propic = itemView.findViewById(R.id.member_icon);
            author_name = itemView.findViewById(R.id.member_name);
            post_content = itemView.findViewById(R.id.post_text);
            date_time = itemView.findViewById(R.id.date_time);
            number_of_comments = itemView.findViewById(R.id.comments_number);
            number_of_likes = itemView.findViewById(R.id.likes_number);
            like = itemView.findViewById(R.id.like_button);
            rating = itemView.findViewById(R.id.rating);

        }
    }

    public FeedbackAdapter(ArrayList<FeedbackSample> exampleList) {
        this.sampleList = exampleList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_bacheca_sample, parent, false);
        return new FeedbackAdapter.FeedbackViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedbackViewHolder holder, int position) {
        final FeedbackSample currentItem = sampleList.get(position);
        holder.author_propic.setImageBitmap(currentItem.getAuthor_propic());
        holder.author_name.setText(currentItem.getAuthor_name());
        holder.post_content.setText(currentItem.getPost_content());
        holder.number_of_likes.setText(Integer.toString(currentItem.getLikes()));
        holder.rating.setRating(currentItem.getRating());
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
    }

    @Override
    public int getItemCount() {
        return sampleList.size();
    }



}
