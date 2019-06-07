package com.example.pypoh.healthlywithherbadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUp;
    private EditText etUsername, etEmail, etPass, etRePass, etNotelp, etAlamat;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String imageDb = "";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //binding
        etUsername = findViewById(R.id.et_username_signup);
        etEmail = findViewById(R.id.et_email_signup);
        etPass = findViewById(R.id.et_password_signup);
        etRePass = findViewById(R.id.et_repassword_signup);
//        etNotelp = findViewById(R.id.et_notelp_signup);
//        etAlamat = findViewById(R.id.et_alamat_signup);
        btnSignUp = findViewById(R.id.btn_signup);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                if (checkSignup()) {
                    String email = etEmail.getText().toString().trim();
                    String pass = etPass.getText().toString().trim();
                    String rePass = etRePass.getText().toString().trim();
                    String username = etUsername.getText().toString().trim();

                    signUpuser(email, pass, rePass, username);
                }
                break;
        }
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

    private void signUpuser(final String email, String pass, String rePass, final String username) {
        dialogShow();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setDataUser(email, username);
                } else {
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Gagal Mendaftar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDataUser(String email, String username) {
        HashMap data = new HashMap();
        data.put("Username", username);
        data.put("Email", email);
        data.put("Bergabung", getDate());
        data.put("Reputasi", "Pengguna Baru");
        data.put("JumlahPost", 0);
        data.put("Image", "https://firebasestorage.googleapis.com/v0/b/fishgo-7d2ae.appspot.com/o/users_image%2Ficon_profile.png?alt=media&token=f955a66f-b3c5-410d-824b-68a3e9ec2f35");
        String uid = auth.getUid();

        reference.child(uid).setValue(data);

        toMain();
        Toast.makeText(this, "Berhasil Mendaftar", Toast.LENGTH_SHORT).show();
    }

    private void toMain() {
        dialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean checkSignup() {
        String email = etEmail.getText().toString().trim();
//        String notelp = etNotelp.getText().toString().trim();
//        String alamat = etAlamat.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String rePass = etRePass.getText().toString().trim();
        String username = etUsername.getText().toString().trim();

        boolean valid;

        if (!username.isEmpty()) {
            if (!email.isEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    if (!pass.isEmpty()) {
                        if (pass.length() > 6) {
                            if (!rePass.isEmpty()) {
                                if (pass.equalsIgnoreCase(rePass)) {
                                    valid = true;
                                } else {
                                    etRePass.setError("Password tidak sama");
                                    etRePass.requestFocus();
                                    valid = false;
                                }
                            } else {
                                etRePass.setError("Password masih kosong");
                                etRePass.requestFocus();
                                valid = false;
                            }
                        } else {
                            etPass.setError("Password harus lebih dari 6 digit");
                            etPass.requestFocus();
                            valid = false;
                        }
                    } else {
                        etPass.setError("Password Kosong");
                        etPass.requestFocus();
                        valid = false;
                    }
                } else {
                    etEmail.setError("Email tidak valid");
                    etEmail.requestFocus();
                    valid = false;
                }
            } else {
                etEmail.requestFocus();
                etEmail.setError("Email Kosong");
                valid = false;
            }
        } else {
            etUsername.requestFocus();
            etUsername.setError("Username masih kosong");
            valid = false;
        }
        return valid;
    }

    private String getImage() {
        storageReference.child("users_image/icon_profile.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageDb = uri.toString();
                Log.i("GetImageDB", imageDb);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("ErrorGetImageDB", e.getMessage());
            }
        });
        return imageDb;
    }

    private String getDate() {
        String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        return date;
    }
}
