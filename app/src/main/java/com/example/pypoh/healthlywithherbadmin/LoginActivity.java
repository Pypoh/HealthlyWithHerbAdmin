package com.example.pypoh.healthlywithherbadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    EditText etEmail, etPass;
    Button btnLogin, btntoSignup;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //binding
        etEmail = findViewById(R.id.et_email_login);
        etPass = findViewById(R.id.et_pass_login);
        btnLogin = findViewById(R.id.btn_login);
        btntoSignup = findViewById(R.id.btn_to_signup);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(this);
        btntoSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                btnLogin.setEnabled(false);
                if(checkLogin()){
                    String email = etEmail.getText().toString().trim();
                    String pass = etPass.getText().toString().trim();

                    loginUser(email, pass);
                }
                break;
            case R.id.btn_to_signup:
                toSignUp();
                break;
        }
    }

    private void toSignUp() {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loginUser(String email, String pass) {
        dialogShow();
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    toMain();
                    Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                    btnLogin.setEnabled(true);
                }
            }
        });
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

    private void toMain() {
        dialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean checkLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        boolean valid ;

        if (!email.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!pass.isEmpty()) {
                    if (pass.length() >= 6) {
                        valid = true;
                    } else {
                        etPass.setError("Password Harus Lebih Dari 6 Digit");
                        etPass.requestFocus();
                        valid = false;
                    }
                } else {
                    etPass.setError("Password kosong");
                    etPass.requestFocus();
                    valid = false;
                }
            } else {
                etEmail.setError("Email Tidak Valid");
                etEmail.requestFocus();
                valid = false;
            }
        } else {
            etEmail.setError("Email kosong");
            etEmail.requestFocus();
            valid = false;
        }
        return valid;
    }
}
