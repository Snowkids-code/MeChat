package com.phoenix.mechat.view.dashboard.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenix.mechat.R;
import com.phoenix.mechat.view.account.ui.Account;
import com.phoenix.mechat.view.auth.ui.Register;
import com.phoenix.mechat.view.call.CallActivity;
import com.phoenix.mechat.view.chat.ChatActivity;
import com.phoenix.mechat.view.dashboard.model.MainModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //xml
    BottomNavigationView bottom_nav_bar;
    ListView listViewMain;
    ProgressDialog progressDialog;
    EditText editTextSearchMain;
    ImageButton imageButtonSearchMain;
    TextView textViewAppTitle;

    //Adapter
    MainAdapter mainAdapter;
    SearchAdapter searchAdapter;

    //Array
    ArrayList<String> arrayListImages = new ArrayList<>();
    ArrayList<String> arrayListNames = new ArrayList<>();
    ArrayList<String> arrayListUniqueID = new ArrayList<>();
    ArrayList<String> arrayListID = new ArrayList<>();
    ArrayList<String> arrayListEmail = new ArrayList<>();
    ArrayList<String> arrayListFImages = new ArrayList<>();
    ArrayList<String> arrayListFNames = new ArrayList<>();
    ArrayList<String> arrayListFContent = new ArrayList<>();
    ArrayList<String> arrayListFID = new ArrayList<>();

    //Firebase
    String UID;
    DatabaseReference databaseReference;
    MainModel mainModel;

    //Search
    String searchName;
    String searchImage;
    String searchID;
    String searchUniqueID;
    String searchEmail;

    //Fonts
    Typeface typeface, typeface1, typefaceSB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove nav bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        setContentView(R.layout.activity_main);

        getUserID();
        initializeViews();
        fonts();
        bnb();
        addArrayData();


    }

    private void fonts() {
        typeface1 = ResourcesCompat.getFont(this, R.font.gothic_a1_black);
        typefaceSB = ResourcesCompat.getFont(this, R.font.gothic_a1_semi_bold);
        typeface = ResourcesCompat.getFont(this, R.font.gothic_a1_regular);
    }

    private void getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        UID = user.getUid();
    }

    private void initializeViews() {
        bottom_nav_bar = findViewById(R.id.bottom_nav_bar);
        listViewMain = findViewById(R.id.listViewMain);
        editTextSearchMain = findViewById(R.id.editTextSearchMain);
        imageButtonSearchMain = findViewById(R.id.imageButtonSearchMain);
        textViewAppTitle = findViewById(R.id.textViewAppTitle);
        setFonts();
        imageButtonSearchMain.setAlpha(0.0f);
        mainAdapter = new MainAdapter();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        VisibleButton();
    }

    private void setFonts() {
        textViewAppTitle.setTypeface(typeface1);
        editTextSearchMain.setTypeface(typeface);
    }

    private void VisibleButton() {
        editTextSearchMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editTextSearchMain.getText().toString().matches("")){
//                    addArrayData();
                    listViewMain.setAdapter(mainAdapter);
                    Log.e("SearchError", "Not Found");
                }else{
                    searchName();
                }

                /*imageButtonSearchMain.setAlpha(1.0f);
                imageButtonSearchMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextSearchMain.getText().toString().matches("")){
//                    addArrayData();
                }
            }
        });
    }

    private void searchName() {
        Register register = new Register();
        String name = register.convertName(editTextSearchMain.getText().toString());
        //loop through the name array
        for (int i = 0; i < arrayListNames.size(); i++){
            if (name.equals(arrayListNames.get(i))){
                //set results
                searchName = arrayListNames.get(i);
                searchImage = arrayListImages.get(i);
                searchID = arrayListID.get(i);
                searchUniqueID = arrayListUniqueID.get(i);
                searchEmail = arrayListEmail.get(i);
                //adapter
                searchAdapter = new SearchAdapter();
                listViewMain.setAdapter(searchAdapter);
            }else if (!name.equals(arrayListNames.get(i))){
                Log.e("SearchError", "Not Found");
            }
        }
    }

    private void bnb(){
        bottom_nav_bar.setSelectedItemId(R.id.dashboard);
        bottom_nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), Account.class));
                        return true;

                    case R.id.call:
                        startActivity(new Intent(getApplicationContext(), CallActivity.class));
                        return true;

                }

                return false;
            }
        });
    }

    private void addArrayData(){
        /*arrayListImages.add("https://firebasestorage.googleapis.com/v0/b/conversations-app-40900.appspot.com/o/Unknown%2FFace?alt=media&token=29b9e404-13c0-4bf5-bddd-0046b3749d9d");
        arrayListImages.add("h");
        arrayListNames.add("Tony");
        arrayListNames.add("Joshua");
        arrayListContent.add("Here");
        arrayListContent.add("There");*/


        //firebase initialize
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        //model
        mainModel = new MainModel();
