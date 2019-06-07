package com.example.pypoh.healthlywithherbadmin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pypoh.healthlywithherbadmin.model.DataItem;
import com.example.pypoh.healthlywithherbadmin.model.DataNarasumber;
import com.example.pypoh.healthlywithherbadmin.widget.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    private String KEY;
    private String SOURCE_KEY;
    private boolean fromPost = false;
    private String userID;

    // Dataset
    private List<DataItem> dataSet = new ArrayList<>();
    private List<DataNarasumber> dataSetNarasumber = new ArrayList<>();

    // Firebase
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private DatabaseReference itemRef;
    private FirebaseUser currentUser;
    private DatabaseReference userPostRef;

    // Content
    private TextView nama, penyakit, harga, info, narasumber, jumlahPost, reputasiPenjual, waktuPengiriman;
    private Button acceptBtn, declineBtn;
    private ImageView itemImage, narasumberImage;
    private CarouselView carouselItem;
    private Toolbar toolbar;

    // Carousel
    private CarouselView carouselView;
    private int[] sampleImages = {R.drawable.sunset, R.drawable.sunset};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        toolbar = findViewById(R.id.toolbar_detail);
        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_detail);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {
                KEY = extras.getString("KEY");
                SOURCE_KEY = extras.getString("SOURCEKEY");
                fromPost = extras.getBoolean("FROMPOST");
            }
        } else {
            KEY = (String) savedInstanceState.getSerializable("KEY");
            SOURCE_KEY = (String) savedInstanceState.getSerializable("SOURCEKEY");
            fromPost = (boolean) savedInstanceState.getSerializable("FROMPOST");
        }

        // Set Refs
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("Users");
        itemRef = database.getReference("DataObat");

        // Get User ID
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();

        userPostRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Posts");

        bindView();
        loadUserData();
        setupCarousel();
//        setView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void bindView() {
        nama = findViewById(R.id.detail_nama);
        penyakit = findViewById(R.id.detail_penyakit);
        info = findViewById(R.id.tv_info_obat);
        narasumber = findViewById(R.id.tv_nama_narasumber);
        jumlahPost = findViewById(R.id.tv_jumlah_post);
        reputasiPenjual = findViewById(R.id.tv_reputasi);
        narasumberImage = findViewById(R.id.image_narasumber);
        carouselItem = (CarouselView) findViewById(R.id.carouselItem);
        acceptBtn = findViewById(R.id.accept_btn);
        declineBtn = findViewById(R.id.decline_btn);
    }

    private void setView() {
        final DataItem dataItem = dataSet.get(0);
        DataNarasumber dataNarasumber = dataSetNarasumber.get(0);
        nama.setText(dataItem.getNama());
        penyakit.setText(dataItem.getPenyakit());
        info.setText(dataItem.getDeskripsi());
        narasumber.setText(dataNarasumber.getUsername());
        jumlahPost.setText(dataNarasumber.getJumlahPost() + " post");
        reputasiPenjual.setText(dataNarasumber.getReputasi());

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPostRef.child(dataItem.getUID()).child("status").setValue("Accepted");
                String itemID = itemRef.push().getKey();
                dataItem.setStatus("Accepted");
                dataItem.setUID(null);
                itemRef.child(itemID).setValue(dataItem);
            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPostRef.child(dataItem.getUID()).child("status").setValue("Rejected");
            }
        });

        carouselItem.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {

            }
        });

        GlideApp.with(getApplicationContext())
                .load(dataNarasumber.getGambar())
                .placeholder(R.drawable.place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(narasumberImage);
    }

    private void setupCarousel() {
        carouselItem.setPageCount(1);
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
//                setImage(imageView);
            }
        };
        carouselItem.setImageListener(imageListener);
        carouselItem.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(ItemDetailActivity.this, "Carousel Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setImage(ImageView image) {
        GlideApp.with(this)
                .load(dataSet.get(0).getGambar())
                .placeholder(R.drawable.place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
    }

    private void loadData() {
        if (fromPost) {
            userPostRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSet.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        DataItem dataItem = ds.getValue(DataItem.class);
                        dataItem.setUID(ds.getKey());
                        dataSet.add(dataItem);
                    }
                    setView();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            itemRef.child(KEY).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSet.clear();
                    DataItem dataItem = dataSnapshot.getValue(DataItem.class);
                    dataSet.add(dataItem);
                    setView();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void loadUserData() {
        userRef.child(SOURCE_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSetNarasumber.clear();
                DataNarasumber dataNarasumber = dataSnapshot.getValue(DataNarasumber.class);
                dataSetNarasumber.add(dataNarasumber);
                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
