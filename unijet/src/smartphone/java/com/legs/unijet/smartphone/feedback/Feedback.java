package com.legs.unijet.smartphone.feedback;

import java.util.ArrayList;

public class Feedback {

    String author;
    Boolean isDeleted;
    int hasDocument, hasPicture;
    float rating;
    ArrayList<String> likes;
    String commentSectionID;
    String likesSectionId;
    private long timestamp;
    String content;

    public Feedback() {
    }

    public Feedback(String author, Boolean isDeleted, ArrayList<String> likes, long timestamp, String commentSectionID, String likesSectionId, String content, float rating) {
        this.author = author;
        this.isDeleted = isDeleted;
        this.likes = likes;
        this.timestamp = timestamp;
        this.commentSectionID = commentSectionID;
        this.likesSectionId = likesSectionId;
        this.content = content;
        this.rating = rating;
    }

    public String getLikesSectionId() {
        return likesSectionId;
    }

    public void setLikesSectionId(String likesSectionId) {
        this.likesSectionId = likesSectionId;
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

    public String getCommentSectionID() {
        return commentSectionID;
    }

    public float getRating(){
        return rating;
    }

    public void setRating(float rating){
    this.rating = rating;
    }

    public void setCommentSectionID(String commentSectionID) {
        this.commentSectionID = commentSectionID;
    }



}
