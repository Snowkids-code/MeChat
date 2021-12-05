package com.phoenix.mechat.view.dashboard.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenix.mechat.R;
import com.phoenix.mechat.view.account.Account;
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

    //Adapter
    MainAdapter mainAdapter;
//    SearchAdapter searchAdapter;

    //Array
    ArrayList<String> arrayListImages = new ArrayList<>();
    ArrayList<String> arrayListNames = new ArrayList<>();
    ArrayList<String> arrayListContent = new ArrayList<>();
    ArrayList<String> arrayListID = new ArrayList<>();

    //Firebase
    String UID;
    DatabaseReference databaseReference;
    MainModel mainModel;

    //Search
    String searchName;
    String searchImage;
    String searchID;

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
        bnb();
        //firebase initialize
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        //model
        mainModel = new MainModel();
        addArrayData();

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
        imageButtonSearchMain.setAlpha(0.0f);
//        VisibleButton();
        mainAdapter = new MainAdapter();
        listViewMain.setAdapter(mainAdapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
    }

    private void VisibleButton() {
        editTextSearchMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                imageButtonSearchMain.setAlpha(1.0f);
                imageButtonSearchMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchName();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchName() {
        Register register = new Register();
        String name = register.convertName(editTextSearchMain.getText().toString());
        //loop through the name array
        /*for (int i = 0; i < arrayListNames.size(); i++){
            if (name.equals(arrayListNames.get(i))){
                searchName = arrayListNames.get(i);
                searchImage = arrayListImages.get(i);
                searchID = arrayListID.get(i);
                searchAdapter = new SearchAdapter();
                listViewMain.setAdapter(searchAdapter);
            }
        }*/
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


//        //progress dialog
//        progressDialog.show();
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


                Log.e("Data", String.valueOf(arrayListNames.size()));
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
            return arrayListID.size();
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
                    .load(arrayListNames.get(i))
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
            textViewMainName.setText(arrayListNames.get(i));
            //TextContent
            TextView textViewMainContent = view.findViewById(R.id.textViewMainContent);
//            textViewMainContent.setText(arrayListContent.get(i));
            //Text ID
            TextView textViewID = view.findViewById(R.id.textViewID);
            textViewID.setText(arrayListID.get(i));
            textViewID.setAlpha(0.0f);
            //onClick
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID = textViewID.getText().toString();
                    String name = textViewMainName.getText().toString();
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class).
                            putExtra("Name", name).
                            putExtra("ID", ID));
                }
            });
            return view;
        }
    }

    /*class SearchAdapter extends BaseAdapter{

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
            //TextContent
            TextView textViewMainContent = view.findViewById(R.id.textViewMainContent);
            textViewMainContent.setText(arrayListContent.get(i));
            //Text ID
            TextView textViewID = view.findViewById(R.id.textViewID);
            textViewID.setText(searchID);
            textViewID.setAlpha(0.0f);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID = textViewID.getText().toString();
                    String name = textViewMainName.getText().toString();
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class).
                            putExtra("Name", name).
                            putExtra("ID", ID));
                }
            });

            return view;
        }
    }*/

}