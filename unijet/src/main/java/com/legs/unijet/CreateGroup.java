package com.legs.unijet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.legs.unijet.createGroupActivity.UserSample;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity {




   FirebaseUser user;
    String userId;
    DatabaseReference db=FirebaseDatabase.getInstance ().getReference ();
    EditText setname;
    //RecyclerView members;
    Button confirm;
    TextView addedMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        Bundle b = getIntent().getExtras();
        ArrayList<UserSample> membersAdded = (ArrayList<UserSample>) b.getSerializable("members");

        setContentView (R.layout.create_group);

        user = FirebaseAuth.getInstance ().getCurrentUser ();
        userId=user.getUid ();

        // = findViewById (R.id.set_name_group);
        confirm = findViewById (R.id.confirm_button);

        addedMembers = findViewById(R.id.actually_added_members);
        addedMembers.setText(String.valueOf(membersAdded.size()) + " " + getString(R.string.members_added));


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
}

