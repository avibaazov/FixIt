package com.example.appproject.locale_lite.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject.Chatlist;
import com.example.appproject.Customer;
import com.example.appproject.ListAdapter;
import com.example.appproject.R;
import com.example.appproject.ServiceProvider;
import com.example.appproject.ShowMessageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragmentSP extends Fragment {

    private RecyclerView recyclerView;
    private ShowMessageAdapter listAdapter;
    private List<Customer> musers;
    FirebaseFirestore db;
    FirebaseUser fuser;
    DatabaseReference database;

    private List<Chatlist> userList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard_sp, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();
        db= FirebaseFirestore.getInstance();
        db.collection("Chatlist").document(fuser.getUid())
                .collection("Chatlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Chatlist c = document.toObject(Chatlist.class);
                                userList.add(c);
                            }
                        }
                        chatList();
                    }
                });


        return view;
    }

    private void chatList(){
        musers = new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        db.collection("ServiceProviders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Customer c = document.toObject(Customer.class);
                                for(Chatlist chatlist : userList){
                                    if(c.getId().equals(chatlist.getId())){
                                        musers.add(c);
                                    }
                                }
                            }
                        }
                        listAdapter = new ShowMessageAdapter(getContext(),musers);
                        recyclerView.setAdapter(listAdapter);
                    }
                });


    }


}