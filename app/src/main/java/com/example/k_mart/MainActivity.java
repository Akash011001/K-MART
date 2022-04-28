package com.example.k_mart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    TextView loginText;
    Button registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        registerbtn = (Button) findViewById(R.id.register_btn_main);
        loginText = findViewById(R.id.activity_main_textview);

        //mauth.getCurrentUser()!=null
        if(mauth.getCurrentUser()!=null){
            registerbtn.setText("Open");
            loginText.setVisibility(View.GONE);
        }else {
            registerbtn.setText("Register");
            loginText.setVisibility(View.VISIBLE);

        }

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mauth.getCurrentUser()==null) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mauth.getCurrentUser()!=null
                if(mauth.getCurrentUser()!=null) {
                    Intent i = new Intent(MainActivity.this, PlatformActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}