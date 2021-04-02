package com.legs.unijet.tabletversion.splashactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.legs.unijet.tabletversion.LoginActivity;
import com.legs.unijet.smartphone.R;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private SplashScreenAdapter splashScreenAdapter;
    private LinearLayout layoutSplashScreenIndicator;
    private MaterialButton splashScreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_splash_screen);

        layoutSplashScreenIndicator = findViewById(R.id.splash_indicators_layout);
        splashScreenButton = findViewById(R.id.splash_navigation_button);

        setupSplashScreenItems();

        final ViewPager2 splashScreenViewPager = findViewById(R.id.splashScreen_viewpager);
        splashScreenViewPager.setAdapter(splashScreenAdapter);

        setupPageIndicators();
        setSplashScreenCurrentIndicator(0);

        splashScreenViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setSplashScreenCurrentIndicator(position);
            }
        });

        splashScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (splashScreenViewPager.getCurrentItem() + 1 < splashScreenAdapter.getItemCount()){
                    splashScreenViewPager.setCurrentItem(splashScreenViewPager.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        });
    }

    private void setupSplashScreenItems(){
        List<SplashScreenItem> splashScreenItemsList = new ArrayList<>();

        SplashScreenItem firstItem = new SplashScreenItem();
        firstItem.setTitle(getString(R.string.welcome_unijet));
        firstItem.setDescription(getString(R.string.welcome_slogan));
        firstItem.setImage(R.drawable.ic_splash_illustration_one); //TODO: impostare le immagini

        SplashScreenItem secondItem = new SplashScreenItem();
        secondItem.setTitle(getString(R.string.create_groups));
        secondItem.setDescription(getString(R.string.create_groups_description));
        secondItem.setImage(R.drawable.ic_splash_illustration_two);

        SplashScreenItem thirdItem = new SplashScreenItem();
        thirdItem.setTitle(getString(R.string.boost_projects));
        thirdItem.setDescription(getString(R.string.boost_projects_description));
        thirdItem.setImage(R.drawable.ic_splash_illustration_three);

        SplashScreenItem fourthItem = new SplashScreenItem();
        fourthItem.setTitle(getString(R.string.check_material));
        fourthItem.setDescription(getString(R.string.check_material_description));
        fourthItem.setImage(R.drawable.ic_splash_illustration_four);

        SplashScreenItem fifthItem = new SplashScreenItem();
        fifthItem.setTitle(getString(R.string.ready_send));
        fifthItem.setDescription(getString(R.string.ready_send_description));
        fifthItem.setImage(R.drawable.ic_splash_illustration_five);

        splashScreenItemsList.add(firstItem);
        splashScreenItemsList.add(secondItem);
        splashScreenItemsList.add(thirdItem);
        splashScreenItemsList.add(fourthItem);
        splashScreenItemsList.add(fifthItem);

        splashScreenAdapter = new SplashScreenAdapter(splashScreenItemsList);
    }

    private void setupPageIndicators(){
        ImageView[] indicator = new ImageView[splashScreenAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicator.length; i++){
            indicator[i] = new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.indicator_active
            ));
            indicator[i].setLayoutParams(layoutParams);
            layoutSplashScreenIndicator.addView(indicator[i]);
        }
    }

    private void setSplashScreenCurrentIndicator(int index){
        int childCount = layoutSplashScreenIndicator.getChildCount();
        for (int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) layoutSplashScreenIndicator.getChildAt(i);
            if (i == index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicator_inactive)
                );
            }
        }
        if (index == splashScreenAdapter.getItemCount() - 1){
            splashScreenButton.setText(getString(R.string.START));
        } else {
            splashScreenButton.setText(getString(R.string.NEXTT));
        }
    }
}