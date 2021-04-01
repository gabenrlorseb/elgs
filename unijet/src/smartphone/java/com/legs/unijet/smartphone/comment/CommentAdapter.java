package com.legs.unijet.smartphone.comment;

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
import com.legs.unijet.smartphone.feedback.FeedbackAdapter;
import com.legs.unijet.smartphone.feedback.FeedbackSample;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
private final ArrayList<CommentSample> sampleList;
public static class CommentViewHolder extends  RecyclerView.ViewHolder{

    public ImageView author_propic;
    public TextView author_name;
    public TextView post_content;
    public TextView date_time;
    public TextView number_of_comments;
    public TextView number_of_likes;
    public boolean liked;
    public ImageView like;


    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        author_propic = itemView.findViewById(R.id.member_icon);
        author_name = itemView.findViewById(R.id.member_name);
        post_content = itemView.findViewById(R.id.post_text);
        date_time = itemView.findViewById(R.id.date_time);
        number_of_comments = itemView.findViewById(R.id.comments_number);
        number_of_likes = itemView.findViewById(R.id.likes_number);
        like = itemView.findViewById(R.id.like_button);

    }
}

    public CommentAdapter(ArrayList<CommentSample> exampleList) {
        this.sampleList = exampleList;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_sample, parent, false);
        return new CommentAdapter.CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.CommentViewHolder holder, int position) {
        final CommentSample currentItem = sampleList.get(position);
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
    }

    @Override
    public int getItemCount() {
        return sampleList.size();
    }



}

