package com.example.appproject;

import static android.view.View.GONE;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Requests extends AppCompatActivity {
    FirebaseFirestore db;
    TextView heading;
    private RecyclerView recyclerView;
    ProgressBar pbar;
    private ListAdapter listAdapter;
    private List<ServiceProvider> musers;

    FirebaseUser fuser;
    DatabaseReference database;
    private List<RequestList> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        pbar = findViewById(R.id.progressBar);
        heading = findViewById(R.id.request_text);
        final String check = getIntent().getStringExtra("buttontext");
        heading.setText(check);
        recyclerView = findViewById(R.id.request_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        db.collection("RequestList").document(fuser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                userList.clear();
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    for (String key : data.keySet()) {
                        RequestList chatlist = documentSnapshot.toObject(RequestList.class);
                        userList.add(chatlist);
                    }
                    chatList();
                }
            }
        });
    }

    private void chatList() {
        musers = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("ServiceProviders");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ServiceProvider c = dataSnapshot1.getValue(ServiceProvider.class);
                    for (RequestList chatlist : userList) {
                        if (c.getId().equals(chatlist.getId())) {
                            musers.add(c);
                        }
                    }
                    pbar.setVisibility(GONE);
                }
                listAdapter = new ListAdapter(Requests.this, musers);
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
