package com.legs.unijet.tabletversion.groupDetailsActivity;

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
import com.legs.unijet.tabletversion.courseDetailsActivity.AuthorCourseManageActivity;
import com.legs.unijet.tabletversion.group.Group;
import com.legs.unijet.tabletversion.course.Course;
import com.legs.unijet.tabletversion.createGroupActivity.CreateGroup;
import com.legs.unijet.tabletversion.createGroupActivity.CreateGroupStart;
import com.legs.unijet.tabletversion.groupDetailsActivity.AuthorGroupManageAdapter;
import com.legs.unijet.tabletversion.course.Course;
import com.legs.unijet.tabletversion.courseDetailsActivity.CourseDetailsActivity;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.createGroupActivity.UserChecklistSample;

import java.util.ArrayList;

public class AuthorGroupManageActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db= FirebaseDatabase.getInstance ().getReference ();

    private ArrayList<UserChecklistSample> names;
    private ArrayList<Course> courses;
    private ArrayList<String> passed_names;
    String authorName;
    String authorMail;
    final StringBuilder membersEmailList = new StringBuilder();

    RecyclerView mRecyclerView;
    private AuthorGroupManageAdapter mAdapter;

    EditText searchEditText;


    @Override
    protected void onCreate (Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.remove_members);
        Toolbar actionBar = findViewById(R.id.toolbar);
        actionBar.setTitle("Members");

        Bundle args = getIntent().getExtras();

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
        mRecyclerView = findViewById(R.id.possible_members_list);

db.child("groups").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Button fab = findViewById(R.id.remove_button);
            Group group;
            group = childSnapshot.getValue(Group.class);
            final ArrayList<String> groupSubscribers = group.getRecipients();
            //fab.setVisibility(View.GONE);
            //final Animation appearFab = AnimationUtils.loadAnimation(this, R.anim.fab_show);
            //final Animation disappearFab = AnimationUtils.loadAnimation(this, R.anim.fade_scale_anim_reverse);


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapter.removeCheckedUsers().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "You Haven't Selected a member", Toast.LENGTH_LONG).show();
                    } else {
                        ArrayList<UserChecklistSample> addedMembersSendList;
                        addedMembersSendList = mAdapter.removeCheckedUsers();

                        ArrayList<String> membersMails;
                        membersMails = mAdapter.removeCheckedMails();

                        groupSubscribers.remove(membersMails);

                        Intent i = new Intent(AuthorGroupManageActivity.this, CourseDetailsActivity.class);
                        startActivity(i);
                        finish();
                    }

                }

            });
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});


    /* private ArrayList<String> getNamesToBeAdded() {
        ArrayList<String> uuidList;

        return uuidList;
    }*/
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
        mAdapter = new AuthorGroupManageAdapter(names);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}