package com.example.fbchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CreateNewAccountActivity extends AppCompatActivity {

    EditText edUserName, edPassword, edName;
    TextView btnLogin;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        edName = findViewById(R.id.edName);
        btnLogin = findViewById(R.id.btnLogin);


        String emailPattern = "(([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)(\\s*(;|,)\\s*|\\s*$))*";

        edUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edUserName.getText().toString().toString().matches(emailPattern)) {
                    edUserName.setError(null);
                } else {
                    edUserName.setError("Invalid Email");
                    edUserName.requestFocus();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edName.getText().toString().length() == 0) {
                    edName.setError("Enter the Name");
                    edName.requestFocus();
                } else if (edUserName.getText().toString().length() == 0) {
                    edUserName.setError("Enter the Email");
                    edUserName.requestFocus();
                } else if (edPassword.getText().toString().length() == 0) {
                    edPassword.setError("Enter the Password");
                    edPassword.requestFocus();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(CreateNewAccountActivity.this,R.style.ProgressBarDialog);
                    progressDialog.setMessage("AKVP Please wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    String UserName = edName.getText().toString().trim();
                    String Email = edUserName.getText().toString().trim();
                    String Password = edPassword.getText().toString().trim();
                    auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference databaseReference = database.getReference().child("user").child(id);
                                UserClass userClass = new UserClass(id,UserName, Email, Password);
                                databaseReference.setValue(userClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(CreateNewAccountActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(CreateNewAccountActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                            } else {
                                Toast.makeText(CreateNewAccountActivity.this, " Failed \n Try again ", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }
            }
        });
    }
}