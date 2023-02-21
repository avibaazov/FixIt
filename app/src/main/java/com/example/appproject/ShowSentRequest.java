package com.example.appproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class ShowSentRequest extends AppCompatActivity {


    TextView name;
    TextView job;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String userid, type;
    List<RequestBox> mchat;
    RequestAdapter requestAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_request);

        name = findViewById(R.id.chat_name);
        job = findViewById(R.id.chat_job);


        recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userid = getIntent().getStringExtra("userid");
        type = getIntent().getStringExtra("type");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (type.equals("ServiceProvider")) {
            db.collection("ServiceProviders").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ServiceProvider s = documentSnapshot.toObject(ServiceProvider.class);
                    String mname = s.getFirstname() + " " + s.getLastname();
                    name.setText(mname);
                    job.setText(s.getCategory());
                    readMessage(firebaseUser.getUid(), userid);
                }
            });
        } else {
            db.collection("Customers").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    Customer s = documentSnapshot.toObject(Customer.class);
                    String mname = s.getFirstname() + " " + s.getLastname();
                    name.setText(mname);
                    job.setText(s.getPhoneNum());

                    readMessage(firebaseUser.getUid(), userid);
                }
            });
        }


    }

    private void readMessage(final String myid, final String userid) {
        mchat = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Requests").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    return;
                }


                mchat.clear();
                for (QueryDocumentSnapshot document : snapshot) {
                    RequestBox requestBox = document.toObject(RequestBox.class);
                    if (requestBox.getReceiver().equals(myid) && requestBox.getSender().equals(userid) ||
                            requestBox.getReceiver().equals(userid) && requestBox.getSender().equals(myid)) {
                        mchat.add(requestBox);
                    }
                    requestAdapter = new RequestAdapter(ShowSentRequest.this, mchat);
                    recyclerView.setAdapter(requestAdapter);
                }
            }
        });
    }
}
