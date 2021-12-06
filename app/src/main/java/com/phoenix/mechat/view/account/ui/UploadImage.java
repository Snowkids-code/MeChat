package com.phoenix.mechat.view.account.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.phoenix.mechat.R;
import com.phoenix.mechat.view.account.model.AccountModel;

import java.io.IOException;
import java.util.Objects;

public class UploadImage extends AppCompatActivity {

    //Bundle
    Bundle bundle;
    String Name;
    String Job;
    String uniqueID;
    String ImageURL;

    //xml
    private Button btnChoose, btnUpload, btnContinue;
    private ImageView imageView;

    //file
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    AccountModel accountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        setContentView(R.layout.activity_upload_image);

        //Initialize Views
        btnChoose = (Button) findViewById(R.id.btnChooseImage);
        btnUpload = (Button) findViewById(R.id.btnUploadImage);
        btnContinue = (Button) findViewById(R.id.btnContinueImage);
        imageView = (ImageView) findViewById(R.id.imgViewImage);

//        final String name = Objects.requireNonNull(extras.getString("name")).trim();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Continue();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {

            //Bind info from previous activity
            bundle = getIntent().getExtras();
            assert bundle != null;
            Name = Objects.requireNonNull(bundle.getString("Name")).trim();

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            StorageReference ref = FirebaseStorage.getInstance().getReference(Name);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded" + filePath, Toast.LENGTH_SHORT).show();
                            imageView.setImageResource(android.R.color.transparent);
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra("name", name);
//                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @SuppressLint("Assert")
    public void Continue(){

        //Bind info from previous activity
        bundle = getIntent().getExtras();
        assert bundle != null;
        Name = Objects.requireNonNull(bundle.getString("Name")).trim();

        storageReference.child(Name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURL = uri.toString().trim();
                uploadData(ImageURL);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void uploadData(String imageURL) {

        bundle = getIntent().getExtras();
        assert bundle != null;
        Name = Objects.requireNonNull(bundle.getString("Name")).trim();
        uniqueID = bundle.getString("uniqueID");

        //get UserID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String UID = user.getUid();

        accountModel = new AccountModel();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //append data on individual node
        databaseReference.child(UID).child(uniqueID).child("imageURL").setValue(imageURL);

        //append data on Users node
        databaseReference.child("Users").child(uniqueID).child("imageURL").setValue(imageURL);

        //start new activity
        Toast.makeText(getApplicationContext(), "Image Changed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Account.class));
        finish();

    }

}