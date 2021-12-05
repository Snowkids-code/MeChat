package com.phoenix.mechat.view.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.phoenix.mechat.R;
import com.phoenix.mechat.view.auth.ui.Register;
import com.phoenix.mechat.view.call.CallActivity;
import com.phoenix.mechat.view.dashboard.ui.MainActivity;

public class Account extends AppCompatActivity {

    //xml
    Button buttonLogOut;
    BottomNavigationView bottom_nav_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        setContentView(R.layout.activity_account);
        initializeViews();
        bnb();
    }

    private void initializeViews() {
        bottom_nav_bar = findViewById(R.id.bottom_nav_bar);
        buttonLogOut = findViewById(R.id.buttonLogOut);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }

    private void bnb(){
        bottom_nav_bar.setSelectedItemId(R.id.account);
        bottom_nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;
                    case R.id.call:
                        startActivity(new Intent(getApplicationContext(), CallActivity.class));
                        return true;
                }

                return false;
            }
        });
    }

}