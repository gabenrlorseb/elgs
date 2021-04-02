package com.legs.unijet.tabletversion.comment;

import java.util.ArrayList;

public class Comment {
    String author;
    Boolean isDeleted;
    ArrayList<String> likes;
    String likesSectionId;
    private long timestamp;
    String content;

    public Comment() {
    }

    public Comment(String author, Boolean isDeleted, ArrayList<String> likes, long timestamp, String likesSectionId, String content) {
        this.author = author;
        this.isDeleted = isDeleted;
        this.likes = likes;
        this.timestamp = timestamp;
        this.likesSectionId = likesSectionId;
        this.content = content;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

}
