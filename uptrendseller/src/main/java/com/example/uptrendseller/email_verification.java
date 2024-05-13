package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.github.muddz.styleabletoast.StyleableToast;

public class email_verification extends AppCompatActivity {

    ProgressBar progressBar2;
    int valueProgress = 0;
    LinearLayout linearLayout_0;
    CardView cardView_0;
    private loadingDialog loading;

    AppCompatButton btn_verify,btn_shine1;

    TextView txt_percentage_verification,txt_email,txt_verify,click_info_0;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);


        auth=FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressbar_email_verification);
        txt_percentage_verification = findViewById(R.id.text_percentage_email_verification);
        txt_email = findViewById(R.id.txt_email);
        txt_verify = findViewById(R.id.txt_verify);
        btn_verify = findViewById(R.id.btn_verification);
        loading=new loadingDialog(this);

        txt_email.setText(getIntent().getStringExtra("email"));




        //Initialization for instruction effect
        click_info_0 = findViewById(R.id.instruction_click1);
        linearLayout_0 = findViewById(R.id.linearLayout1);
        cardView_0 = findViewById(R.id.cardView1);

        //initialization shine button
        btn_shine1 = findViewById(R.id.shine_btn1);

        //That can be used to cancel or checked execution
        ScheduledExecutorService scheduledExecutorService =
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

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), seller_information.class));
                verifyEmail(txt_email.getText().toString().trim());
//                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                valueProgress = sharedPreferences.getInt("process", 0);
//                valueProgress += 20;
//                editor.putInt("process", valueProgress);
//                editor.apply();
//                updateProgress(valueProgress);
                //startActivity(new Intent(getApplicationContext(),seller_information.class));
            }
        });
        click_info_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (cardView_0.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(linearLayout_0, new AutoTransition());

                cardView_0.setVisibility(var);
            }
        });

    }
    @Override
    protected void onStart () {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        valueProgress = sharedPreferences.getInt("process", 0);
        if (valueProgress >= 0) {
            progressBar2.setProgress(valueProgress);
            txt_percentage_verification.setText(valueProgress + "%");
        }
    }

    private void updateProgress ( int value)
    {
        progressBar2.setProgress(value);
        txt_percentage_verification.setText(valueProgress + "%");
    }
    public void verifyEmail(String email){
        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    StyleableToast.makeText(getApplicationContext(),"Check Your Email",R.style.UptrendToast).show();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        if(auth.getCurrentUser().isEmailVerified()){
                            loading.show();
                            txt_verify.setText("Your Email is Verified...");
                            txt_verify.setTextColor(getColor(R.color.green));
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            valueProgress = sharedPreferences.getInt("process", 0);
                            valueProgress += 25;
                            editor.putInt("process", valueProgress);
                            editor.apply();
                            updateProgress(valueProgress);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(), seller_information.class));
                                    finish();
                                    loading.cancel();
                                }
                            },2500);
                        }
                    }
                }
            });
        }
    }
    private void shineStart() {
        Animation animation = new TranslateAnimation(
                0, btn_verify.getWidth() + btn_shine1.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        btn_shine1.startAnimation(animation);

    }
}