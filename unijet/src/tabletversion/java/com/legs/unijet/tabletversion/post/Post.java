package com.legs.unijet.tabletversion.post;

import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

public class Post {
    int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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

    public Post(int ID, String author, Boolean isDeleted, int hasDocument, int hasPicture, ArrayList<String> likes, long timestamp, String commentSectionID, String likesSectionId, String content) {
        this.ID = ID;
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


