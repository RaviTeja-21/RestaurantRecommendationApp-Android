package com.example.foodworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Welcomescreen extends AppCompatActivity {
    TextView  privacypolicy;
    Button welcomeregister;
    Button welcomelogin;
    String text = "<font color=#000>By logging in or registering, you have agreed to the </font> <font color=#E90000>Terms and Conditions and Privacy Policy.</font>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomescreen);

        privacypolicy = findViewById(R.id.privacypolicy);
        welcomelogin = findViewById(R.id.welcomelogin);
        welcomeregister = findViewById(R.id.welcomeregister);

        privacypolicy.setText(Html.fromHtml(text));

        welcomelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcomescreen.this,LoginActivity.class));
            }
        });

        welcomeregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcomescreen.this,RegisterActivity.class));
            }
        });
    }
}