//        //progress dialog
        progressDialog.show();
        //get Users
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressDialog.dismiss();
                MainModel value = snapshot.getValue(MainModel.class);
                //get values
                assert value != null;
                arrayListNames.add(value.getName());
                arrayListImages.add(value.getImageURL());
                arrayListID.add(value.getUID());
                arrayListUniqueID.add(value.getPushID());
                arrayListEmail.add(value.getEmail());
                listViewMain.setAdapter(mainAdapter);
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
        
        userFriends();

    }

    private void userFriends() {

        //get UserID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String UID = user.getUid();

        //firebase initialize
        databaseReference = FirebaseDatabase.getInstance().getReference(UID + "F");
        mainModel = new MainModel();
        //progress dialog
        progressDialog.show();
        //get Users Friends
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressDialog.dismiss();
                MainModel value = snapshot.getValue(MainModel.class);
                //get values
                assert value != null;
                arrayListFNames.add(value.getName());
                arrayListFImages.add(value.getImageURL());
                arrayListFID.add(value.getUID());
                arrayListUniqueID.add(value.getPushID());
                arrayListEmail.add(value.getEmail());
                listViewMain.setAdapter(mainAdapter);
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

    class MainAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayListFID.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.main_modification, null);
            //ImageView
            ImageView imageViewMain = view.findViewById(R.id.imageViewMain);
            Picasso.get()
                    .load(arrayListFImages.get(i))
                    .into(imageViewMain, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            imageViewMain.setImageResource(R.drawable.ic_account);
                        }
                    });
            //TextName
            TextView textViewMainName = view.findViewById(R.id.textViewMainName);
            textViewMainName.setText(arrayListFNames.get(i));
            textViewMainName.setTypeface(typefaceSB);
            //TextContent
            TextView textViewMainContent = view.findViewById(R.id.textViewMainContent);
            textViewMainContent.setText(arrayListUniqueID.get(i));
            textViewMainContent.setAlpha(0.0f);
            textViewMainContent.setHeight(0);
            //Text ID
            TextView textViewID = view.findViewById(R.id.textViewID);
            textViewID.setText(arrayListFID.get(i));
            textViewID.setAlpha(0.0f);
            textViewID.setHeight(0);
            //onClick
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID = textViewID.getText().toString();
                    String name = textViewMainName.getText().toString();

                    //check if user has clicked on their profile
                    if (ID.equals("UID")){
                        Toast.makeText(getApplicationContext(), "Search on the text box above", Toast.LENGTH_SHORT).show();
                    }else{
                        startActivity(new Intent(getApplicationContext(), ChatActivity.class).
                                putExtra("Name", name).
                                putExtra("ID", ID));
                    }
                }
            });
            return view;
        }
    }

    class SearchAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.main_modification, null);

            //ImageView
            ImageView imageViewMain = view.findViewById(R.id.imageViewMain);
            Picasso.get()
                    .load(searchImage)
                    .into(imageViewMain, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            imageViewMain.setImageResource(R.drawable.ic_account);
                        }
                    });

            //TextView
            //TextName
            TextView textViewMainName = view.findViewById(R.id.textViewMainName);
            textViewMainName.setText(searchName);
            textViewMainName.setTypeface(typefaceSB);
            //TextContent
            TextView textViewMainContent = view.findViewById(R.id.textViewMainContent);
            textViewMainContent.setText(arrayListUniqueID.get(i));
            textViewMainContent.setAlpha(0.0f);
            //Text ID
            TextView textViewID = view.findViewById(R.id.textViewID);
            textViewID.setText(searchID);
            textViewID.setAlpha(0.0f);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID = textViewID.getText().toString();
                    String name = textViewMainName.getText().toString();
                    String uniqueID = textViewMainContent.getText().toString();
                    for (int i = 0; i < arrayListFID.size(); i++){
                        if (ID.equals(arrayListFID.get(i))){
                            startActivity(new Intent(getApplicationContext(), ChatActivity.class).
                                    putExtra("Name", name).
                                    putExtra("ID", ID));
                        }else{
                            addFriend();
                        }
                    }

                }
            });

            return view;
        }
    }

    private void addFriend() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Add Friend");
        alertDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //add to friends list
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                //setters
                mainModel.setName(searchName);
                mainModel.setImageURL(searchImage);
                mainModel.setUID(searchID);
                mainModel.setPushID(searchUniqueID);
                mainModel.setEmail(searchEmail);

                //insert data
                DatabaseReference fRef = reference.child(UID + "F");
                DatabaseReference finalRef = fRef.child(searchUniqueID);
                finalRef.setValue(mainModel);
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

}