package com.legs.unijet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.legs.unijet.createGroupActivity.CreateGroupStart;

public class MainActivity extends AppCompatActivity {

    BottomSheetDialog bottomSheetDialog;
    DatabaseReference reference;
    String email;

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




        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        String userId;
        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser ();
        userId=user.getUid ();
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

        loadFragment(new MyUnijetFragment());
        navigation.setSelectedItemId(R.id.myunijet_tab);

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
                    loadFragment(fragment);
                    return true;
                case R.id.projects_tab:
                    fragment = new ProjectsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.courses_tab:
                    fragment = new CoursesFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.groups_tab:
                    fragment = new GroupsFragment();
                    loadFragment(fragment);
                    return true;

            }

            return false;
        }
    };



    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
                startActivity(new Intent (MainActivity.this,CreateProject.class));
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
                Intent intent = new Intent (MainActivity.this,CreateCourse.class);
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

}