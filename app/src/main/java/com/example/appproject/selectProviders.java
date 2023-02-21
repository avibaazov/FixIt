package com.example.appproject;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class selectProviders extends AppCompatActivity {

    TextView heading;
    private RecyclerView recyclerView;
    private ListAdapter useradapter;
    private List<ServiceProvider> musers;
    ProgressBar pbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_providers);

        pbar = findViewById(R.id.progressBar);
        heading = findViewById(R.id.service_text);
        final String check = getIntent().getStringExtra("buttontext");
        String display = check + " in your city";
        heading.setText(display);
        recyclerView = findViewById(R.id.service_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        musers = new ArrayList<>();
        readUsers();

    }

    public void readUsers() {
        final String check = getIntent().getStringExtra("buttontext");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference serviceProvidersRef = db.collection("ServiceProviders");
        serviceProvidersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    musers.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.get("category").toString().equalsIgnoreCase(check)) {
                            ServiceProvider serviceProviders = document.toObject(ServiceProvider.class);
                            musers.add(serviceProviders);
                        }
                    }
                    pbar.setVisibility(GONE);
                    useradapter = new ListAdapter(selectProviders.this, musers);
                    recyclerView.setAdapter(useradapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }
}