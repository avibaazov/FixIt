package com.example.appproject;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Chat extends AppCompatActivity {


    TextView name;
    TextView job;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    EditText message;
    ImageView send;
    MessageAdapter messageAdapter;
    List<ChatBox> mchat;
    RecyclerView recyclerView;
    String userid,type;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        name = findViewById(R.id.chat_name);
        job = findViewById(R.id.chat_job);
        message = findViewById(R.id.chat_message);
        send = findViewById(R.id.send_chat);

        recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userid = getIntent().getStringExtra("userid");
        type = getIntent().getStringExtra("type");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(type.equals("Customer")){
            DocumentReference docRef = db.collection("Customers").document(userid);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                       // Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Customer s = snapshot.toObject(Customer.class);
                        String mname = s.getFirstname() + " " + s.getLastname();
                        name.setText(mname);
                        job.setText(s.getPhoneNum());

                        readMessage(firebaseUser.getUid(),userid);
                    } else {

                    }
                }
            });
        } else {
            DocumentReference docRef = db.collection("ServiceProviders").document(userid);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {

                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        ServiceProvider s = snapshot.toObject(ServiceProvider.class);
                        String mname = s.getFirstname() + " " + s.getLastname();
                        name.setText(mname);
                        job.setText(s.getCategory());

                        readMessage(firebaseUser.getUid(),userid);
                    } else {

                    }
                }
            });
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }
                else{
                    Toast.makeText(Chat.this,"Type a message to send",Toast.LENGTH_SHORT).show();
                }
                message.setText("");
            }
        });



    }

    private void sendMessage(String sender, String receiver, String message){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> chatData = new HashMap<>();
        chatData.put("sender", sender);
        chatData.put("receiver", receiver);
        chatData.put("message", message);

        CollectionReference chatsRef = db.collection("Chats");
        chatsRef.add(chatData)
                .addOnSuccessListener(documentReference -> {
                    DocumentReference chatlistRef = db.collection("Chatlist")
                            .document(firebaseUser.getUid())
                            .collection("users")
                            .document(userid);
                    chatlistRef.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (!documentSnapshot.exists()) {
                                    Map<String, Object> chatlistData = new HashMap<>();
                                    chatlistData.put("id", userid);
                                    chatlistRef.set(chatlistData);
                                }
                            })
                            .addOnFailureListener(e -> Log.d(TAG, "Error checking chatlist: " + e));

                    DocumentReference reverseChatlistRef = db.collection("Chatlist")
                            .document(userid)
                            .collection("users")
                            .document(firebaseUser.getUid());
                    reverseChatlistRef.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (!documentSnapshot.exists()) {
                                    Map<String, Object> chatlistData = new HashMap<>();
                                    chatlistData.put("id", firebaseUser.getUid());
                                    reverseChatlistRef.set(chatlistData);
                                }
                            })
                            .addOnFailureListener(e -> Log.d(TAG, "Error checking reverse chatlist: " + e));
                })
                .addOnFailureListener(e -> Log.d(TAG, "Error adding chat message: " + e));

    }

    private void readMessage(final String myid, final String userid){
        mchat = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Chats")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mchat.clear();
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }
                        for (QueryDocumentSnapshot document : value) {
                            ChatBox chatBox = document.toObject(ChatBox.class);
                            if (chatBox.getReceiver().equals(myid) && chatBox.getSender().equals(userid) ||
                                    chatBox.getReceiver().equals(userid) && chatBox.getSender().equals(myid)) {
                                mchat.add(chatBox);
                            }
                        }
                        messageAdapter = new MessageAdapter(Chat.this, mchat);
                        recyclerView.setAdapter(messageAdapter);
                    }
        });
    }
}
