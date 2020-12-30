package com.legs.unijet.splashactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.legs.unijet.R;

import java.util.List;


public class SplashScreenAdapter extends RecyclerView.Adapter<SplashScreenAdapter.SplashScreenViewHolder> {

    private List<SplashScreenItem> splashScreenItems;

    public SplashScreenAdapter(List<SplashScreenItem> splashScreenItems) {
        this.splashScreenItems = splashScreenItems;
    }

    @NonNull
    @Override
    public SplashScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SplashScreenViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.splashscreen_single_item, parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SplashScreenViewHolder holder, int position) {
        holder.setSplashScreenItemDatas(splashScreenItems.get(position));
    }

    @Override
    public int getItemCount() {
        return splashScreenItems.size();
    }

    class SplashScreenViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDescription;
        private ImageView itemSplashImage;

        SplashScreenViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.splash_title);
            textDescription = itemView.findViewById(R.id.splash_description);
            itemSplashImage = itemView.findViewById(R.id.splash_image);
        }

        void setSplashScreenItemDatas(SplashScreenItem splashScreenItem) {
            textTitle.setText(splashScreenItem.getTitle());
            textDescription.setText(splashScreenItem.getDescription());
            itemSplashImage.setImageResource(splashScreenItem.getImage());
        }
    }



}