package com.legs.unijet.smartphone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.legs.unijet.smartphone.group.Group;
import com.legs.unijet.smartphone.group.GroupAdapter;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.course.CourseSample;
import com.legs.unijet.smartphone.groupDetailsActivity.GroupActivity;
import com.legs.unijet.smartphone.groupDetailsActivity.MembersDetailsActivity;
import com.legs.unijet.smartphone.profile.User;
import com.legs.unijet.smartphone.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class GroupsFragment extends Fragment {
    ImageView item;
    EditText searchEditText;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    FirebaseUser auth;
    FirebaseDatabase mdb;
    Group group;
    DatabaseReference reference;
    private ArrayList<CourseSample> fullSampleList;
    private ArrayList <Group> groups;
    private ArrayList<String> members;
    ArrayList<String> reci;

    DatabaseReference db = FirebaseDatabase.getInstance ().getReference ();
    DatabaseReference db1 = FirebaseDatabase.getInstance ().getReference ();
    RecyclerView mRecyclerView;
    private GroupAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView notFoundTextView;
    RelativeLayout notFoundLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate(R.layout.groups_page, container, false);

        populateList ();

        item = view.findViewById(R.id.groups_search_button);

        searchEditText = view.findViewById(R.id.groups_search_edit_text);

        notFoundTextView = view.findViewById(R.id.not_found_textview);
        notFoundLayout = view.findViewById(R.id.not_found);

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




                    Boolean isPrivate = childSnapshot.child("private").getValue(Boolean.class);
                    String name = childSnapshot.child("name").getValue(String.class);
                    String owner = childSnapshot.child("author").getValue(String.class);
                    String department = childSnapshot.child("department").getValue(String.class);
                    ArrayList<String> recipients = new ArrayList<>();


                    if (isPrivate && user != null) {
                        for (DataSnapshot users : childSnapshot.child("recipients").getChildren()) {
                            if (users.getValue(String.class).equals(user.getEmail())) {
                                groups.add(new Group(name, owner, members, department, true));
                                break;
                            }
                        }
                    } else if (isPrivate || user == null) {
                        break;
                    } else {
                        groups.add(new Group(name, owner, members, department, isPrivate));
                    }

                }
                buildRecyclerView();

            }





            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        if (user==null){
            ViewGroup ();
        }

        else if (user.getEmail().contains("@studenti.uniba.it")){
            fragmentStudent();
        } else if (user.getEmail().contains("@uniba.it")){
            fragmentProfessor();
        }

    }


    public void fragmentProfessor(){
        fullSampleList = new ArrayList();
        db.child("teachers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullSampleList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                        for (Group group : groups) {
                            if (childSnapshot.child("department").getValue(String.class).equals(group.getDepartment())
                                    && group.getAuthor().contains("@uniba.it")) {
                                String namesString = group.getName();
                                //TI ODIO + " " + childSnapshot.child("academicYear").getValue(String.class) ;
                                String mail = group.getAuthor();

                                fullSampleList.add (new CourseSample (namesString, mail));
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
    public void fragmentStudent(){
        fullSampleList = new ArrayList();
        db.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullSampleList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                        for (Group group : groups) {
                            if (childSnapshot.child("department").getValue(String.class).equals(group.getDepartment())
                                    && group.getAuthor().contains("@studenti.uniba.it")) {
                                String namesString = group.getName();

                                String mail = group.getAuthor();

                                fullSampleList.add (new CourseSample (namesString,mail));
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
                    @Override
                    public void onItemClick(final View view, final int position) {
                        if (user == null) {
                            final Intent i = new Intent (view.getContext (), MembersDetailsActivity.class);
                            String name;
                            ArrayList<String> mail;
                            ArrayList<String> namepass;
                            ArrayList<String> nameowner;

                            i.putExtra ("authorMail", mail = mAdapter.returnOwner (position));
                            Log.d (TAG, "onDataChange:d " + i);
                            i.putExtra ("name", name = mAdapter.returnTitle (position));

                            i.putExtra ("nameless", namepass =mAdapter.returnReci (position));
                            i.putExtra ("nameowner", nameowner =mAdapter.returnNameOwner (position));
                            System.out.println ("membri:" + namepass);


                            Log.d (TAG, "onDataChange:d " + i);
                            view.getContext ().startActivity (i);

                        }
                        else {
                            Intent i = new Intent (view.getContext (), GroupActivity.class);
                            i.putExtra ("titleName", mAdapter.returnTitle (position));
                            i.putExtra ("owner", mAdapter.returnOwner (position));
                            view.getContext().startActivity(i);
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //non c'è bisogno
                    }

                })
        );
        if (groups.isEmpty()) {
            if (user==null){

            }else {
                notFoundLayout.setVisibility (View.VISIBLE);
                String[] notFoundStrings = getResources ().getStringArray (R.array.not_found_strings);
                int randomIndex = new Random ().nextInt (notFoundStrings.length);
                String randomName = notFoundStrings[randomIndex];
                notFoundTextView.setText (randomName);
            }
        }
    }
    public void ViewGroup() {
        fullSampleList = new ArrayList();
        mdb=FirebaseDatabase.getInstance ();
        db=mdb.getReference ("groups");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String>groups=new ArrayList<> ();

                final ArrayList<String>nameOwners=new ArrayList<> ();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    groups.add(childSnapshot.getKey ());

                    group = childSnapshot.getValue (Group.class);
                    if(group.getPrivate ()==false&& group.getAuthor ().contains ("@studenti.uniba.it")) {
                        String name = group.getName ();
                        final String[] mailU = new String[1];
                        final String[] autor = {group.getAuthor()};

                        reci = group.getRecipients ();
                        System.out.println ("::"+reci);
                        db1 = mdb.getReference ("students");


                        final String[] nameO = {""};

                        final ArrayList<String> finalNameOWners = new ArrayList<> ();
                        final ArrayList<String> finalMailOWners = new ArrayList<> ();
                        db1.addValueEventListener (new ValueEventListener () {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    nameOwners.add (childSnapshot.getKey ());
                                    User user = childSnapshot.getValue (User.class);
                                    mailU[0] =user.getEmail ();

                                    if (mailU[0].equals (autor[0])) {
                                        nameO[0] = user.getName () + ( " " ) + user.getSurname ();
                                        autor[0] =user.getEmail();
                                        finalNameOWners.add(nameO[0]);
                                        finalMailOWners.add( autor[0]);

                                    }
                                    System.out.println ("nameOwners:" + finalNameOWners);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        fullSampleList.add (new CourseSample (name,  autor[0],finalMailOWners,reci, finalNameOWners));
                    }
                    System.out.println ("-:"+fullSampleList);
                    buildRecyclerView();
                }

                Log.d (TAG, "onDataChange:d "+fullSampleList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



    }



}