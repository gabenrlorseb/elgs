package com.legs.unijet.tabletversion.comment;

import android.graphics.Bitmap;

public class CommentSample {
    public CommentSample() {
    }

    private Bitmap author_propic;
    private String author_name;
    private String post_content;
    private String identifier;
    private String bachecaIdentifier;
    private long timestamp;
    private int likes;
    private boolean isLiked;


    public CommentSample(Bitmap author_propic, String author_name, String post_content, String postIdentifier, String bachecaIdentifier,  long timestamp, int likes, boolean isLiked) {
        this.author_propic = author_propic;
        this.author_name = author_name;
        this.post_content = post_content;
        this.timestamp = timestamp;
        this.likes = likes;
        this.bachecaIdentifier = bachecaIdentifier;
        this.identifier = postIdentifier;
        this.isLiked = isLiked;
    }



    public Bitmap getAuthor_propic() {
        return author_propic;
    }

    public void setAuthor_propic(Bitmap author_propic) {
        this.author_propic = author_propic;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getBachecaIdentifier() {
        return bachecaIdentifier;
    }

    public void setBachecaIdentifier(String bachecaIdentifier) {
        this.bachecaIdentifier = bachecaIdentifier;
    }
}
