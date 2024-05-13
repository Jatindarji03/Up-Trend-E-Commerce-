package com.example.uptrend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class splash_return_pd extends AppCompatActivity {


    RelativeLayout keep_shopping,view_all_orders;

    TextView back_btn,txtCancel,txtReturn;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_return_pd);



        keep_shopping = findViewById(R.id.keep_shopping);
        view_all_orders= findViewById(R.id.view_all_orders);
        back_btn = findViewById(R.id.back_btn55);
        txtCancel=findViewById(R.id.txtCancel);
        txtReturn=findViewById(R.id.txtReturn);

        status=getIntent().getStringExtra("status");
        if(status.equals("cancel")){
            txtReturn.setVisibility(View.GONE);
            txtCancel.setVisibility(View.VISIBLE);
        } else if (status.equals("return")) {
            txtCancel.setVisibility(View.GONE);
            txtReturn.setVisibility(View.VISIBLE);
        }


        keep_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home.class));
            }
        });

        view_all_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), complete_order.class));
            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), complete_order.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), complete_order.class));
        finish();
    }
}