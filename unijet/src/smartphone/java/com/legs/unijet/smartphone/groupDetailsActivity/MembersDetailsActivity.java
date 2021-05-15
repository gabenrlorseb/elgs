package com.legs.unijet.smartphone.groupDetailsActivity;

import android.os.Bundle;

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
import com.legs.unijet.smartphone.createGroupActivity.UserChecklistSample;
import com.legs.unijet.smartphone.profile.User;

import java.util.ArrayList;

public class MembersDetailsActivity extends AppCompatActivity {

    private ArrayList<UserChecklistSample> names;
    private ArrayList<String> passed_names;
    private ArrayList<String> nameowner;
    private String passed_names1;
    RecyclerView mRecyclerView;
    DatabaseReference db = FirebaseDatabase.getInstance ().getReference ();
    FirebaseUser user = FirebaseAuth.getInstance ().getCurrentUser ();
    String authorName;


    String authorMail;


    @Override
    public String toString() {
        return "" + authorName ;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {

        setContentView (R.layout.simple_activity_recyclerview_list);

        Toolbar actionBar = findViewById (R.id.toolbar);
        actionBar.setTitle ("Members");

        Bundle args = getIntent ().getExtras ();

        if (user == null) {
            passed_names = getIntent ().getExtras ().getStringArrayList ("nameless");
            System.out.println ("passed_names:" + passed_names);
            System.out.println ("authorName" + authorName);
            System.out.println ("prova2");
            authorMail = args.getString ("author");
            System.out.println ("authorMail:" + authorMail);

            authorName= String.valueOf (args.getStringArrayList ("nameowner"));
            authorName = authorName.replaceAll("[\\[\\](){}]","");



            //authorName = getString (R.string.admin);
            System.out.println ("Print di nameowners::"+authorName);

                      //authorName = args.getString("nameowner");
                      authorMail = args.getString("author");







        } else {
            passed_names = (ArrayList<String>) args.getSerializable ("groupRecipients");
            System.out.println ("passed_names:" + passed_names);
            authorName = args.getString("author_name");
            System.out.println ("authorName" + authorName);
            if (authorName.equals ("you")) {
                System.out.println ("prova1");
                authorName = getString (R.string.you);
                authorMail = getString (R.string.admin);
            } else {
                System.out.println ("prova2");
            authorName = args.getString("author_name");
            authorMail = args.getString("author");
        }

        }



        populateList ();
        super.onCreate (savedInstance);

    }

    private void populateList() {
        names = new ArrayList<>();
        names.add(new UserChecklistSample(R.drawable.ic_people, authorName, authorMail, false, authorName));

        db.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                    if (passed_names.contains(childSnapshot.child("email").getValue(String.class))){
                        String namesString = childSnapshot.child("name").getValue(String.class) +
                                " " +
                                childSnapshot.child("surname").getValue(String.class);

                        String mail = childSnapshot.child ("email").getValue (String.class);
                        names.add (new UserChecklistSample(R.drawable.ic_people,namesString, mail, false, childSnapshot.getKey()));
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
        mRecyclerView = findViewById(R.id.recyclerview_details);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        MemberNoChecklistAdapter mAdapter = new MemberNoChecklistAdapter(names);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}
