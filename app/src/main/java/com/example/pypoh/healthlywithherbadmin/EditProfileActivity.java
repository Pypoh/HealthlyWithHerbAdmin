package com.example.pypoh.healthlywithherbadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSimpan;
    private EditText etUsername, etNotelp, etAlamat, etEmail;
    private TextView tvEditFoto;
    private CircularImageView imgUser;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private StorageReference storageReference;

    private String dbBergabung, dbImage;
    private static int REQUSET_CAMERA = 1;
    private static int SELECT_FILE = 0;
    private ProgressDialog dialog;

    @Override
    protected void onStart() {
        super.onStart();
        setDataUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //binding
        btnSimpan = findViewById(R.id.btn_simpan_editprofile);
        etEmail = findViewById(R.id.et_email_editprofile);
        etUsername = findViewById(R.id.et_username_editprofile);
        etNotelp = findViewById(R.id.et_notelp_editprofile);
        etAlamat = findViewById(R.id.et_alamat_editprofile);
        tvEditFoto = findViewById(R.id.tv_editfoto_editprofile);
        imgUser = findViewById(R.id.cimg_editprofile);

        //firebase binding
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("users_image");

        tvEditFoto.setOnClickListener(this);
        btnSimpan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_editfoto_editprofile:
                selectImage();
                break;
            case R.id.btn_simpan_editprofile:
                if (checkMasukkan()) {
                    dialogShow();
                    updateDataUser();
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);

        builder.setTitle("Profile Pict");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUSET_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUSET_CAMERA) {

            } else if (requestCode == SELECT_FILE) {

            }
        }
    }

    private void uploadImage(){

    }


    private void setDataUser() {
        reference.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                etEmail.setText(dataSnapshot.child("Email").getValue(String.class));
                etUsername.setText(dataSnapshot.child("Username").getValue(String.class));
                etNotelp.setText(dataSnapshot.child("NomorTelepon").getValue(String.class));
                etAlamat.setText(dataSnapshot.child("Alamat").getValue(String.class));
                dbImage = dataSnapshot.child("Image").getValue(String.class);
                dbBergabung = dataSnapshot.child("Bergabung").getValue(String.class);

                Glide.with(EditProfileActivity.this)
                        .load(dbImage)
                        .into(imgUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ErrorSetDataEditProfile", databaseError.getMessage());
            }
        });
    }

    private boolean checkMasukkan() {
        String username = etUsername.getText().toString().trim();
        String notelp = etNotelp.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();
        boolean valid;

        if (!username.isEmpty()) {
            if (!notelp.isEmpty()) {
                if (notelp.length() > 8) {
                    if (!alamat.isEmpty()) {
                        valid = true;
                    } else {
                        etAlamat.setError("Alamat Kosong");
                        etAlamat.requestFocus();
                        valid = false;
                    }
                } else {
                    etNotelp.setError("Nomor Telepon Tidak Valid");
                    etNotelp.requestFocus();
                    valid = false;
                }
            } else {
                etNotelp.setError("Nomor Telepon Kosong");
                etNotelp.requestFocus();
                valid = false;
            }
        } else {
            etUsername.requestFocus();
            etUsername.setError("Username Kosong");
            valid = false;
        }
        return valid;
    }

    private void dialogShow() {
        dialog = new ProgressDialog(this, R.style.AppTheme_NoActionBar) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.progress_dialog);
                getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        };
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void updateDataUser() {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String notelp = etNotelp.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();

        reference.child(auth.getUid()).child("Username").setValue(username);
        reference.child(auth.getUid()).child("NomorTelepon").setValue(notelp);
        reference.child(auth.getUid()).child("Alamat").setValue(alamat);

        dialog.dismiss();
        Toast.makeText(this, "Updating Data User", Toast.LENGTH_SHORT).show();
    }
}
