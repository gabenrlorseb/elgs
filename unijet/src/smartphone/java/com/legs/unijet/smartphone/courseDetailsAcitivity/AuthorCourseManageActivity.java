package com.legs.unijet.smartphone.courseDetailsAcitivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.course.Course;
import com.legs.unijet.smartphone.createGroupActivity.UserChecklistSample;

import java.util.ArrayList;

public class AuthorCourseManageActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db= FirebaseDatabase.getInstance ().getReference ();

    private ArrayList<UserChecklistSample> names;
    private ArrayList<Course> courses;
    private ArrayList<String> passed_names;
    String authorName;
    String authorMail;
    final StringBuilder membersEmailList = new StringBuilder();

    RecyclerView mRecyclerView;
    private AuthorCourseManageAdapter mAdapter;

    EditText searchEditText;


    @Override
    protected void onCreate (Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.remove_members);
        Toolbar actionBar = findViewById(R.id.toolbar);
        actionBar.setTitle("Members");

        final Bundle args = getIntent().getExtras();

        passed_names =  (ArrayList<String>) args.getSerializable("groupRecipients");
        authorName = args.getString("author_name");

        if (authorName.equals("you")) {
            authorName = getString(R.string.you);
            authorMail = getString(R.string.admin);
        } else {
            authorName = args.getString("author_name");
            authorMail = args.getString("author");
        }

        populateList();



        final Toolbar toolbar = findViewById(R.id.toolbar);
        Menu menu = toolbar.getMenu();
        final MenuItem item = menu.findItem(R.id.action_search);

        searchEditText = findViewById(R.id.search_edit_text);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                searchEditText.setVisibility(View.VISIBLE);
                return true;
            }
        });


        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                mAdapter.getFilter().filter(s);
                mAdapter.notifyDataSetChanged();
            }
        });
        Button fab = findViewById(R.id.remove_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.child("courses").orderByChild("name").equalTo(args.getString("name")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            final String courseUID = childSnapshot.getKey();
                            Course course;
                            course = childSnapshot.getValue(Course.class);
                            ArrayList<String> courseSubscribers = course.getMembers();
                            //fab.setVisibility(View.GONE);
                            //final Animation appearFab = AnimationUtils.loadAnimation(this, R.anim.fab_show);
                            //final Animation disappearFab = AnimationUtils.loadAnimation(this, R.anim.fade_scale_anim_reverse);

                            mRecyclerView = findViewById(R.id.possible_members_list);
                            if (mAdapter.getCheckedUsers().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "You Haven't Selected a member", Toast.LENGTH_LONG).show();
                            } else {
                                ArrayList<String> removeMail = mAdapter.getCheckedMails();
                                for (String string: removeMail) {
                                    courseSubscribers.remove(string);

                                db.child("courses").child(courseUID).child("members").setValue(courseSubscribers);
                                Intent i = new Intent(AuthorCourseManageActivity.this, CourseDetailsActivity.class);
                                startActivity(i);
                                //finish();
                                }
                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });



    /* private ArrayList<String> getNamesToBeAdded() {
        ArrayList<String> uuidList;

        return uuidList;
    }*/
    }

    private void removeMembers(){

    }

    private void populateList() {
        names = new ArrayList<>();
        names.add(new UserChecklistSample(R.drawable.ic_people, authorName, authorMail, false));
        db.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                    if (passed_names.contains(childSnapshot.child("email").getValue(String.class)) ) {
                        String namesString = childSnapshot.child("name").getValue(String.class) +
                                " " +
                                childSnapshot.child("surname").getValue(String.class);
                        String mail = childSnapshot.child ("email").getValue (String.class);
                        names.add (new UserChecklistSample(R.drawable.ic_people, namesString, mail, false));
                    }
                }



                buildRecyclerView();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.possible_members_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AuthorCourseManageAdapter(names);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}

