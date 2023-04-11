package com.example.fbchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.ktx.Firebase;

public class LoginActivity extends AppCompatActivity {


    FirebaseAuth auth;

    EditText edUserName, edPassword;
    TextView btnLogin;

    TextView tvCreateAc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAc = findViewById(R.id.tvCreateAc);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edUserName.getText().toString().length()==0){
                    edUserName.setError("Enter the Email");
                    edUserName.requestFocus();
                }else if (edPassword.getText().toString().length()==0){
                    edPassword.setError("Enter the Password");
                    edPassword.requestFocus();
                }else {
                    ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,R.style.ProgressBarDialog);
                    progressDialog.setMessage("AKVP Please wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    String Email = edUserName.getText().toString();
                    String Password = edPassword.getText().toString();
                    auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            try {

                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    PreferenceManger.setLoggedStatus(LoginActivity.this,true);
                                    Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                            }


                        }
                    });
                }
            }
        });

        tvCreateAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CreateNewAccountActivity.class);
                startActivity(intent);
            }
        });


    }
}