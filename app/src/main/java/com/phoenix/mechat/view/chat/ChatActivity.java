package com.phoenix.mechat.view.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenix.mechat.R;
import com.phoenix.mechat.view.chat.model.ChatModel;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    //Bundle
    Bundle bundle;
    String name;
    String ID;

    //xml
    ImageView imageViewImageChat;
    TextView textViewNameChat;
    ImageButton imageButtonCallChat, imageButtonVideoChat, imageButtonSend;
    EditText editTextMessage;
    ListView listViewChat;

    //Adapter
    ChatAdapter chatAdapter;
    ArrayList<String> arrayListSent = new ArrayList<>();
    ArrayList<String> arrayListReceived = new ArrayList<>();

    //firebase
    ChatModel chatModel;
    String UID;

    //Fonts
    Typeface typefaceSB, typefaceR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        setContentView(R.layout.activity_chat);

        //get bundle data
        bundle = getIntent().getExtras();
        name = bundle.getString("Name").trim();
        ID = bundle.getString("ID");
        //get UserID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        UID = user.getUid();
        chatModel = new ChatModel();

        initializeViews(name);
        fonts();
    }

    private void fonts() {
        typefaceSB = ResourcesCompat.getFont(this, R.font.gothic_a1_semi_bold);
        typefaceR = ResourcesCompat.getFont(this, R.font.gothic_a1_regular);
    }

    private void initializeViews(String name) {
        listViewChat = findViewById(R.id.listViewChat);
        textViewNameChat = findViewById(R.id.textViewNameChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageButtonSend = findViewById(R.id.imageButtonSend);
        imageButtonSend.setAlpha(0.0f);
        imageButtonCallChat = findViewById(R.id.imageButtonCallChat);
        imageButtonVideoChat = findViewById(R.id.imageButtonVideoChat);
        setFont();
        VIButton();
        textViewNameChat.setText(name);
        chatAdapter = new ChatAdapter();

        addData(ID);
    }

    private void setFont() {
        textViewNameChat.setTypeface(typefaceSB);
        editTextMessage.setTypeface(typefaceR);
    }

    private void VIButton() {
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editTextMessage.getText().toString().matches("")){
                    imageButtonSend.setAlpha(0.0f);
                }else{
                    imageButtonSend.setAlpha(1.0f);
                    imageButtonSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String message = editTextMessage.getText().toString();
                            sendMessage(message);
                            editTextMessage.setText("");
                            editTextMessage.setHint("Message");
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addData(String ID){
        /*arrayListReceived.add("Hello");
        arrayListSent.add("Hi");
        arrayListReceived.add("How have you been?");
        arrayListSent.add("Fine");
        arrayListReceived.add("");
        arrayListSent.add("How have you been?");
        arrayListReceived.add("");
        arrayListSent.add("How have you been?ghdhggdfhtdtytdtydhgchghj h thd tydhtdhgdhgdytdiytdjghcjghjgfd");*/



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UID + "-" + ID);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatModel value = snapshot.getValue(ChatModel.class);
                assert value != null;
                arrayListSent.add(value.getSent());
                arrayListReceived.add(value.getReceived());
                listViewChat.setAdapter(chatAdapter);
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

    private void sendMessage(String message){
        //submit the data
        //reread the submitted data have a progress dialog to load this info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        //setter
        chatModel.setSent(message);
        chatModel.setReceived("");

        //first node
        DatabaseReference bRef = reference.child(UID + "-" + ID);
        DatabaseReference pRef = bRef.push();
        pRef.setValue(chatModel);
//        addData(ID);

        //second node
        secondNode(message);
    }

    private void secondNode(String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        //Setters
        chatModel.setReceived(message);
        chatModel.setSent("");

        DatabaseReference bRef = reference.child(ID + "-" + UID);
        DatabaseReference pRef = bRef.push();
        pRef.setValue(chatModel);

        //imageButton invisible
        imageButtonSend.setAlpha(0.0f);

    }

    class ChatAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayListSent.size();
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
            view = getLayoutInflater().inflate(R.layout.chat_modification, null);
            //TextView received
            TextView textViewReceived = view.findViewById(R.id.textViewReceived);
            textViewReceived.setText(arrayListReceived.get(i));
            textViewReceived.setTypeface(typefaceR);
            //textView sent
            TextView textViewSent = view.findViewById(R.id.textViewSent);
            textViewSent.setText(arrayListSent.get(i));
            textViewSent.setTypeface(typefaceR);
            if (arrayListReceived.get(i).equals("")){
                textViewReceived.setHeight(5);
                textViewReceived.setAlpha(0.0f);
            }else if (arrayListSent.get(i).equals("")){
                textViewSent.setHeight(5);
                textViewSent.setAlpha(0.0f);
            }
            return view;
        }
    }

}