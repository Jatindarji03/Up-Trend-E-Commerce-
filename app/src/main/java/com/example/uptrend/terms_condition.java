package com.example.uptrend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class terms_condition extends AppCompatActivity {

    TextView back_txt1;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

//        back_txt1 = findViewById(R.id.back1);
//        status=getIntent().getStringExtra("status");
//        back_txt1.setOnClickListener(new View.OnClickListener() {
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