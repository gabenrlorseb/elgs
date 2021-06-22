package com.legs.unijet.smartphone.project;

import java.util.ArrayList;

public class ProjectSample {

    private final String mText1;
    private final  String mText2;
    private  ArrayList<String>   mText5;
    private   ArrayList<String> mText3;
    private    ArrayList<String>  mText4;

    public ProjectSample(String namesString, String mail) {
        this.mText1 = namesString;
        this.mText2 = mail;
    }

    public ProjectSample(String nameProjects, String nameGroups, ArrayList<String> strings,  ArrayList<String> mailAutor, ArrayList<String> finalNameOWners) {
        this.mText1 = nameProjects;
        this.mText2 = nameGroups;
        this.mText3=strings;
        this.mText5=mailAutor;
        this.mText4=finalNameOWners;

    }


    public String getText1() {
        return mText1;
    }
    public  String getText2() {
        return mText2;
    }
    public ArrayList<String> getText3() {
        return mText3;
    }
    public  ArrayList<String>  getText4() {
        return mText4;
    }

    public ArrayList<String>  getText5() {
        return mText5;
    }

}
