package com.example.uptrendseller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.github.muddz.styleabletoast.StyleableToast;

public class agreement_seller extends AppCompatActivity {


    TextView txt1,txt2;
    AppCompatButton btnContinue,shine_btn;
    CheckBox checkBox1,checkBox2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_seller);


        txt1 = findViewById(R.id.terms_txt);
        txt2 = findViewById(R.id.privacy_txt);
        checkBox1=findViewById(R.id.checkbox1);
        checkBox2=findViewById(R.id.checkbox2);

        shine_btn = findViewById(R.id.shine_button);
        btnContinue=findViewById(R.id.btnContinue);

        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread((new Runnable() {
                    @Override
                    public void run() {
                        shineStart();
                    }
                }));
            }
        }, 1, 2, TimeUnit.SECONDS);



        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),condition_use_seller.class));
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),privacy_policy_seller.class));
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxIsChecked()){

                }
            }
        });
    }

    private boolean checkBoxIsChecked() {
        if (!checkBox2.isChecked() || !checkBox1.isChecked()) {
            StyleableToast.makeText(getApplicationContext(),"Please You Can Select Our Terms & Conditions",R.style.UptrendToast).show();
            return false;
        }else{
            startActivity(new Intent(getApplicationContext(), sucessfull_seller.class));
            finish();
            return true;
        }
    }
    private void shineStart() {
        Animation animation = new TranslateAnimation(
                0, btnContinue.getWidth() + shine_btn.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        shine_btn.startAnimation(animation);

    }
}
