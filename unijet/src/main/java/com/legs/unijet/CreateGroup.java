package com.legs.unijet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.createGroupActivity.UserSample;

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
    ArrayList<UserSample> membersAdded;
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
        department = groupDepartment();
        department = (String) b.getString("department");
        membersAdded = (ArrayList<UserSample>) b.getSerializable("members");
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
                checkCrededentials();
            }
        });

    }

    void checkCrededentials() {

        String name = setname.getText ().toString ();
        if (name.isEmpty ()) {
            showError (setname, getString(R.string.group_name_error));
        } else {

            String email = user.getEmail();


            Group group= new Group (name, email, addedMails, department, makePrivate.isChecked());
            Log.d ("TAG", "gruppo Creato: " + group.getName ());
            Toast.makeText (this, "success", Toast.LENGTH_SHORT).show ();

            DatabaseReference dr = FirebaseDatabase.getInstance ().getReference ("groups").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());
            dr.setValue(group);

            LoadingBar.setTitle (getString (R.string.course_creation));
            LoadingBar.setMessage (getString(R.string.check_credentials));
            LoadingBar.setCanceledOnTouchOutside (false);

            startActivity(new Intent(CreateGroup.this, MainActivity.class));

        }


    }

    public String groupDepartment(){
        students = new ArrayList<>();
        final String[] department = new String[1];
        db.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if(user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                        String name = childSnapshot.child("name").getValue(String.class);
                        String surname = childSnapshot.child("surname").getValue(String.class);
                        String ID = childSnapshot.child("id").getValue(String.class);
                        String department = childSnapshot.child("department").getValue(String.class);
                        String universityCampus = childSnapshot.child("universityCampus").getValue(String.class);
                        String gender = childSnapshot.child("gender").getValue(String.class);
                        String dateBorn = childSnapshot.child("dateBorn").getValue(String.class);
                        String email = childSnapshot.child("email").getValue(String.class);
                        students.add(new User(name, surname, ID, department, universityCampus, gender, dateBorn, email));
                    }
                    if (user.getEmail().equals(childSnapshot.child("email").getValue(String.class))) {
                        for (User student: students) {
                            String mDepartment = student.getDepartment();
                            department[0] = mDepartment;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
      return department[0];
    }

    private void showError(EditText input, String s) {
        input.setError (s);
        input.requestFocus ();

    }



}

