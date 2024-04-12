package com.satdroid.registrationandloginuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class home extends AppCompatActivity {

    FirebaseAuth Fauth;
    TextView currentuser;
    AppCompatButton logout;
    FirebaseUser fuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentuser=findViewById(R.id.current_user);
        logout=findViewById(R.id.logout_button);
        Fauth=FirebaseAuth.getInstance();
        fuser=Fauth.getCurrentUser();

        if(fuser==null)
        {
            Intent inext=new Intent(home.this, Login.class);
            startActivity(inext);
            finish();
        }
        else
        {
            currentuser.setText(fuser.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent inext=new Intent(home.this, Login.class);
                startActivity(inext);
                finish();

            }
        });
    }
}