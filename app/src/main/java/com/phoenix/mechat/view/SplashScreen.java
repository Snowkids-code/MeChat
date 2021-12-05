package com.phoenix.mechat.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.phoenix.mechat.view.dashboard.ui.MainActivity;
import com.phoenix.mechat.R;
import com.phoenix.mechat.adapter.MyAdapter;
import com.phoenix.mechat.view.auth.ui.Register;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {

    //xml
    TextView textViewSkip;
    ViewPager2 viewPagerSplash;

    //Adapter
    ArrayList<Integer> arrayList = new ArrayList<>();
    MyAdapter myAdapter;
    //Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //remove title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        setContentView(R.layout.activity_splash_screen);

        initializeViews();
        mAuth = FirebaseAuth.getInstance();
        addImages();
        setAdapters();

    }

    private void initializeViews() {
        textViewSkip = findViewById(R.id.textViewSkip);
        viewPagerSplash = findViewById(R.id.viewPagerSplash);
        addClick();
    }

    private void addClick() {
        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }

    private void addImages(){
        arrayList.add(R.drawable.ic_chats);
        arrayList.add(R.drawable.ic_conversation);
        arrayList.add(R.drawable.ic_security);
    }

    private void setAdapters(){
        myAdapter = new MyAdapter(this, arrayList);
        viewPagerSplash.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPagerSplash.setAdapter(myAdapter);
    }

    //if user is registered don't show this splash screen
    @Override
    protected void onStart() {
        super.onStart();
        //check if user is signed in
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //remove activity from queue
            finish();
        }
    }

}