package com.example.fbchatapp;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


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
        senderRoom = SenderId + id;
        recevierRoom = id + SenderId;
        msgAdapter = new MsgAdapter(this, msgClasses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvMsg.setLayoutManager(linearLayoutManager);
        rvMsg.setAdapter(msgAdapter);

        DatabaseReference chatRef = database.getReference().child("chat").child(senderRoom).child("message");


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

                    MsgClass msgClass = new MsgClass(msg, SenderId,time);
                    DatabaseReference databaseReference = database.getReference().child("chat").child(senderRoom).child("message").push();
                    databaseReference.setValue(msgClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            database.getReference().child("chat").child(recevierRoom).child("message").push().setValue(msgClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    try {


                                    } catch (Exception e) {

                                    }

                                }
                            });

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
}