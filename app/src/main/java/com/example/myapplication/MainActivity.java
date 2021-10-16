package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.login.StartActivity;
import com.example.myapplication.util.Auth;

public class MainActivity extends AppCompatActivity {

    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            Auth.signOut(this);
            startActivity(new Intent(this, StartActivity.class));
            finish();
        });
    }
}
