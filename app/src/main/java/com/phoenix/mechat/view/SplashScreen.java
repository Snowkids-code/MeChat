package com.phoenix.mechat.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Typeface;
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
    private final ArrayList<String> arrayListH = new ArrayList<>();
    private final ArrayList<String> arrayListC = new ArrayList<>();
    MyAdapter myAdapter;
    //Firebase
    FirebaseAuth mAuth;

    //Fonts
    Typeface typeface;

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

        //fonts
        typeface = ResourcesCompat.getFont(this, R.font.gothic_a1_regular);

        mAuth = FirebaseAuth.getInstance();
        addImages();
        setAdapters();

    }

    private void initializeViews() {
        textViewSkip = findViewById(R.id.textViewSkip);
        viewPagerSplash = findViewById(R.id.viewPagerSplash);
        textViewSkip.setTypeface(typeface);
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
        arrayListH.add("Talk to those most special to you");
        arrayListH.add("Share a moment with those nearby");
        arrayListH.add("Enjoy top quality security for your chat");
        arrayListC.add("Keep the connection with those that matter most in your life");
        arrayListC.add("Meet and share idea with others");
        arrayListC.add("Keep texting without the worry of who is reading your conversation");
    }

    private void setAdapters(){
        myAdapter = new MyAdapter(this, arrayList, arrayListH, arrayListC);
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