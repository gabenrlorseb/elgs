package com.legs.unijet.smartphone.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.legs.unijet.smartphone.R;

import java.util.ArrayList;

public class SlidingImagesAdapter extends PagerAdapter {


    private final ArrayList<Bitmap> IMAGES;
    private final LayoutInflater inflater;


    public SlidingImagesAdapter (Context context,ArrayList<Bitmap> IMAGES) {
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.post_sliding_images_element, view, false);

        TextView indicator = imageLayout.findViewById(R.id.current_image_indicator);

        if (getCount() > 1) {
            indicator.setVisibility(View.VISIBLE);
            indicator.setText( (position + 1 )+ "/" + getCount());
        }

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.image);


        imageView.setImageBitmap(IMAGES.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
