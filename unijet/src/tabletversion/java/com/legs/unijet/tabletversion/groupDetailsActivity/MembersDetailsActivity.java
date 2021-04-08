 package com.legs.unijet.tabletversion.groupDetailsActivity;

 import android.os.Bundle;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.Toolbar;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;
 import com.legs.unijet.smartphone.R;
 import com.legs.unijet.tabletversion.createGroupActivity.UserChecklistSample;

 import java.util.ArrayList;

 public class MembersDetailsActivity extends AppCompatActivity {

     private ArrayList<UserChecklistSample> names;
     private ArrayList<String> passed_names;

     RecyclerView mRecyclerView;
     DatabaseReference db= FirebaseDatabase.getInstance ().getReference ();

     String authorName;
     String authorMail;


     @Override
     protected void onCreate (Bundle savedInstance) {

         setContentView(R.layout.simple_activity_recyclerview_list);

         Toolbar actionBar = findViewById(R.id.toolbar);
         actionBar.setTitle("Members");

         Bundle args = getIntent().getExtras();

         passed_names =  (ArrayList<String>) args.getSerializable("groupRecipients");
         authorName = args.getString("author_name");

         if (authorName.equals("you")) {
             authorName = getString(R.string.you);
             authorMail = getString(R.string.admin);
         } else {
             authorName = args.getString("author_name");
             authorMail = args.getString("author");
         }

         populateList();

         super.onCreate(savedInstance);
     }

     private void populateList() {
         names = new ArrayList<>();
         names.add(new UserChecklistSample(R.drawable.ic_people, authorName, authorMail, false, authorName));
         db.child("students").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                     if (passed_names.contains(childSnapshot.child("email").getValue(String.class)) ) {
                         String namesString = childSnapshot.child("name").getValue(String.class) +
                                 " " +
                                 childSnapshot.child("surname").getValue(String.class);
                         String mail = childSnapshot.child ("email").getValue (String.class);
                         names.add (new UserChecklistSample(R.drawable.ic_people, namesString, mail, false, childSnapshot.getKey()));
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
