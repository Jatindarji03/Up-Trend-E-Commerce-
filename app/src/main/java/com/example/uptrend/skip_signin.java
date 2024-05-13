package com.example.uptrend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class skip_signin extends AppCompatActivity {

    AppCompatButton btnSignIn, btnCreateAccount, btn_shine1, btn_shine2;

    private static int SPLASH_SCREEN=3800;

    Animation top;

    RelativeLayout rl_anime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip_signin);

        //initialization 3 button
        btnSignIn = findViewById(R.id.btnSignIn);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        //initialization for top animation
        rl_anime = findViewById(R.id.relative_anime);


        //For top animation
        top = AnimationUtils.loadAnimation(this,R.anim.top24);
        rl_anime.setAnimation(top);

        //initialization shine button
        btn_shine1 = findViewById(R.id.shine_btn1);
        btn_shine2 = findViewById(R.id.shine_btn2);

        //That can be used to cancel or checked execution
        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService scheduledExecutorService1 =
                Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService scheduledExecutorService2 =
                Executors.newSingleThreadScheduledExecutor();


        //scheduleAtFixedRate-->It is used to repeated execution again and again
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
        scheduledExecutorService1.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread((new Runnable() {
                    @Override
                    public void run() {
                        shineStart1();
                    }
                }));
            }
        }, 1, 2, TimeUnit.SECONDS);



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), signUp_and_logIn_page.class);
                i.putExtra("status", "SignIn");
                startActivity(i);
                finish();
            }
        });
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), signUp_and_logIn_page.class);
                i.putExtra("status", "CreateAccount");
                startActivity(i);
                finish();
            }
        });

    }

    //Methods for 3 Button shining
    private void shineStart() {
        Animation animation = new TranslateAnimation(
                0, btnSignIn.getWidth() + btn_shine1.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        btn_shine1.startAnimation(animation);

    }
    private void shineStart1() {
        Animation animation = new TranslateAnimation(
                0, btnCreateAccount.getWidth() + btn_shine2.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        btn_shine2.startAnimation(animation);
    }
}
