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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.smartphone.course.Course;
import com.legs.unijet.smartphone.group.Group;
import com.legs.unijet.smartphone.groupDetailsActivity.MembersDetailsActivity;
import com.legs.unijet.smartphone.profile.User;
import com.legs.unijet.smartphone.project.Project;
import com.legs.unijet.smartphone.project.ProjectAdapter;
import com.legs.unijet.smartphone.project.ProjectDetailsActivity;
import com.legs.unijet.smartphone.project.ProjectSample;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.utils.RecyclerItemClickListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class ProjectsFragment extends Fragment {
    ImageView item;
    EditText searchEditText;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase mdb;
    DatabaseReference db1;

    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private ArrayList<Project> projects;
    private ArrayList<ProjectSample> projectList;
    DatabaseReference db = FirebaseDatabase.getInstance ().getReference ();
    DatabaseReference db2 = FirebaseDatabase.getInstance ().getReference ();
    RecyclerView mRecyclerView;
    private ProjectAdapter mAdapter;

    TextView notFoundTextView;
    RelativeLayout notFoundLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        final android.view.View view = inflater.inflate (R.layout.projects_page, container, false);
        populateList ();

        item = view.findViewById(R.id.projects_search_button);

        searchEditText = view.findViewById(R.id.projects_search_edit_text);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setVisibility(View.VISIBLE);
            }
        });

        notFoundTextView = view.findViewById(R.id.not_found_textview);
        notFoundLayout = view.findViewById(R.id.not_found);


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
        projects = new ArrayList<Project>();


        db.child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //projects.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue(String.class);
                    String course = childSnapshot.child("course").getValue(String.class);
                    String group = childSnapshot.child("group").getValue(String.class);
                    projects.add(new Project(name, course, group));

                }

                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(user==null){
            ViewProject();

        }

        else   if (user.getEmail().contains("@studenti.uniba.it")) {

            projectList = new ArrayList();
            db.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot){
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        final Group group = childSnapshot.getValue(Group.class);
                        db2.child("students").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {

                                        for (Project project : projects) {
                                            if((childSnapshot.child("email").getValue(String.class).equals(group.getAuthor())
                                                    || group.getRecipients().contains(childSnapshot.child("email").getValue(String.class)))
                                                    && project.getGroup().equals(group.getName()))
                                            {
                                                Group group=new Group ();
                                                String namesString = project.getName();
                                                String mail = project.getGroup();

                                                projectList.add(new ProjectSample(namesString, mail));
                                            }


                                        }
                                    }
                                    buildRecyclerView();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
                @Override
                public void onCancelled (@NonNull DatabaseError error){

                }
            });
        }
        else if (user.getEmail().contains("@uniba.it")) {
            projectList = new ArrayList();
            db.child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot){
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        final Course course = childSnapshot.getValue(Course.class);
                        db2.child("teachers").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                                        for (Project project : projects) {
                                            if (childSnapshot.child("email").getValue(String.class).equals(course.getEmail())
                                                    && project.getCourse().equals(course.getName()))

                                            {
                                                Group group =new Group ();
                                                String namesString = project.getName();
                                                String mail = project.getGroup();
                                                projectList.add(new ProjectSample(namesString, mail));
                                            }

                                        }
                                    }
                                    buildRecyclerView();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
                @Override
                public void onCancelled (@NonNull DatabaseError error){

                }
            });
        }



    }

    private void fragmentStudent(){

    }

    private void fragmentProfessor(){

    }

    private void buildRecyclerView() {
        mRecyclerView = getView ().findViewById (R.id.projects_list);
        mRecyclerView.setHasFixedSize (true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getContext ());
        mAdapter = new ProjectAdapter (projectList);
        mRecyclerView.setLayoutManager (mLayoutManager);
        mRecyclerView.setAdapter (mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if (user == null) {
                            final Intent i = new Intent (view.getContext (), MembersDetailsActivity.class);
                            String name;
                            ArrayList<String>  mail;
                            String namegroup;
                            ArrayList<String> namepass;
                            ArrayList<String> nameOwners;
                            i.putExtra ("authorMail", mail = mAdapter.returnMailA (position));
                            i.putExtra ("name", name = mAdapter.returnTitle (position));
                            i.putExtra ("nameless", namepass =mAdapter.returnReci (position));
                           // i.putExtra ("author", namegroup = mAdapter.returnGroup (position));
                            System.out.println(" le mail:"+mail);
                            Log.d (TAG, "onDataChange:d " + i);
                            i.putExtra ("nameowner", nameOwners =mAdapter.returnNameOwner (position));

                            System.out.println ("membri:" + namepass);


                            Log.d (TAG, "onDataChange:d " + i);
                            view.getContext ().startActivity (i);

                        }else {
                            Intent i = new Intent (view.getContext (), ProjectDetailsActivity.class);
                            i.putExtra ("titleName", mAdapter.returnTitle (position));
                            i.putExtra ("group", mAdapter.returnGroup (position));
                            view.getContext ().startActivity (i);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //non c'è bisogno
                    }

                })
        );
        if (projects.isEmpty()) {
            notFoundLayout.setVisibility(View.VISIBLE);
            String[] notFoundStrings = getResources().getStringArray(R.array.not_found_strings);
            int randomIndex = new Random().nextInt(notFoundStrings.length);
            String randomName = notFoundStrings[randomIndex];
            notFoundTextView.setText(randomName);
        }
    }
    public void ViewProject() {

        projectList = new ArrayList();
        mdb=FirebaseDatabase.getInstance ();
        db=mdb.getReference ("projects");
        db1=mdb.getReference ("groups");
        db2=mdb.getReference ("students");



        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectList.clear();
                List<String> projects=new ArrayList<> ();
                final ArrayList<String>nameOwners=new ArrayList<> ();

                final String[] mailU = new String[1];
                final String[] nameO = new String[1];

                final List<String> members=new ArrayList<> ();
                final String[] autor1 = new String[1];

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    projects.add(childSnapshot.getKey ());
                    final Project project = childSnapshot.getValue (Project.class);
                    final String nameProjects = project.getName ();

                    final String nameGroups = project.getGroup ();
//System.out.println (""+mail);

                    final ArrayList<String>[] reci = new ArrayList[]{new ArrayList ()};

                    final ArrayList<String> finalNameOWners = new ArrayList<> ();
                    final ArrayList<String> finalMailsOWners = new ArrayList<> ();

                    final String[] autor  = new String[5];
                    db1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren ()) {
                                members.add(childSnapshot.getKey ());
                                System.out.println ("members:"+members);
                                Group group = childSnapshot.getValue (Group.class);
                                String namegroup=group.getName ();

                                System.out.println ("namegroup:"+namegroup);
                                System.out.println ("nameGroups:"+nameGroups);
                                if (nameGroups.equals (namegroup) && !group.getPrivate()) {
                                    System.out.println ("entrato");
                                    ArrayList<String> membri = group.getRecipients ();

                                    autor[0] = group.getAuthor ();
                                    System.out.println ("autor[0]=" + autor[0]);

                                    reci[0].addAll (membri);
                                }
                                System.out.println ("::" + reci[0]);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    db2.addValueEventListener (new ValueEventListener () {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                nameOwners.add (childSnapshot.getKey ());
                                User user = childSnapshot.getValue (User.class);
                                mailU[0] =user.getEmail ();

                                System.out.println ("mailU[0]:" + mailU[0]);

                                if (mailU[0].equals (autor[0])) {
                                    nameO[0] = user.getName () + ( " " ) + user.getSurname ();
                                    autor1[0] =user.getEmail();
                                    finalNameOWners.add(nameO[0]);
                                    finalMailsOWners.add( autor1[0]);


                                }
                                System.out.println ("nameOwners:" + finalNameOWners);
                                System.out.println ("il autor[0]:" +  autor[0]);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    projectList.add (new ProjectSample (nameProjects, nameGroups, reci[0], finalMailsOWners,finalNameOWners));
                    System.out.println ("autore=" + autor[0]);

                    //System.out.println ("memgri" + reci[0]);


                    // System.out.println ("ProjectList:"+projectList);
                    buildRecyclerView();

                }

                Log.d (TAG, "onDataChange:d "+projectList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



    }


}