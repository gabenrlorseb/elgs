package com.legs.unijet.smartphone.project;

import java.util.ArrayList;

public class ProjectSample {

    private final String mText1;
    private final  String mText2;
    private   String mText5;
    private   ArrayList<String> mText3;
    private    ArrayList<String>  mText4;
    public ProjectSample(String text1, String text2, ArrayList<String> text3) {
        this.mText1 = text1;
        this.mText2 = text2;
        this.mText3=text3;
    }


    public ProjectSample(String nameProjects, String nameGroups, ArrayList<String> strings, String s, ArrayList<String> finalNameOWners) {
        this.mText1 = nameProjects;
        this.mText2 = nameGroups;
        this.mText5=s;
        this.mText3=strings;
        this.mText4=finalNameOWners;

    }

    public ProjectSample(String namesString, String mail) {
        this.mText1 = namesString;
        this.mText2 = mail;
    }


    public String getText1() {
        return mText1;
    }

    public String getText5() {
        return mText5;
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
}
