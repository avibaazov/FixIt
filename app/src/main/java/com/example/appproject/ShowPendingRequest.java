package com.example.appproject;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowPendingRequest extends AppCompatActivity {

    CircleImageView dp;
    private LinearLayout call, message, directions;
    TextView name;
    TextView job,time,date,des;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Button accept,decline;
    String userid,requestid;
    List<RequestBox> mchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pending_request);

        name = findViewById(R.id.chat_name);
        job = findViewById(R.id.chat_job);
        dp = findViewById(R.id.chat_dp);

        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        des = findViewById(R.id.description);

        accept = findViewById(R.id.accept_btn);
        decline = findViewById(R.id.decline_btn);

        call = findViewById(R.id.call);
        message = findViewById(R.id.message);
        directions = findViewById(R.id.directions);




        userid = getIntent().getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Customers").document(userid);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Customer s = documentSnapshot.toObject(Customer.class);
                    String mname = s.getFirstname() + " " + s.getLastname();
                    name.setText(mname);
                    job.setText(s.getPhoneNum());
                    dp.setImageResource(R.drawable.cuslogo);
                    readMessage(firebaseUser.getUid(),userid);
                } else {

                }
            }
        });

         db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("Requestlist").document(firebaseUser.getUid()).collection("active");

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    RequestList requestList = documentSnapshot.toObject(RequestList.class);
                    if (requestList.getStatus().equals("active")) {
                        Toast.makeText(ShowPendingRequest.this, "You already have an active request", Toast.LENGTH_SHORT).show();
                        accept.setClickable(false);
                        accept.setBackgroundColor(Color.GRAY);
                        break; // Exit the loop if an active request is found
                    }
                }
            }
        });




        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + getIntent().getStringExtra("phone")));
                startActivity(intent);

            }});
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowPendingRequest.this,Chat.class);
                intent.putExtra("userid",getIntent().getStringExtra("userid"));
                intent.putExtra("type","Customer");
                startActivity(intent);

            }});




        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             FirebaseFirestore   db = FirebaseFirestore.getInstance();
                db.collection("Requests").document(requestid)
                        .update("status", "active")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("Requestlist").document(userid)
                                        .collection("active").document(firebaseUser.getUid())
                                        .set(new RequestList("active"))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                db.collection("Requestlist").document(firebaseUser.getUid())
                                                        .collection("active").document(userid)
                                                        .set(new RequestList("active"))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                finish();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore   db = FirebaseFirestore.getInstance();
                db.collection("Requests").document(requestid)
                        .update("status", "declined")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("Requestlist").document(userid)
                                        .collection("active").document(firebaseUser.getUid())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                db.collection("Requestlist").document(firebaseUser.getUid())
                                                        .collection("active").document(userid)
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                finish();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
            }
        });


    }
    private void readMessage(final String myid, final String userid){
        mchat = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference requestsRef = db.collection("Requests");

        requestsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }
                mchat.clear();
                for (QueryDocumentSnapshot snapshot : querySnapshot) {
                    RequestBox requestBox = snapshot.toObject(RequestBox.class);
                    if (requestBox.getReceiver().equals(myid) && requestBox.getSender().equals(userid) ||
                            requestBox.getReceiver().equals(userid) && requestBox.getSender().equals(myid) &&
                                    requestBox.getStatus().equals("pending")) {
                        date.setText(requestBox.getDate());
                        time.setText(requestBox.getTime());
                        des.setText(requestBox.getDescription());
                        requestid = requestBox.getId();
                    }
                }
            }
        });


    }

}
