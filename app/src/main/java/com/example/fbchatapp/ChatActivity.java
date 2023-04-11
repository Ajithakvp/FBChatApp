package com.example.fbchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    RecyclerView rvListUser;

    UserListAdapter userListAdapter;
    ArrayList<UserClass>userClasses;

    FirebaseAuth auth;
    FirebaseDatabase database;

    ImageView ivLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvListUser=findViewById(R.id.rvListUser);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        ivLogout=findViewById(R.id.ivLogout);


        if (auth.getCurrentUser()==null){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }


        DatabaseReference databaseReference=database.getReference().child("user");
        userClasses=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserClass userClass=dataSnapshot.getValue(UserClass.class);
                    userClasses.add(userClass);
                }
                userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        userListAdapter=new UserListAdapter(ChatActivity.this,userClasses);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvListUser.setAdapter(userListAdapter);
        rvListUser.setLayoutManager(linearLayoutManager);


        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this,R.style.AlertDialogCustom);
                builder.setCancelable(false);
                builder.setTitle("Log out !");
                builder.setMessage("Do you want to log out ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        auth.signOut();
                        dialogInterface.dismiss();
                        PreferenceManger.setLoggedStatus(ChatActivity.this,false);
                        Intent intent=new Intent(ChatActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
    }
}