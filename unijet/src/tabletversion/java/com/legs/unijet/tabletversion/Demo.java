package com.legs.unijet.tabletversion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.course.CreateCourse;
import com.legs.unijet.tabletversion.createGroupActivity.CreateGroupStart;
import com.legs.unijet.tabletversion.fragment.CoursesFragment;
import com.legs.unijet.tabletversion.fragment.GroupsFragment;
import com.legs.unijet.tabletversion.fragment.MyUnijetFragment;
import com.legs.unijet.tabletversion.fragment.ProjectsFragment;

import java.util.List;

public class Demo extends AppCompatActivity {

    BottomSheetDialog bottomSheetDialog;
    String email;
    OrientationEventListener m_sensorEventListener;

    String currentTag;
    FragmentTransaction supportFt;


    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_layout_main_activity);


        bottomSheetDialog = new BottomSheetDialog(
                Demo.this, R.style.BottomSheetDialogTheme
        );

        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_create,
                (LinearLayout)findViewById(R.id.bottom_sheet));

        View bottomSheetViewProfessor = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_create_professor,
                (LinearLayout)findViewById(R.id.bottom_sheet));




        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        String userId;
        FirebaseUser user;


        final BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




        m_sensorEventListener = new OrientationEventListener(getApplicationContext()) {
            @Override
            public void onOrientationChanged(int orientation) {
                loadFragment(new MyUnijetFragment(), "myunijet", false);
                navigation.setSelectedItemId(R.id.myunijet_tab);
            }

        };

        m_sensorEventListener.enable();

    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = Demo.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.myunijet_tab:
                    fragment = new MyUnijetFragment();
                    loadFragment(fragment, "myunijet", false);
                    return true;
                case R.id.projects_tab:
                    fragment = new ProjectsFragment ();
                    loadFragment(fragment, "projects", false);
                    return true;
                case R.id.courses_tab:
                    fragment = new CoursesFragment ();
                    loadFragment(fragment, "courses", false);
                    return true;
                case R.id.groups_tab:
                    fragment = new GroupsFragment ();
                    loadFragment(fragment, "groups", false);
                    return true;

            }

            return false;
        }
    };






    private void loadFragment(Fragment fragment, String tagName, boolean reloading) {

        if (!reloading) {
            Fragment actualFragment = getSupportFragmentManager().findFragmentByTag(tagName);
            if (actualFragment != null && actualFragment.isVisible()) {
                return;
            }
        }
        currentTag = tagName;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tagName);
        transaction.addToBackStack(tagName);
        if (!fm.isDestroyed()) {

            transaction.commit();
        }
    }



    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }




}