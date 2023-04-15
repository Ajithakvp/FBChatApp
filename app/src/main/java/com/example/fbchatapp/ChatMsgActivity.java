package com.example.fbchatapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ChatMsgActivity extends AppCompatActivity {

    ImageView ivSend, ivback;
    EditText edTyping;
    RecyclerView rvMsg;
    TextView tvUserName;

    String UserName, id;

    FirebaseAuth auth;
    FirebaseDatabase database;

    String SenderId, senderRoom, recevierRoom;

    MsgAdapter msgAdapter;
    ArrayList<MsgClass> msgClasses = new ArrayList<>();
    boolean seenby = false;






    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg);

        ivSend = findViewById(R.id.ivSend);
        edTyping = findViewById(R.id.edTyping);
        rvMsg = findViewById(R.id.rvMsg);
        tvUserName = findViewById(R.id.tvUserName);
        ivback = findViewById(R.id.ivback);

        UserName = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");

        tvUserName.setText(UserName);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        SenderId = auth.getUid();
        msgAdapter = new MsgAdapter(this, msgClasses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvMsg.setLayoutManager(linearLayoutManager);
        rvMsg.setAdapter(msgAdapter);

        DatabaseReference chatRef = database.getReference().child("chat").child("message");




            Seenmsg();


        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgClasses.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MsgClass msgClass = dataSnapshot.getValue(MsgClass.class);
                    msgClasses.add(msgClass);
                }

                msgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edTyping.getText().toString();

                if (msg.isEmpty()) {
                    Toast.makeText(ChatMsgActivity.this, "typing", Toast.LENGTH_SHORT).show();
                } else {

                    edTyping.setText("");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date date = new Date();
                    String time = formatter.format(date);




                    DatabaseReference databaseReference = database.getReference().child("chat").child("message").push();
                   String key= databaseReference.getKey();
                    MsgClass msgClass = new MsgClass(msg, SenderId, time, id,key, seenby);
                    databaseReference.setValue(msgClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {



                        }
                    });
                }
            }
        });


        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void Seenmsg() {
         DatabaseReference chatRef = database.getReference().child("chat").child("message");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MsgClass msgClass = dataSnapshot.getValue(MsgClass.class);
                            if (msgClass.getReceiver().trim().equals(auth.getUid()) && msgClass.getSenderId().trim().equals(id) ) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("seen", true);
                                dataSnapshot.getRef().updateChildren(hashMap);
                            }

                        }
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        chatRef.addListenerForSingleValueEvent(valueEventListener);
    }
}