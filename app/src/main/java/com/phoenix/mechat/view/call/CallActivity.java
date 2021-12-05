package com.phoenix.mechat.view.call;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phoenix.mechat.R;
import com.phoenix.mechat.view.account.Account;
import com.phoenix.mechat.view.dashboard.ui.MainActivity;

public class CallActivity extends AppCompatActivity {

    //xml
    BottomNavigationView bottom_nav_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        initializeViews();
        bnb();

    }

    private void initializeViews() {
        bottom_nav_bar = findViewById(R.id.bottom_nav_bar);
    }

    private void bnb() {

        bottom_nav_bar.setSelectedItemId(R.id.call);
        bottom_nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), Account.class));
                        return true;

                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;

                }

                return false;
            }
        });

    }

}