package com.example.pypoh.healthlywithherbadmin;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pypoh.healthlywithherbadmin.fragment.HomeFragment;
import com.example.pypoh.healthlywithherbadmin.fragment.PostFragment;
import com.example.pypoh.healthlywithherbadmin.fragment.ProfileFragment;
import com.example.pypoh.healthlywithherbadmin.model.DataItem;
import com.example.pypoh.healthlywithherbadmin.model.DataItemsList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private Toolbar toolbar;

    // User Data
    private String userUID;

    // Firebase
    private FirebaseDatabase database;
    private DatabaseReference keranjangRef;
    private DatabaseReference itemDataRef;

    // Dataset
    private List<DataItemsList> dataSetList = new ArrayList<>();
    private List<DataItem> dataSetItem = new ArrayList<>();

    // Fragment
    private HomeFragment homeFragment = new HomeFragment();
    private PostFragment postFragment= new PostFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    boolean doubleBackToExitPressedOnce = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(homeFragment);
                    return true;
                case R.id.navigation_post:
                    setFragment(postFragment);
                    return true;
                case R.id.navigation_profile:
                    setFragment(profileFragment);
                    return true;

            }
            return false;
        }
    };

    private void setFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame,fragment);
//        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment(homeFragment);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
