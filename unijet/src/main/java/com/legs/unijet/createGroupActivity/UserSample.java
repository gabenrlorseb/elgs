package com.legs.unijet.createGroupActivity;

public class UserSample {
    private int mImageResource;
    private String mText1;
    private String mText2;
    private Boolean isChecked;


    public UserSample(int imageResource, String text1, String text2, Boolean checkbox) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        isChecked = checkbox;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean status) {
        isChecked = status;

    }
}