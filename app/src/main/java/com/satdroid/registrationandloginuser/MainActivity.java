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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
public class MainActivity extends AppCompatActivity {
   private   TextInputLayout emailIn, passwordIn, cfm_passwordIn;
 private   AppCompatButton register_button;
   private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView login_page;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(MainActivity.this,home.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        emailIn=findViewById(R.id.input_email);
        passwordIn=findViewById(R.id.input_password);
        cfm_passwordIn=findViewById(R.id.confirm_password);
        register_button=findViewById(R.id.register_btn);
        progressBar=findViewById(R.id.pg_bar);
        login_page=findViewById(R.id.login_page);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
        login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPage=new Intent(MainActivity.this,Login.class);
                startActivity(loginPage);
                finish();
            }
        });
    }

    public void RegisterUser()
    {
        progressBar.setVisibility(View.VISIBLE);
        String email, password, cnfmPassword;
                email=emailIn.getEditText().getText().toString().trim();
        password=passwordIn.getEditText().getText().toString().trim();
        cnfmPassword=cfm_passwordIn.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||TextUtils.isEmpty(cnfmPassword)) {
            Toast.makeText(MainActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(cnfmPassword.equals(password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,"Registratin Success",Toast.LENGTH_SHORT).show();

                                String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference=FirebaseDatabase.getInstance().getReference("User").child(userid);

                                HashMap<String,String> User_details_hash=new HashMap<>();
                                User_details_hash.put("Email",email);
                                User_details_hash.put("Password",password);
                                databaseReference.setValue(User_details_hash).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(MainActivity.this,"User data saved",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            Intent loginPage=new Intent(MainActivity.this,Login.class);
                                            startActivity(loginPage);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"Registratin success but data not saved",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            Intent loginPage=new Intent(MainActivity.this,Login.class);
                                            startActivity(loginPage);
                                            finish();
                                        }
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Something Error",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }

        else {
            Toast.makeText(MainActivity.this,"Password Mismatched",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);

        }
    }


}