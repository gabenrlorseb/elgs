package com.legs.unijet;

import java.util.ArrayList;

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
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
    int likes;


    public Post() {
    }

    public Post(int ID, String author, String content, int hasDocument, int hasPicture, Boolean isDeleted, int likes, String commentSectionID) {
        this.ID = ID;
        this.content = content;
        this.author = author;
        this.hasDocument = hasDocument;
        this.hasPicture = hasPicture;
        this.isDeleted = isDeleted;
        this.likes = likes;
        this.commentSectionID = commentSectionID;
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


