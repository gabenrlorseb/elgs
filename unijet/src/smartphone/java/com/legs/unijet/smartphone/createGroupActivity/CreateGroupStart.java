package com.legs.unijet.smartphone.createGroupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.smartphone.course.Course;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.profile.User;

import java.util.ArrayList;

public class CreateGroupStart extends AppCompatActivity  {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db= FirebaseDatabase.getInstance ().getReference ();

    private ArrayList<UserChecklistSample> names;
    private ArrayList<Course> courses;


    RecyclerView mRecyclerView;
    private MemberCheckListAdapter mAdapter;

    EditText searchEditText;
    DatabaseReference userReference;


    @Override
    protected void onCreate (Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.create_group_search_section);
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

        final FloatingActionButton fab = findViewById(R.id.continue_button);
        //fab.setVisibility(View.GONE);
        //final Animation appearFab = AnimationUtils.loadAnimation(this, R.anim.fab_show);
        //final Animation disappearFab = AnimationUtils.loadAnimation(this, R.anim.fade_scale_anim_reverse);

        mRecyclerView = findViewById(R.id.possible_members_list);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getCheckedUsers().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You Haven't Selected a member", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<UserChecklistSample> addedMembersSendList;
                    addedMembersSendList = mAdapter.getCheckedUsers();

                    ArrayList<String> membersMails;
                    membersMails = mAdapter.getCheckedMails();

                    Bundle b = new Bundle();
                    b.putSerializable("members", addedMembersSendList);
                    b.putSerializable("mails", membersMails);

                    Intent i = new Intent(CreateGroupStart.this, CreateGroup.class);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                }
            }
        });

    /* private ArrayList<String> getNamesToBeAdded() {
        ArrayList<String> uuidList;

        return uuidList;
    }*/
    }

    private void populateList() {
        final User[] userProfile = new User[1];
        String reference = null;
        if (user.getEmail().contains("@uniba.it")){
        reference = "teachers" ;
        userReference = FirebaseDatabase.getInstance ().getReference (reference);
        } else if (user.getEmail().contains("@studenti.uniba.it")){
            reference = "students" ;
            userReference = FirebaseDatabase.getInstance ().getReference (reference);
        }

        userReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile[0] = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        names = new ArrayList<>();
        db.child (reference).addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()) {
                    if(!(user.getEmail().equals(childSnapshot.child("email").getValue(String.class)) ) && userProfile[0].getDepartment().equals(childSnapshot.child("department").getValue(String.class))) {
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
        mAdapter = new MemberCheckListAdapter(names);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}
