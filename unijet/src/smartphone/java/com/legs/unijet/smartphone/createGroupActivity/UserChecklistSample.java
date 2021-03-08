package com.legs.unijet.smartphone.createGroupActivity;

import java.io.Serializable;

public class UserChecklistSample implements Serializable {
    private final int mImageResource;
    private final String mText1;
    private final String mailText;
    private Boolean isChecked;


    public UserChecklistSample(int imageResource, String text1, String text2, Boolean checkbox) {
        mImageResource = imageResource;
        mText1 = text1;
        mailText = text2;
        isChecked = checkbox;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mailText;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean status) {
        isChecked = status;

    }
}