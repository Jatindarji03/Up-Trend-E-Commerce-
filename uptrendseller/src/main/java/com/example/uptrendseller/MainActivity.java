package com.example.uptrendseller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3500;
    SpinKitView spinKitView;
    Animation middle_slide1;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        spinKitView = findViewById(R.id.spinKitView);

        middle_slide1 = AnimationUtils.loadAnimation(this, R.anim.middle);

        spinKitView.setAnimation(middle_slide1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    startActivity(new Intent(getApplicationContext(), dashboard_admin.class));
                    finish();
                } else {
                    startActivity(new Intent(MainActivity.this, tutorial.class));
                    finish();
                }

            }
        }, 4000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}