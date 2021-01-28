package com.legs.unijet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.legs.unijet.createGroupActivity.UserSample;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity {

   FirebaseUser user;

    String userId;
    EditText setname;
    //RecyclerView members;
    Button confirm;
    TextView addedMembers;

    DataSnapshot snapshot;
    DatabaseReference db;
    User userProfile = snapshot.getValue (User.class);
    ArrayList<UserSample> membersAdded;
    Intent intent;

    private ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        Bundle b = getIntent().getExtras();
        membersAdded = (ArrayList<UserSample>) b.getSerializable("members");

        setContentView (R.layout.create_group);

        user = FirebaseAuth.getInstance ().getCurrentUser ();

        LoadingBar = new ProgressDialog (CreateGroup.this);

        assert user != null;
        userId=user.getUid ();

        setname = findViewById (R.id.set_name_group);
        confirm = findViewById (R.id.confirm_button);

        addedMembers = findViewById(R.id.actually_added_members);
        addedMembers.setText(new StringBuilder().append(membersAdded.size()).append(" ").append(getString(R.string.members_added)).toString());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCrededentials();
            }
        });

    }

    void checkCrededentials() {

        String name = setname.getText ().toString ();
        if (name.isEmpty ()) {
            showError (setname, getString(R.string.group_name_error));
        } else {
            db = FirebaseDatabase.getInstance ().getReference ("groups").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());

            String email = user.getEmail();
            ArrayList<String> usersMails = new ArrayList<>();
            for (int i = 0; i < membersAdded.size(); i++) {
                usersMails.add(membersAdded.get(i).getText2());
            }

            Group group= new Group (name, userProfile.getDipartimento(), email, usersMails);
            Log.d ("TAG", "gruppo Creato: " + group.getName ());
            Toast.makeText (this, "success", Toast.LENGTH_SHORT).show ();
            db.setValue (group);

            LoadingBar.setTitle (getString (R.string.course_creation));
            LoadingBar.setMessage (getString(R.string.check_credentials));
            LoadingBar.setCanceledOnTouchOutside (false);

        }


    }

    private void showError(EditText input, String s) {
        input.setError (s);
        input.requestFocus ();

    }



}

