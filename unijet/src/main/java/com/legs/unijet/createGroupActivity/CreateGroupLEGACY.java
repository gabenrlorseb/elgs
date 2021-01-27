package com.legs.unijet.createGroupActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.legs.unijet.R;

import java.util.ArrayList;
import java.util.List;


public class CreateGroupLEGACY extends AppCompatActivity {


    List<UserSample> members=new ArrayList<> ();
    ArrayList<String> addNames=new ArrayList<> ();

    Button confirmButton = findViewById (R.id.confirm_button);

    Spinner setmember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.create_group);

        //UI COMPONENTS



             //parte per la searchview
        /*
        memberSearch.setQueryHint(getString(R.string.member));

        memberSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                //searchText = searchView.getQuery().toString();
                //firebaseUserSearch (searchText);
                return true;
            }
        }); */
       /* String email = "ciao.seddd@stud.it";
        String temp = email.split ("@")[0];
        temp = email.replace (".","_");*/

        setmember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        /*confirm.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
              //go();
            }
        });*/

    }



    /*void go() {
        String name = setname.getText ().toString ();
        String member = setmember
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();


                }
*/



}

