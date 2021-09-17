package com.legs.unijet.tabletversion.course;

import java.util.ArrayList;

public class CourseSample {
    private final String mText1;
    private final  String mText2;
    private ArrayList<String> mText5;
    private ArrayList<String> mText3;
    private ArrayList<String> mText4;


    public CourseSample(String namesString, String mail) {
        this.mText1 = namesString;
        this.mText2 = mail;

    }
    public CourseSample(String text1,String mail, ArrayList<String> text5,ArrayList<String> text3,ArrayList<String> finalNameOWners) {
        this.mText1 = text1;
        this.mText2=mail;
        this.mText5 = text5;
        this.mText3=text3;
        this.mText4=finalNameOWners;
    }








    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }
    public ArrayList<String> getText3() {
        return mText3;
    }
    public ArrayList<String> getText4() {
        return mText4;
    }
    public ArrayList<String> getText5() {
        return mText5;
    }


}


