package com.legs.unijet.tabletversion.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.legs.unijet.tabletversion.course.CourseSample;
import com.legs.unijet.tabletversion.courseDetailsActivity.CourseDetailsActivity;
import com.legs.unijet.tabletversion.group.Group;
import com.legs.unijet.tabletversion.group.GroupAdapter;
import com.legs.unijet.tabletversion.groupDetailsActivity.GroupActivity;
import com.legs.unijet.tabletversion.utils.RecyclerItemClickListener;
import com.legs.unijet.smartphone.R;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {
    ImageView item;
    EditText searchEditText;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private ArrayList<CourseSample> fullSampleList;
    private ArrayList <Group> groups;
    private ArrayList <String> members;
    private boolean isPrivate;
    static boolean isSinglePane = true;
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
        item = (ImageView) view.findViewById(R.id.groups_search_button);

        searchEditText = (EditText) view.findViewById(R.id.groups_search_edit_text);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setVisibility(View.VISIBLE);
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

        return view;

    }


    private void populateList() {
        groups = new ArrayList<>();
        fullSampleList = new ArrayList<>();

        db.child("groups").addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    String name = childSnapshot.child("name").getValue(String.class);
                    String owner = childSnapshot.child("author").getValue(String.class);
                    String department = childSnapshot.child("department").getValue(String.class);

                    groups.add(new Group(name, owner, members, department, isPrivate));

                }
                buildRecyclerView();

                    }





            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        fullSampleList = new ArrayList();
        db.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullSampleList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                        for (Group group : groups) {
                            if (childSnapshot.child("department").getValue(String.class).equals(group.getDepartment())) {
                                String namesString = group.getName();
                                //TI ODIO + " " + childSnapshot.child("academicYear").getValue(String.class) ;
                                String mail = group.getAuthor();

                                fullSampleList.add(new CourseSample(namesString, mail));
                            }

                        }
                    }
                    buildRecyclerView();
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

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

                        Bundle bundle = new Bundle();
                        bundle.putString("GName", mAdapter.returnTitle(position));
                        bundle.putString("owner", mAdapter.returnOwner(position));

                        if (isSinglePane) {
                            Fragment fragment;
                            fragment = new GroupActivity();
                            fragment.setArguments(bundle);
                            if (searchEditText.getVisibility() == View.VISIBLE) {
                                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(
                                        getContext().INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.commit();
                        } else {
                            getChildFragmentManager().findFragmentById(R.id.fragment_container);

                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //non c'è bisogno
                    }

                })
        );
    }


}