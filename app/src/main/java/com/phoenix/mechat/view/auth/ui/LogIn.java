package com.phoenix.mechat.view.auth.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.phoenix.mechat.view.dashboard.ui.MainActivity;
import com.phoenix.mechat.R;

public class LogIn extends AppCompatActivity {

    //xml
    EditText editTextEmailLogin, editTextPassLogin;
    Button buttonLogin;
    TextView textViewSignUp, textViewLogReg, textViewLogTitle;
    ProgressDialog progressDialog;

    //Firebase
    FirebaseAuth mAuth;

    //fonts
    Typeface typefaceSB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get window off
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        initializeViews();
        fonts();
    }

    private void fonts() {
        typefaceSB = ResourcesCompat.getFont(this, R.font.gothic_a1_semi_bold);
    }

    private void initializeViews() {
        editTextEmailLogin = findViewById(R.id.editTextEmailLogin);
        editTextPassLogin = findViewById(R.id.editTextPassLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewLogReg = findViewById(R.id.textViewLogReg);
        textViewLogTitle = findViewById(R.id.textViewLogTitle);
        setFonts();

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailLogin.getText().toString().trim();
                String pass = editTextPassLogin.getText().toString().trim();
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                login(email, pass);
            }
        });
    }

    private void setFonts() {
        editTextEmailLogin.setTypeface(typefaceSB);
        editTextPassLogin.setTypeface(typefaceSB);
        buttonLogin.setTypeface(typefaceSB);
        textViewSignUp.setTypeface(typefaceSB);
        textViewLogReg.setTypeface(typefaceSB);
        textViewLogTitle.setTypeface(typefaceSB);
    }

    private void login(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            assert user != null;
                            String UID = user.getUid();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Check your email or password and try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}