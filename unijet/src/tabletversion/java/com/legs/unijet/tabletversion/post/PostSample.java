package com.legs.unijet.tabletversion.post;


public class PostSample  {
    public PostSample() {
    }

    private String author_key;

    private String author_name;
    private String post_content;
    private String identifier;
    private String bachecaIdentifier;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public PostSample(String author_key, String author_name, String post_content, int hasPictures, int hasDocuments, String postIdentifier, String bachecaIdentifier,  long timestamp) {
        this.author_key = author_key;
        this.author_name = author_name;
        this.post_content = post_content;
        this.timestamp = timestamp;
        this.bachecaIdentifier = bachecaIdentifier;
        this.identifier = postIdentifier;
        this.hasPictures = hasPictures;
        this.hasDocuments = hasDocuments;
    }



    public String getAuthor_key() {
        return author_key;
    }

    public void setAuthor_key(String author_key) {
        this.author_key = author_key;
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


    public void setHasPictures(int hasPictures) {
        this.hasPictures = hasPictures;
    }

    public int isHasDocuments() {
        return hasDocuments;
    }

    public void setHasDocuments(int hasDocuments) {
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

    public int getHasPictures() {
        return hasPictures;
    }

    public int getHasDocuments() {
        return hasDocuments;
    }

    private long timestamp;
    private int likes;
    private int hasPictures;
    private int hasDocuments;
    private boolean isLiked;
    private int comments;

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