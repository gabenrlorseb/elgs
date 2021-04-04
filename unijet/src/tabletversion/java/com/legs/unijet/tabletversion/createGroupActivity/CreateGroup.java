package com.legs.unijet.tabletversion.createGroupActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.legs.unijet.tabletversion.group.Group;
import com.legs.unijet.tabletversion.utils.MainActivity;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.profile.User;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db= FirebaseDatabase.getInstance().getReference();


    String userId;
    EditText setname;
    //RecyclerView members;
    ExtendedFloatingActionButton confirm;
    TextView addedMembers;
    CheckBox makePrivate;

    DataSnapshot snapshot;
    String userProfile;
    String department;
    ArrayList<UserChecklistSample> membersAdded;
    ArrayList<String> addedMails;
    ArrayList<User> students;
    Intent intent;

    final StringBuilder membersEmailList = new StringBuilder();


    ImageView infoImageView;
    TextView infoTextView;

    private ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        Bundle b = getIntent().getExtras();
        department = b.getString("department");
        membersAdded = (ArrayList<UserChecklistSample>) b.getSerializable("members");
        addedMails = (ArrayList<String>) b.getSerializable("mails");


        setContentView (R.layout.create_group);


        LoadingBar = new ProgressDialog (CreateGroup.this);


        assert user != null;
        userId=user.getUid ();

        setname = findViewById (R.id.set_name_group);
        confirm = findViewById (R.id.confirm_button);
        makePrivate = findViewById(R.id.private_group_checkbox);

        infoImageView = findViewById(R.id.info_imageview);
        infoTextView = findViewById(R.id.info_textview);

        addedMembers = findViewById(R.id.actually_added_members);
        addedMembers.setText(new StringBuilder().append(membersAdded.size()).append(" ").append(getString(R.string.members_added)).toString());

        for (int i = 0; i < addedMails.size(); i++) {
            String addThis = addedMails.get(i);
            membersEmailList.append(addThis);
            if (i != addedMails.size() - 1 ) {
                membersEmailList.append(",");
            }
        }

        addedMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedMembers.setText(membersEmailList);
            }
        });

        makePrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (makePrivate.isChecked()){
                    infoImageView.setVisibility(View.VISIBLE);
                    infoTextView.setVisibility(View.VISIBLE);
                } else {
                    infoImageView.setVisibility(View.GONE);
                    infoTextView.setVisibility(View.GONE);
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });

    }

    void createGroup() {
        DatabaseReference userReference = FirebaseDatabase.getInstance ().getReference ();
        final String name = setname.getText ().toString ();
        if (name.isEmpty ()) {
            showError (setname, getString(R.string.group_name_error));
        } else {
            final User[] thisUser = new User[1];
            final String email = user.getEmail();
            String reference = null;
            if (user.getEmail().contains("@uniba.it")){
                reference = "teachers" ;
                userReference = FirebaseDatabase.getInstance ().getReference (reference);
            } else if (user.getEmail().contains("@studenti.uniba.it")){
                reference = "students" ;
                userReference = FirebaseDatabase.getInstance ().getReference (reference);
            }

            userReference = FirebaseDatabase.getInstance ().getReference (reference);
            userReference.orderByChild("email").equalTo(user.getEmail()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    thisUser[0] = snapshot.getValue(User.class);
                    Boolean newBoolean = makePrivate.isChecked();
                    Group group = new Group (name, email, addedMails, thisUser[0].getDepartment(), newBoolean);
                    DatabaseReference dr = FirebaseDatabase.getInstance ().getReference ("groups");
                    dr.push().setValue(group, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Data could not be saved " + databaseError.getMessage());
                                Toast.makeText (CreateGroup.this, "ERROR", Toast.LENGTH_SHORT).show ();
                            } else {
                                DatabaseReference dr2 = FirebaseDatabase.getInstance ().getReference ("members").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());

                                Toast.makeText (CreateGroup.this, "success", Toast.LENGTH_SHORT).show ();
                                startActivity(new Intent(CreateGroup.this, MainActivity.class));
                            }
                        }
                    });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });









        }


    }



    private void showError(EditText input, String s) {
        input.setError (s);
        input.requestFocus ();

    }



}

