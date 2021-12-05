package com.phoenix.mechat.view.auth.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenix.mechat.view.dashboard.ui.MainActivity;
import com.phoenix.mechat.R;
import com.phoenix.mechat.view.auth.model.AuthModel;

public class Register extends AppCompatActivity {

    //xml
    EditText editTextUserPhone, editTextConfirmCode, editTextNameReg;
    Button buttonRegister;
    ProgressDialog progressDialog;
    TextView textViewSignIn;

    //firebase
    private FirebaseAuth mAuth;
    AuthModel authModel;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        setContentView(R.layout.activity_register);
        initializeViews();
        //initialize firebase Auth
        mAuth = FirebaseAuth.getInstance();
        authModel = new AuthModel();
        progressDialog = new ProgressDialog(this);
        handler = new Handler();
    }

    private void initializeViews() {
        editTextUserPhone = findViewById(R.id.editTextUserPhone);
        editTextConfirmCode = findViewById(R.id.editTextConfirmCode);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewSignIn = findViewById(R.id.textViewSignIn);
        editTextNameReg = findViewById(R.id.editTextNameReg);

        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LogIn.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
                progressDialog.show();
            }
        });
    }

    //check to see if user is signed in
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

    private void validateUser(){
        //get value of email and password
        String email = editTextUserPhone.getText().toString();
        String pass = editTextConfirmCode.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            sendVerificationEmail();
                        }else{
                            Toast.makeText(getApplicationContext(), "Registration failed, try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationEmail() {
        //get User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //send verification email
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            registerUser();
                            /*Toast.makeText(getApplicationContext(),"Check your email for verification code",
                                    Toast.LENGTH_SHORT).show();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    checkEmailVerification(user);
                                }
                            }, 30000);*/
                        }else{

                        }
                    }
                });
    }

    private void checkEmailVerification(FirebaseUser user) {
        if (user.isEmailVerified()){
            progressDialog.dismiss();
            registerUser();
        }else{
            Log.e("LOG", "Not authenticated");
        }
    }


    private void registerUser() {
        //get Email
        String email = editTextUserPhone.getText().toString();
        String name = convertName(editTextNameReg.getText().toString());
        //initialize DatabaseR
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //Get current userUID
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String UID = user.getUid();
        //setters
        authModel.setEmail(email);
        authModel.setUID(UID);
        authModel.setName(name);
        authModel.setImageURL("h");
        //push data to specific user node
        DatabaseReference dRef = databaseReference.child(UID);
        String PushID = dRef.push().getKey();
        authModel.setPushID(PushID);
        assert PushID != null;
        DatabaseReference pRef = dRef.child(PushID);
        pRef.setValue(authModel);
        //push data to general users
        DatabaseReference gUsers = databaseReference.child("Users");
        DatabaseReference pUsers = gUsers.child(PushID);
        pUsers.setValue(authModel);
        nextActivity(UID);
    }

    public String convertName(String name){

        char firstChar = name.charAt(0);
        char capital = Character.toUpperCase(firstChar);

        return capital + name.substring(1);
    }

    private void nextActivity(String UID) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

}