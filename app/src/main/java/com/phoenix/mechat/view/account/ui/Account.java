package com.phoenix.mechat.view.account.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenix.mechat.R;
import com.phoenix.mechat.view.account.model.AccountModel;
import com.phoenix.mechat.view.auth.ui.Register;
import com.phoenix.mechat.view.call.CallActivity;
import com.phoenix.mechat.view.dashboard.ui.MainActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Account extends AppCompatActivity {

    //xml
    Button buttonLogOut;
    BottomNavigationView bottom_nav_bar;
    ImageView imageViewPP;
    TextView textViewEditPP, textViewSUsername, textViewSUserID, textViewSEmail,
            textViewAccUN, textViewAccUI, textViewAccUE;

    //Model
    AccountModel accountModel;

    //String
    String uniqueID;

    //fonts
    Typeface typefaceR, typefaceSB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        setContentView(R.layout.activity_account);
        initializeViews();
        fonts();
        bnb();
    }

    private void fonts() {
        typefaceSB = ResourcesCompat.getFont(this, R.font.gothic_a1_semi_bold);
        typefaceR = ResourcesCompat.getFont(this, R.font.gothic_a1_regular);
    }

    private void initializeViews() {
        bottom_nav_bar = findViewById(R.id.bottom_nav_bar);
        buttonLogOut = findViewById(R.id.buttonLogOut);
        textViewAccUN = findViewById(R.id.textViewAccUN);
        textViewAccUI = findViewById(R.id.textViewAccUI);
        textViewAccUE = findViewById(R.id.textViewAccUE);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
        imageViewPP = findViewById(R.id.imageViewPP);
        textViewEditPP = findViewById(R.id.textViewEditPP);
        textViewSUsername = findViewById(R.id.textViewSUsername);
        textViewSUserID = findViewById(R.id.textViewSUserID);
        textViewSEmail = findViewById(R.id.textViewSEmail);
        setFonts();
        getTextData();

        //onClick
        textViewEditPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UploadImage.class).
                        putExtra("Name", textViewSUsername.getText().toString()).
                        putExtra("uniqueID", uniqueID));
            }
        });
    }

    private void setFonts() {
        buttonLogOut.setTypeface(typefaceSB);
        textViewAccUN.setTypeface(typefaceSB);
        textViewAccUI.setTypeface(typefaceSB);
        textViewAccUE.setTypeface(typefaceSB);
        textViewEditPP.setTypeface(typefaceR);
        textViewSUsername.setTypeface(typefaceR);
        textViewSUserID.setTypeface(typefaceR);
        textViewSEmail.setTypeface(typefaceR);
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

    private void getTextData(){
        //Get User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String UID = user.getUid();

        //initialize firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UID);

        //Initialize model
        accountModel = new AccountModel();

        //read data
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AccountModel value = snapshot.getValue(AccountModel.class);

                //get and set values
                assert value != null;
                textViewSUsername.setText(value.getName());
                textViewSUserID.setText(value.getUID());
                textViewSEmail.setText(value.getEmail());
                uniqueID = value.getPushID();

                //Image
                String imageURL = value.getImageURL();
                loadImage(imageURL);
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

    private void loadImage(String imageURL) {
        Picasso.get()
                .load(imageURL)
                .into(imageViewPP, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        imageViewPP.setImageResource(R.drawable.ic_account);
                    }
                });
    }

}