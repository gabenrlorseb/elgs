package com.legs.unijet.tabletversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.legs.unijet.tabletversion.LoginActivity;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.splashactivity.SplashActivity;
import com.legs.unijet.tabletversion.utils.MainActivity;

public class LauncherActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);

        sp = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();
        boolean firstRun = sp.getBoolean("firstRun", true);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            spEditor.putBoolean("firstRun",false);
            spEditor.apply();
            startActivity (new Intent(getApplicationContext (), MainActivity.class));
        } else {
            if (firstRun) {
                spEditor.putBoolean("firstRun",true);
                spEditor.apply();
                startActivity (new Intent(getApplicationContext (), SplashActivity.class));
            } else {
                spEditor.putBoolean("firstRun",false);
                spEditor.apply();
                Toast.makeText(getApplicationContext(), "Your session has expired", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        }


    }
}