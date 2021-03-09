package com.legs.unijet.tabletversion;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class PostSample implements Serializable {
    public PostSample() {
    }

    private Bitmap author_propic;

    private String author_name;
    private String post_content;

    public int getNumber_of_pics() {
        return number_of_pics;
    }

    public void setNumber_of_pics(int number_of_pics) {
        this.number_of_pics = number_of_pics;
    }

    public int getNumber_of_docs() {
        return number_of_docs;
    }

    public void setNumber_of_docs(int number_of_docs) {
        this.number_of_docs = number_of_docs;
    }


    private int number_of_pics;
    private int number_of_docs;

    private ArrayList<String> documents;
    private ArrayList<Bitmap> images;

    public PostSample(Bitmap author_propic, String author_name, String post_content, int number_of_pics, int number_of_docs, ArrayList<String> documents, ArrayList<Bitmap> images, long timestamp, int likes, boolean hasPictures, boolean hasDocuments, boolean isLiked, int comments) {
        this.author_propic = author_propic;
        this.author_name = author_name;
        this.post_content = post_content;
        this.number_of_pics = number_of_pics;
        this.number_of_docs = number_of_docs;
        this.documents = documents;
        this.images = images;
        this.timestamp = timestamp;
        this.likes = likes;
        this.hasPictures = hasPictures;
        this.hasDocuments = hasDocuments;
        this.isLiked = isLiked;
        this.comments = comments;
    }

    public ArrayList<String> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<String> documents) {
        this.documents = documents;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
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

    public boolean isHasPictures() {
        return hasPictures;
    }

    public void setHasPictures(boolean hasPictures) {
        this.hasPictures = hasPictures;
    }

    public boolean isHasDocuments() {
        return hasDocuments;
    }

    public void setHasDocuments(boolean hasDocuments) {
        this.hasDocuments = hasDocuments;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    private long timestamp;
    private int likes;
    private boolean hasPictures;
    private boolean hasDocuments;
    private boolean isLiked;
    private int comments;

}