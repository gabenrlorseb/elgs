package com.legs.unijet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateGroup extends AppCompatActivity {
   FirebaseUser user;
    String userId;
    DatabaseReference db=FirebaseDatabase.getInstance ().getReference ();
    EditText setname;
    //RecyclerView members;
    Button confirm;
    CustomAdapter adapter;
    Spinner setmember;
    List<String> names=new ArrayList<> ();
    ArrayList<String> addNames=new ArrayList<> ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.create_group);

        setmember=findViewById (R.id.set_member_1);
        user = FirebaseAuth.getInstance ().getCurrentUser ();
        userId=user.getUid ();
             showDataSpinner();

        // = findViewById (R.id.set_name_group);
        confirm = findViewById (R.id.confirm_button);
        final RecyclerView  members = findViewById (R.id.members);
        members.setAdapter (new CustomAdapter());


        setmember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String toBeAdded =setmember.getSelectedItem ().toString ();
                members.notify ();
                addNames.add(toBeAdded);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
       /* String email = "ciao.seddd@stud.it";
        String temp = email.split ("@")[0];
        temp = email.replace (".","_");*/



        /*confirm.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
              //go();
            }
        });*/

    }

    private void showDataSpinner() {
        names.clear ();
        db.child ("students").addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot chilSnapshot:dataSnapshot.getChildren()) {
                      // if(user.getEmail () != dataSnapshot)) {
                            names.add (chilSnapshot.child ("email").getValue (String.class));
                        //}
                        }
                ArrayAdapter<String>arrayAdapter=new ArrayAdapter<> (CreateGroup.this, R.layout.style_spinner,names);
                setmember.setAdapter (arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*void go() {
        String name = setname.getText ().toString ();
        String member = setmember
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();


                }
*/



}

