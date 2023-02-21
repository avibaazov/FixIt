package com.example.appproject.locale_lite.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appproject.Chatlist;
import com.example.appproject.Customer;
import com.example.appproject.ListAdapter;
import com.example.appproject.R;
import com.example.appproject.ServiceProvider;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<ServiceProvider> musers;
    FirebaseFirestore db;
    FirebaseUser fuser;
    DatabaseReference database;
    private DashboardViewModel DashboardViewModel;
    private List<Chatlist> userList;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();


      /*DashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
            db=FirebaseFirestore.getInstance();
        db.collection("Chatlist").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                          @Override
                                                          public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                                                              if (error != null) {

                                                                  return;
                                                              }
                                                              userList.clear();
                                                              for (QueryDocumentSnapshot document : querySnapshot) {
                                                                  Chatlist chatlist = document.toObject(Chatlist.class);
                                                                  userList.add(chatlist);
                                                              }
                                                              chatList();
                                                          }
                                                      });*/
//
//        });
           // chatList();
        return view;
    }

    private void chatList(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference serviceProvidersRef = db.collection("ServiceProviders");

        musers = new ArrayList<>();
        serviceProvidersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    return;
                }
                musers.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    ServiceProvider c = document.toObject(ServiceProvider.class);
                    for (Chatlist chatlist : userList) {
                        if (c.getId().equals(chatlist.getId())) {
                            musers.add(c);
                        }
                    }
                }
                listAdapter = new ListAdapter(getContext(), musers);
                recyclerView.setAdapter(listAdapter);
            }
        });



    }


}