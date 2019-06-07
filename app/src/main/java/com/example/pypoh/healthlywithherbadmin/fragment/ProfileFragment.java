package com.example.pypoh.healthlywithherbadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pypoh.healthlywithherbadmin.EditProfileActivity;
import com.example.pypoh.healthlywithherbadmin.LoginActivity;
import com.example.pypoh.healthlywithherbadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    public ProfileFragment() {
        // Required empty public constructor
    }


    private ImageView tes;
    private CircularImageView imgUser;
    private TextView tvUsername, tvDate, tvEdit, tvPost, tvLogout;
    private DatabaseReference reference;
    String img = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        //binding
        imgUser = v.findViewById(R.id.cimg_profile);
        tvUsername = v.findViewById(R.id.tv_username_profile);
        tvDate = v.findViewById(R.id.tv_date_profile);
        tvEdit = v.findViewById(R.id.tv_edit_profile);
        tvPost = v.findViewById(R.id.tv_buat_postingan);
        tvLogout = v.findViewById(R.id.tv_logout);

        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent toNewPost = new Intent(getContext(), NewPostActivity.class);
//                startActivity(toNewPost);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent toLogin = new Intent(getContext(), LoginActivity.class);
                startActivity(toLogin);
                getActivity().finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        setDataUser(reference);

        tes = v.findViewById(R.id.tes);

        tvEdit.setOnClickListener(this);
        return v;
    }


    private void setDataUser(DatabaseReference reference) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();

        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvUsername.setText(dataSnapshot.child("Username").getValue(String.class));
                tvDate.setText(dataSnapshot.child("Bergabung").getValue(String.class));

                String image = dataSnapshot.child("Image").getValue(String.class);
                Glide.with(ProfileFragment.this)
                        .load(image)
                        .into(imgUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ERRORPROFILE", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit_profile:
                toEdit();
                break;
        }
    }


    private void toEdit() {
        startActivity(new Intent(getContext(), EditProfileActivity.class));
    }
}
