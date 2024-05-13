package com.example.uptrend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class privacy_policy_CA extends AppCompatActivity {

    TextView back_txt;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy_ca);

//
//        back_txt = findViewById(R.id.back);
//        status=getIntent().getStringExtra("status");
//
//        back_txt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), signUp_and_logIn_page.class);
//                i.putExtra("status", status);
//                startActivity(i);
//                finish();
//            }
//        });
    }
}