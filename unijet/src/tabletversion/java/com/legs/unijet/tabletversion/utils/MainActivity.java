package com.legs.unijet.tabletversion.utils;

import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.legs.unijet.tabletversion.course.CreateCourse;
import com.legs.unijet.tabletversion.createGroupActivity.CreateGroupStart;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.fragment.CoursesFragment;
import com.legs.unijet.tabletversion.fragment.GroupsFragment;
import com.legs.unijet.tabletversion.fragment.MyUnijetFragment;
import com.legs.unijet.tabletversion.fragment.ProjectsFragment;
import com.legs.unijet.tabletversion.project.CreateProject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomSheetDialog bottomSheetDialog;
    DatabaseReference reference;
    String email;
    SensorEventListener m_sensorEventListener;

    Fragment mCurrentFrag;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_layout_main_activity);


        bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );

        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_create,
                (LinearLayout)findViewById(R.id.bottom_sheet));

        View bottomSheetViewProfessor = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_create_professor,
                (LinearLayout)findViewById(R.id.bottom_sheet));




        final FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        String userId;
        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser ();

        email=user.getEmail();

        if(email.contains("@studenti.uniba.it")) {
            reference = FirebaseDatabase.getInstance ().getReference ("students");
            bottomSheetDialog.setContentView(bottomSheetView);
            setBottomButtonsStudent(bottomSheetView);
        }
        else if(email.contains("@uniba.it")){
            reference = FirebaseDatabase.getInstance ().getReference ("teachers");
            bottomSheetDialog.setContentView(bottomSheetViewProfessor);
            setBottomButtonsProfessor(bottomSheetViewProfessor);
        }



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new MyUnijetFragment(), "mynujet");
        navigation.setSelectedItemId(R.id.myunijet_tab);

        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        sm.registerListener(m_sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);

        m_sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    loadFragment(new MyUnijetFragment(), "myunijet");
            }
        };



        /*loadFragment(new ProjectsFragment());
        navigation.setSelectedItemId(R.id.projects_tab);

        loadFragment(new CoursesFragment());
        navigation.setSelectedItemId(R.id.courses_tab);

        loadFragment(new GroupsFragment());
        navigation.setSelectedItemId(R.id.myunijet_tab);*/
    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.myunijet_tab:
                    fragment = new MyUnijetFragment();
                        loadFragment(fragment, "myunijet");
                    return true;
                case R.id.projects_tab:
                    fragment = new ProjectsFragment();
                        loadFragment(fragment, "projects");
                    return true;
                case R.id.courses_tab:
                    fragment = new CoursesFragment();
                    loadFragment(fragment, "courses");
                    return true;
                case R.id.groups_tab:
                    fragment = new GroupsFragment();
                    loadFragment(fragment, "groups");
                    return true;

            }

            return false;
        }
    };



    private void loadFragment(Fragment fragment, String tag_name) {

        if (!fragment.isInLayout()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment, tag_name);

            transaction.addToBackStack(fragment.getTag());
            transaction.commit();
        }

    }

    private void setBottomButtonsStudent (View view){

        LinearLayout firstButton = view.findViewById(R.id.first_button);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (MainActivity.this,CreateGroupStart.class));
            }
        });

        LinearLayout secondButton = view.findViewById(R.id.second_button);
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (MainActivity.this, CreateProject.class));
            }
        });

    }

    private void setBottomButtonsProfessor(View view) {
        LinearLayout firstButton = view.findViewById(R.id.first_button);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (MainActivity.this, CreateGroupStart.class));
            }
        });

        LinearLayout secondButton = view.findViewById(R.id.second_button);
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, CreateCourse.class);
                intent.putExtra ("email", email);
                startActivity (intent);
            }
        });
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

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }



}