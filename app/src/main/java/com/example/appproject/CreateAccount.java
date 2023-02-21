package com.example.appproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {
    EditText firstName, lastName, emailId, phoneNum, password, secondPassword;

    private DatabaseReference mDatabase;

    Button submit;
    Spinner cityList, categories;

    RadioGroup accType;
    RadioButton radioCustomer, radioService;

    int p = 0;
    private FirebaseAuth mAuth;
    final ArrayList<ServiceProvider> serviceProvidersArrayList = new ArrayList<>();
    final ArrayList<Customer> customerArrayList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);
        findViews();
        mAuth = FirebaseAuth.getInstance();
        radioService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories.setVisibility(View.VISIBLE);
            }
        });
        radioCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories.setVisibility(View.INVISIBLE);
            }
        });


        submit.setOnClickListener(v -> {

            boolean checkIfServiceClicked = radioService.isChecked();
            if (checkIfServiceClicked)
                createServiceProvider();
            boolean checkIfCustomerClicked = radioCustomer.isChecked();
            if (checkIfCustomerClicked) {
                createCustomer();
            }

        });

        final ArrayList<Customer> customersArrayList = new ArrayList<>();
        int size = customersArrayList.size();
//not sure what this does
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityList.setAdapter(adapter);
        // cityList.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this,
                R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapt);
        // categories.setOnItemSelectedListener(this);
    }

    private void findViews() {
        submit = (Button) findViewById(R.id.btSubmit);
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        emailId = (EditText) findViewById(R.id.email);
        phoneNum = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.pwd);
        secondPassword = (EditText) findViewById(R.id.confirmpwd);
        cityList = (Spinner) findViewById(R.id.citylist);
        accType = (RadioGroup) findViewById(R.id.acctype);
        categories = (Spinner) findViewById(R.id.categories);

        categories.setVisibility(View.INVISIBLE);
        radioCustomer = (RadioButton) findViewById(R.id.checkCustomer);
        radioService = (RadioButton) findViewById(R.id.checkServiceProvider);

    }

    void createServiceProvider() {
        final String firstname = firstName.getText().toString().trim();
        final String lastname = lastName.getText().toString().trim();
        final String emailid = emailId.getText().toString();
        final String phonenum = phoneNum.getText().toString();
        final String city = cityList.getSelectedItem().toString();
        final String pword = password.getText().toString();
        final String cpword = secondPassword.getText().toString();
        final String category = categories.getSelectedItem().toString();
        if (firstName.getText().toString().trim().equalsIgnoreCase(""))
            firstName.setError("First name is required!");

        else if (lastName.getText().toString().trim().equalsIgnoreCase(""))
            lastName.setError("Last name is required!");

        else if (emailId.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString()).matches())
            emailId.setError("Enter valid Email Id!");

        else if (phoneNum.getText().toString().length() < 10)
            phoneNum.setError("Enter valid Phone Number!");

        else if (cityList.getSelectedItem().toString().matches("Select City")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Select City!",
                    Toast.LENGTH_LONG);
            toast.show();
        } else if (password.getText().toString().length() < 8)
            password.setError("Password of minimum 8 characters is required");
        else if (!password.getText().toString().matches(secondPassword.getText().toString()))
            password.setError("Passwords do not match. Try again!");
        else if (categories.getSelectedItem().toString().matches("Select Category")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Select Category!",
                    Toast.LENGTH_LONG);
            toast.show();
        } else {


            final Bundle bundle = new Bundle();
            bundle.putString("firstname", firstname);
            bundle.putString("lastname", lastname);
            bundle.putString("emailid", emailid);
            bundle.putString("phonenum", phonenum);
            bundle.putString("city", city);
            bundle.putString("category", category);


            mAuth.createUserWithEmailAndPassword(emailid, pword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
//

                            if (task.isSuccessful()) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> user = new HashMap<>();
                                user.put("firstname", firstname);
                                user.put("lastname", lastname);
                                user.put("emailID", emailid);
                                user.put("phoneNum", phonenum);
                                user.put("city", city);
                                user.put("category", category);
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                db.collection("ServiceProviders")
                                        .document(uid)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void avoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
//                                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                Map<String, Object> user = new HashMap<>();
//                                user.put("firstname", firstname);
//                                user.put("lastname", lastname);
//                                user.put("emailID", emailid);
//                                user.put("phoneNum", phonenum);
//                                user.put("city", city);
//                                user.put("category", category);
//                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                db.collection("ServiceProviders")
//                                        .document(uid)
//                                        .set(user)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void avoid) {
//
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//
//                                            }
//                                        });


                                Intent intent = new Intent(CreateAccount.this, sp_homepage.class);
                                intent.putExtra("phonenumber", phonenum);
                                startActivity(intent);
                            } else {
                                Toast.makeText(CreateAccount.this, "This mail Number is already registered", Toast.LENGTH_SHORT).show();
                            }
//
                        }
                    });
        }

    }

    void createCustomer() {
        final String firstname = firstName.getText().toString().trim();
        final String lastname = lastName.getText().toString().trim();
        final String emailid = emailId.getText().toString();
        final String phonenum = phoneNum.getText().toString();
        final String city = cityList.getSelectedItem().toString();
        final String pword = password.getText().toString();
        final String cpword = secondPassword.getText().toString();
        if (firstName.getText().toString().trim().equalsIgnoreCase(""))
            firstName.setError("First name is required!");

        else if (lastName.getText().toString().trim().equalsIgnoreCase(""))
            lastName.setError("Last name is required!");

        else if (emailId.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString()).matches())
            emailId.setError("Enter valid Email Id!");

        else if (phoneNum.getText().toString().length() < 10)
            phoneNum.setError("Enter valid Phone Number!");


        else if (cityList.getSelectedItem().toString().matches("Select City")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Select City!",
                    Toast.LENGTH_LONG);
            toast.show();
        } else if (password.getText().toString().length() < 8)
            password.setError("Password of minimum 8 characters is required");
        else if (!password.getText().toString().matches(secondPassword.getText().toString()))
            secondPassword.setError("Passwords do not match. Try again!");
        else {


            final Bundle bundle = new Bundle();
            bundle.putString("firstname", firstname);
            bundle.putString("lastname", lastname);
            bundle.putString("emailid", emailid);
            bundle.putString("phonenum", phonenum);
            bundle.putString("city", city);


            mAuth.createUserWithEmailAndPassword(emailid, pword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
//

                            if (task.isSuccessful()) {
                                final String phone = phoneNum.getText().toString();
//
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> user = new HashMap<>();
                                user.put("firstname", firstname);
                                user.put("lastname", lastname);
                                user.put("emailID", emailid);
                                user.put("phoneNum", phonenum);
                                user.put("city", city);
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                db.collection("Customers")
                                        .document(uid)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void avoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                                Intent intent = new Intent(CreateAccount.this, Main2Activity.class);
                                intent.putExtra("phonenumber", phonenum);

                                startActivity(intent);
                            } else {
                                Toast.makeText(CreateAccount.this, "This mail Number is already registered", Toast.LENGTH_SHORT).show();
                            }
//
                        }
                    });

        }

    }
}

