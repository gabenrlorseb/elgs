package com.legs.unijet.createGroupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.legs.unijet.CreateGroup;
import com.legs.unijet.R;

import java.io.Serializable;
import java.util.ArrayList;

public class CreateGroupStart extends AppCompatActivity  {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db= FirebaseDatabase.getInstance ().getReference ();

    private ArrayList<UserSample> names;

    RecyclerView mRecyclerView;
    private MemberAdapter mAdapter;

    EditText searchEditText;


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
                    ArrayList<UserSample> members;
                    members = mAdapter.getCheckedUsers();

                    Bundle b = new Bundle();
                    b.putSerializable("members", members);

                    Intent i = new Intent(CreateGroupStart.this, CreateGroup.class);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });

    /* private ArrayList<String> getNamesToBeAdded() {
        ArrayList<String> uuidList;

        return uuidList;
    }*/
    }

    private void populateList() {
        names = new ArrayList<>();
        db.child ("students").addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()) {
                    if(!user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                        String namesString = childSnapshot.child("name").getValue(String.class) +
                                " " +
                                childSnapshot.child("surname").getValue(String.class);
                        String mail = childSnapshot.child ("email").getValue (String.class);
                        names.add (new UserSample(R.drawable.ic_people, namesString, mail, false));
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
        mAdapter = new MemberAdapter(names);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}
