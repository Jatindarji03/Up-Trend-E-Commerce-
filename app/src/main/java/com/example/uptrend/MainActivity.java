package com.example.uptrend;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4300;
    TextView black,txt;
    Animation top, bottom,middle;
    MediaPlayer mp;
   ImageView animation,animation2;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);




        if (Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        animation = findViewById(R.id.animation);
        animation2 = findViewById(R.id.animation2);
        black = findViewById(R.id.black);
        txt = findViewById(R.id.text);

        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        middle = AnimationUtils.loadAnimation(this, R.anim.middle);

        animation.setAnimation(top);
        animation2.setAnimation(bottom);
        txt.setAnimation(middle);

        user = FirebaseAuth.getInstance().getCurrentUser();



        animation.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.setVisibility(View.GONE);
            }

        },1500);

        black.postDelayed(new Runnable() {
            @Override
            public void run() {
                black.setVisibility(View.GONE);
            }

        },1400);





        mp = MediaPlayer.create(MainActivity.this, R.raw.fire_song);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mp.start();
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    Intent intent = new Intent(MainActivity.this, select_language.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, select_language.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, SPLASH_SCREEN);
    }
}