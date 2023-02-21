package com.example.appproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity  {

    TextView signUp;
    boolean p=false,q=false;
    EditText pNum;
    TextView useEmail;
    ProgressBar pbar;
    Button logIn;
    LinearLayout mainLayout;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pNum = findViewById(R.id.phonenum);
        logIn =(Button) findViewById(R.id.login);
        pbar = findViewById(R.id.loading);
        mainLayout = (LinearLayout)findViewById(R.id.container1);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = pNum.getText().toString();
                if (pNum.getText().toString().length()<10) {
                    pNum.setError("Enter valid Phone Number");
                } else {
                    pbar.setVisibility(View.VISIBLE);
                    db.collection("Customers")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String phoneNum = document.get("phoneNum").toString();
                                            if(phoneNum.equals(phone)){
                                                p=true;
                                                intent = new Intent(Login.this, Main2Activity.class);
                                                intent.putExtra("type","Customers");
                                                intent.putExtra("phonenumber",   phone);
                                                startActivity(intent);
                                                return;
                                            }
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    db.collection("ServiceProviders")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String phoneNum = document.get("phoneNum").toString();
                                            if(phoneNum.equals(phone)){
                                                q=true;
                                                intent = new Intent(Login.this, sp_homepage.class);
                                                intent.putExtra("type","serviceProviders");
                                                intent.putExtra("phonenumber",   phone);
                                                startActivity(intent);
                                                return;
                                            }
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                    // Check if phone number is registered in either collection
                                    if (p == false && q == false) {
                                        pbar.setVisibility(View.GONE);
                                        Toast.makeText(Login.this, "Mobile Number not registered. Sign up", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        useEmail = (TextView) findViewById(R.id.email);
        useEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent in1 = new Intent(Login.this, EmailLogin.class);
                startActivity(in1);
                finish();
            }
        });

        signUp = (TextView) findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
            }
        });
    }
//    @Override
//    public void onBackPressed(){
//
//        finish();
//    }
}
