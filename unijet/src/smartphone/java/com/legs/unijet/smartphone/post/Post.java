package com.legs.unijet.smartphone.post;

import java.util.ArrayList;

public class Post {

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getHasDocument() {
        return hasDocument;
    }

    public void setHasDocument(int hasDocument) {
        this.hasDocument = hasDocument;
    }

    public int getHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(int hasPicture) {
        this.hasPicture = hasPicture;
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

    public void setCommentSectionID(String commentSectionID) {
        this.commentSectionID = commentSectionID;
    }

    String author;



    Boolean isDeleted;
    int hasDocument, hasPicture;
    ArrayList<String> likes;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private long timestamp;




    public Post() {
    }

    public Post(String author, Boolean isDeleted, int hasDocument, int hasPicture, ArrayList<String> likes, long timestamp, String commentSectionID, String likesSectionId, String content) {
        this.author = author;
        this.isDeleted = isDeleted;
        this.hasDocument = hasDocument;
        this.hasPicture = hasPicture;
        this.likes = likes;
        this.timestamp = timestamp;
        this.commentSectionID = commentSectionID;
        this.likesSectionId = likesSectionId;
        this.content = content;
    }

    public String getLikesSectionId() {
        return likesSectionId;
    }

    public void setLikesSectionId(String likesSectionId) {
        this.likesSectionId = likesSectionId;
    }

    String commentSectionID;
    String likesSectionId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String content;


}


