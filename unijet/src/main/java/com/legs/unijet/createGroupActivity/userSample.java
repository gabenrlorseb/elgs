package com.legs.unijet.createGroupActivity;

import android.widget.CheckBox;

public class userSample {
    private int mImageResource;
    private String mText1;
    private String mText2;
    private Boolean isSelected;

    public userSample(int imageResource, String text1, String text2, Boolean selected) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        isSelected = selected;
    }

    public boolean getChecked() { return isSelected; }

    public void setChecked(Boolean bool) { isSelected = bool; }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }
}