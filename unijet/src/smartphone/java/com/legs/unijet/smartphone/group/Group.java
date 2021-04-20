package com.legs.unijet.smartphone.group;

import java.util.ArrayList;

public class Group {

    String name;
    String department;
    String author;

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    Boolean isPrivate;
    ArrayList<String> recipients;

    public Group() {

    }

    public Group(String name, String author, ArrayList<String> recipients,  String department, Boolean beingPrivate) {
        this.name = name;
        this.author = author;
        this.recipients = recipients;
        this.department = department;
        this.isPrivate = beingPrivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }

}


