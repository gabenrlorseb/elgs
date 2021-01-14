package com.legs.unijet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

public class ProfileStudent extends AppCompatActivity {
 Button logout_button,settings_button,notifications_button;


     FirebaseUser user;
    String userId;
    FirebaseUser auth;
    DatabaseReference reference;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.myunijet);
        logout_button = findViewById (R.id.logout_button);

        user = FirebaseAuth.getInstance().getCurrentUser ();
         reference = FirebaseDatabase.getInstance ().getReference ("students");
         userId=user.getUid ();
         final TextView text_name_surname= (TextView)findViewById (R.id.text_name_surname);
        final TextView email_logn_field=(TextView) findViewById (R.id.email_logn_field);
        reference.child(userId).addListenerForSingleValueEvent (new ValueEventListener () {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue (User.class);
                if (userProfile != null) {
                    String name = userProfile.name;
                    String email = user.getEmail();

                    String surname = userProfile.surname;
                    String nameFull= name+"  "+surname;
                    text_name_surname.setText (nameFull.toUpperCase ());
                    email_logn_field.setText (email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // fStore = FirebaseFirestore.getInstance();
        //storageReference = FirebaseStorage.getInstance().getReference();

        //StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        //  profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        ///@Override
        //  public void onSuccess(Uri uri) {
        //    Picasso.get().load(uri).into(profileImage);
        //}
        //});

        //resendCode = findViewById(R.id.resendCode);
        // verifyMsg = findViewById(R.id.verifyMsg);



        logout_button.setOnClickListener (new android.view.View.OnClickListener () {
            @Override
            public void onClick(android.view.View v) {
                logout (view);




            }
        });

      //  if(!user.isEmailVerified()){
        //    verifyMsg.setVisibility(View.VISIBLE);
          //  resendCode.setVisibility(View.VISIBLE);

            //resendCode.setOnClickListener(new View.OnClickListener() {
              //  @Override
               // public void onClick(final View v) {

                 //   user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                   //     @Override
                        //public void onSuccess(Void aVoid) {
                     //       Toast.makeText(v.getContext(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                       // }
                    //}).addOnFailureListener(new OnFailureListener() {
                      //  @Override
                        //public void onFailure(@NonNull Exception e) {
                          //  Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                        //}
                    //});
                //}
            //});
       // }




        // DocumentReference documentReference = fStore.collection("users").document(userId);
        //documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
        //   @Override
        // public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        //   if(documentSnapshot.exists()){
        //     phone.setText(documentSnapshot.getString("phone"));
        //   fullName.setText(documentSnapshot.getString("fName"));
        // email.setText(documentSnapshot.getString("email"));

        // }else {
        //   Log.d("tag", "onEvent: Document do not exists");
        //}
        //}
        //});


        //  resetPassLocal.setOnClickListener(new View.OnClickListener()

        // {
        //   @Override
        // public void onClick (View v){

        //final EditText resetPassword = new EditText (v.getContext ());

        //final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder (v.getContext ());
        //passwordResetDialog.setTitle ("Reset Password ?");
        //passwordResetDialog.setMessage ("Enter New Password > 6 Characters long.");
        //passwordResetDialog.setView (resetPassword);

        //passwordResetDialog.setPositiveButton ("Yes", new DialogInterface.OnClickListener () {
        //  @Override
        // public void onClick(DialogInterface dialog, int which) {
        // extract the email and send reset link
        //   String newPassword = resetPassword.getText ().toString ();
        // user.updatePassword (newPassword).addOnSuccessListener (new OnSuccessListener<Void> () {
        //   @Override
        // public void onSuccess(Void aVoid) {
        //   Toast.makeText (MainActivity.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show ();
        //  }
        //}).addOnFailureListener (new OnFailureListener () {
        //  @Override
        //public void onFailure(@NonNull Exception e) {
        //  Toast.makeText (MainActivity.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show ();
        // }
        // });
        // }
        //});

    /*        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // close
                }
            });

            passwordResetDialog.create().show();

        }
    });*/

       /* changeProfileImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // open gallery
            Intent i = new Intent(v.getContext(),EditProfile.class);
            i.putExtra("fullName",fullName.getText().toString());
            i.putExtra("email",email.getText().toString());
            i.putExtra("phone",phone.getText().toString());
            startActivity(i);
//

        }
    });*/




    }
    public void logout (View view){
        FirebaseAuth.getInstance ().signOut ();//logout
        startActivity (new Intent (getApplicationContext (), BaseActivity.class));
        finish ();
    }


    }


