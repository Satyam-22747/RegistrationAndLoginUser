package com.satdroid.registrationandloginuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextInputLayout emailIn, passwordIn;
    private AppCompatButton register_button;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView reg_page;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(Login.this,home.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        emailIn=findViewById(R.id.input_email_login);
        passwordIn=findViewById(R.id.input_password_login);
        register_button=findViewById(R.id.login_btn);
        reg_page=findViewById(R.id.registration_page);
        progressBar=findViewById(R.id.pg_bar_login);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        reg_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regPage=new Intent(Login.this, MainActivity.class);
                startActivity(regPage);
                finish();
            }
        });

    }

    void loginUser(){
        progressBar.setVisibility(View.VISIBLE);
        String email, password;
        email=emailIn.getEditText().getText().toString().trim();
        password=passwordIn.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "Please enter all details", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(Login.this, "Login success.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Login.this,home.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
}