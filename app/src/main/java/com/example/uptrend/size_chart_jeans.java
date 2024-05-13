package com.example.uptrend;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class size_chart_jeans extends AppCompatActivity {


    LinearLayout jeans_shirt_layout, jeans_img, size_jeans_layout_W, jeans_img_W,layoutMenBottom,layoutWomenBottom;
    String chart,productId;
    TextView txtBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_chart_jeans);


        jeans_shirt_layout = findViewById(R.id.size_layout_jeans);
        jeans_img = findViewById(R.id.size_image_jeans);
        size_jeans_layout_W = findViewById(R.id.size_layout_jeans_W);
        jeans_img_W = findViewById(R.id.size_image_jeans_W);
        layoutMenBottom=findViewById(R.id.layoutMenBottom);
        layoutWomenBottom=findViewById(R.id.layoutWomenBottom);

        txtBack=findViewById(R.id.btnBack);

        chart=getIntent().getStringExtra("chart");
        productId=getIntent().getStringExtra("productId");
        if(chart.equals("Men's(Bottom)")){
            layoutMenBottom.setVisibility(View.VISIBLE);
            layoutWomenBottom.setVisibility(View.GONE);
        } else if (chart.equals("Women's(Bottom)")) {
            layoutWomenBottom.setVisibility(View.VISIBLE);
            layoutMenBottom.setVisibility(View.GONE);
        }

        jeans_shirt_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (jeans_img.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(jeans_img, new AutoTransition());

                jeans_img.setVisibility(var);
            }
        });
        size_jeans_layout_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (jeans_img_W.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(jeans_img_W, new AutoTransition());

                jeans_img_W.setVisibility(var);
            }
        });

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i=new Intent(getApplicationContext(),open_product.class);
               i.putExtra("productId",productId);
               startActivity(i);
               finish();
            }
        });
    }
}