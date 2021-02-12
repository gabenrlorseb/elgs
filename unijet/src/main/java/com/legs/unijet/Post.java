package com.legs.unijet;

import java.util.ArrayList;

public class Post {
    String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getHasDocument() {
        return hasDocument;
    }

    public void setHasDocument(Boolean hasDocument) {
        this.hasDocument = hasDocument;
    }

    public Boolean getHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(Boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCommentSectionID() {
        return commentSectionID;
    }

    public void setCommentSectionID(int commentSectionID) {
        this.commentSectionID = commentSectionID;
    }

    String author;
    String type;


    Boolean hasDocument, hasPicture, isDeleted;
    int likes;


    public Post() {
    }

    public Post(String ID, String author, String type, Boolean hasDocument, Boolean hasPicture, Boolean isDeleted, int likes, int commentSectionID) {
        this.ID = ID;
        this.author = author;
        this.type = type;
        this.hasDocument = hasDocument;
        this.hasPicture = hasPicture;
        this.isDeleted = isDeleted;
        this.likes = likes;
        this.commentSectionID = commentSectionID;
    }

    int commentSectionID;


}


