package com.legs.unijet.smartphone.course;

import android.util.Log;

import java.util.ArrayList;

public class CourseSample {
    private final String mText1;
    private final String mText2;
    private ArrayList<String> mText3;
    private ArrayList<String> mText4;


    public CourseSample(String text1, String text2,ArrayList<String> text3,ArrayList<String> finalNameOWners) {
        this.mText1 = text1;
        this.mText2 = text2;
        this.mText3=text3;
        this.mText4=finalNameOWners;
    }



    public CourseSample(String namesString, String mail) {
        this.mText1 = namesString;
        this.mText2 = mail;
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

    @Override
    public String toString() {
        return "CourseSample{" +
                "mText1='" + mText4 + '\'' +
                '}';
    }
}

