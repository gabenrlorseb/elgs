package com.legs.unijet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.groupDetailsActivity.GroupActivity;
import com.legs.unijet.utils.RecyclerItemClickListener;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private ArrayList<CourseSample> fullSampleList;
    private ArrayList <Course> courses;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    RecyclerView mRecyclerView;
    private GroupAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate(R.layout.groups_page, container, false);
        populateList();

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.post(new Runnable() {

                    @Override
                    public void run() {

                        mSwipeRefreshLayout.setRefreshing(true);

                        populateList();
                    }
                });
            }
        });

        return view;

    }

    private void populateList() {
        courses = new ArrayList<>();
        fullSampleList = new ArrayList<>();

        db.child("groups").addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullSampleList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    //if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                    String namesString = childSnapshot.child("name").getValue(String.class);
                    String owner = childSnapshot.child("author").getValue(String.class);
                    fullSampleList.add(new CourseSample(namesString, owner));

                }
                buildRecyclerView();

                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
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
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i = new Intent(view.getContext(), GroupActivity.class);
                        i.putExtra("GName", mAdapter.returnTitle(position));
                        i.putExtra("owner", mAdapter.returnOwner(position));
                        view.getContext().startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //non c'è bisogno
                    }

                })
        );
    }


}
