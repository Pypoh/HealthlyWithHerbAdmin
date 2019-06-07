package com.example.pypoh.healthlywithherbadmin.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pypoh.healthlywithherbadmin.R;
import com.example.pypoh.healthlywithherbadmin.adapter.PostAdapter;
import com.example.pypoh.healthlywithherbadmin.model.DataItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    private RecyclerView recyclerView;
    private DatabaseReference itemRef;
    private List<DataItem> dataSet = new ArrayList<>();
    private PostAdapter postAdapter;


    private String userID;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //binding
        recyclerView = v.findViewById(R.id.rv_list_post_home);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        itemRef = FirebaseDatabase.getInstance().getReference().child("DataObat");

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        postAdapter = new PostAdapter(getContext(), dataSet);
        recyclerView.setAdapter(postAdapter);

        loadData();
        
        return v;
    }

    private void loadData() {
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSet.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DataItem dataItem = ds.getValue(DataItem.class);
                    dataItem.setUID(ds.getKey());
                    dataSet.add(dataItem);
                }
                postAdapter.setItems(dataSet);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
