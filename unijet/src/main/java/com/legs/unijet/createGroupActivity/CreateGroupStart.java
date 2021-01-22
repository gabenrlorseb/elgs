package com.legs.unijet.createGroupActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.R;

import java.util.ArrayList;

public class CreateGroupStart extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference db= FirebaseDatabase.getInstance ().getReference ();

    ArrayList<userSample> names=new ArrayList<>();

    RecyclerView mRecyclerView;
    MemberAdapter mAdapter;



    @Override
    protected void onCreate (Bundle savedInstance) {
        super.onCreate(savedInstance);
         setContentView(R.layout.create_group_search_section);

        user = FirebaseAuth.getInstance ().getCurrentUser ();
        assert user != null;

        populateList();

    }

    private void populateList() {
        names.clear ();
        db.child ("students").addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()) {
                    if(!user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                        String namesString = childSnapshot.child("name").getValue(String.class) +
                                " " +
                                childSnapshot.child("surname").getValue(String.class);
                        String mail = childSnapshot.child ("email").getValue (String.class);
                        names.add (new userSample(R.drawable.ic_people, namesString, mail));
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);


        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("well", newText);
                mAdapter.getFilter().filter(newText);
                return false;
            }

        });
        return  super.onPrepareOptionsMenu(menu);
    }

}
