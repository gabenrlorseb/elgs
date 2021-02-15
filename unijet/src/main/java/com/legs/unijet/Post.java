package com.legs.unijet;

import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
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

    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }

    private Map<String, String> timestamp;




    public Post() {
    }

    public Post(int ID, String author, String content, int hasDocument, int hasPicture, Boolean isDeleted, ArrayList<String> likes, String commentSectionID, Map<String, String> timestamp) {
        this.ID = ID;
        this.content = content;
        this.author = author;
        this.hasDocument = hasDocument;
        this.hasPicture = hasPicture;
        this.isDeleted = isDeleted;
        this.likes = likes;
        this.commentSectionID = commentSectionID;
        this.timestamp = timestamp;
    }

    String commentSectionID;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String content;


}


