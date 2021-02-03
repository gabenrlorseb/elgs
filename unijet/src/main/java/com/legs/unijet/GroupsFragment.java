package com.legs.unijet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.createGroupActivity.GroupAdapter;
import com.legs.unijet.createGroupActivity.MemberAdapter;
import com.legs.unijet.createGroupActivity.UserSample;
import com.legs.unijet.createGroupActivity.UserSample2;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {
    FirebaseUser group;
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private ArrayList<UserSample2> fullSampleList;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    RecyclerView mRecyclerView;
    private GroupAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate(R.layout.groups_page, container, false);
        populateList();

        return view;

    }

    private void populateList() {
        fullSampleList = new ArrayList<UserSample2> ();
        db.child("groups").addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                                String namesString = childSnapshot.child("name").getValue(String.class) +
                                        " " ;
                                fullSampleList.add(new UserSample2 (namesString));





                }
                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buildRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.groups_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getContext());
        mAdapter = new GroupAdapter (fullSampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
