package com.legs.unijet.smartphone.utils;

import com.legs.unijet.smartphone.post.PostSample;

import java.util.Comparator;

public class CustomComparator implements Comparator<PostSample> {

    public long diff;

    @Override
    public int compare(PostSample o1, PostSample o2) {
        return this.diff<o1.getTimestamp()?-1: this.diff<o2.getTimestamp()?1:0;
    }
}
