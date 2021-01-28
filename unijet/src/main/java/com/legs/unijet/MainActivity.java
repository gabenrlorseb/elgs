package com.legs.unijet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.setContentView(bottomSheetViewProfessor);

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
        String email=user.getEmail();

        if(email.contains("@studenti.uniba.it")) {
            reference = FirebaseDatabase.getInstance ().getReference ("students");
            setBottomButtonsStudent(bottomSheetView);
        }
        else{
            reference = FirebaseDatabase.getInstance ().getReference ("teachers");
            setBottomButtonsProfessor(bottomSheetViewProfessor);
        }



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new Profile());
        navigation.setSelectedItemId(R.id.myunijet_tab);
    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.myunijet_tab:
                    fragment = new Profile();
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

    }

    private void setBottomButtonsProfessor(View view) {
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
                startActivity(new Intent (MainActivity.this,CreateCourse.class));
            }
        });


    }

}