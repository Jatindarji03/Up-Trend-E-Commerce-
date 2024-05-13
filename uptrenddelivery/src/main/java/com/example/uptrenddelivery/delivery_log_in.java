package com.example.uptrenddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.github.muddz.styleabletoast.StyleableToast;

public class delivery_log_in extends AppCompatActivity {

    EditText email_dy,password_dy;
    loadingDialog loading;
    Timer timer;
    private FirebaseAuth auth;
    DatabaseReference databaseReference;

    TextView forget_pass_dy,new_account_dy,signUp_txt;

    private CheckBox checkBox3;

    private static int SPLASH_SCREEN=3800;

    Animation top;

    RelativeLayout rl_anime;

    AppCompatButton btn_dy,btn_shine1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_log_in);


        email_dy = findViewById(R.id.email_login_delivery);
        password_dy = findViewById(R.id.password_login_delivery);
        checkBox3 = findViewById(R.id.checkbox_login_dy);
        btn_dy = findViewById(R.id.btn_login_delivery);


        forget_pass_dy = findViewById(R.id.forget_password_delivery);
        new_account_dy = findViewById(R.id.new_account_txt);
        signUp_txt = findViewById(R.id.txt_signup1);


        rl_anime = findViewById(R.id.top_anim);

        top = AnimationUtils.loadAnimation(this,R.anim.top);
        rl_anime.setAnimation(top);


        btn_shine1 = findViewById(R.id.shine_btn_Sign1);


        timer=new Timer();

        databaseReference= FirebaseDatabase.getInstance().getReference("DeliveryBoy");
        auth=FirebaseAuth.getInstance();
        loading=new loadingDialog(this);

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

        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    password_dy.setTransformationMethod(null);
                }else {
                    password_dy.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        signUp_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),delivery_create_account.class));
            }
        });

        new_account_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),delivery_create_account.class));
            }
        });
        forget_pass_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),forgetPass_dy.class));
            }
        });
        btn_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=email_dy.getText().toString().trim();
                String password=password_dy.getText().toString().trim();
                if(validInputForLogin(email,password)){
                    FirebaseUtils.checkUserEmailInDatabase(databaseReference, email, new FirebaseUtils.EmailCheckListener() {
                        @Override
                        public void onEmailCheckResult(boolean emailExists) {
                            if(emailExists){
                                loginDeliveryBoy(email,password);
                            }else{
                                ChangeColour.errorColour(
                                        getApplicationContext(),
                                        email_dy,
                                        "Wrong Email",
                                        R.drawable.edittext_error_effect,
                                        R.drawable.email_vector_red
                                );

                            }
                        }
                    });

                }
            }
        });
    }
    private void shineStart() {
        Animation animation = new TranslateAnimation(
                0, btn_dy.getWidth() + btn_shine1.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        btn_shine1.startAnimation(animation);

    }
    public void loginDeliveryBoy(String email,String password){

        loading.show();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            startActivity(new Intent(getApplicationContext(),dashboard_delivery.class));
                            finish();
                            // load2.setVisibility(View.GONE);
                        }
                    },3200);

                }else{

                    loading.cancel();

                    ChangeColour.errorColour(getApplicationContext(),
                            password_dy,
                            "Wrong Password",
                            R.drawable.edittext_error_effect,
                            R.drawable.key_vector_red_error
                    );
                    StyleableToast.makeText(getApplicationContext(),"Login Failed",R.style.UptrendToast).show();
                }
            }
        });
    }
    private boolean validInputForLogin(String email,String password){

        if (TextUtils.isEmpty(email)) {
            ChangeColour.errorColour(getApplicationContext(),
                    email_dy,
                    "Email is required",
                    R.drawable.edittext_error_effect,
                    R.drawable.email_vector_red_error
            );
            return false;
        } else if (!isValidEmail(email)) {
            ChangeColour.errorColour(getApplicationContext(),
                    email_dy,
                    "Invalid Email",
                    R.drawable.edittext_error_effect,
                    R.drawable.email_vector_red_error
            );
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            ChangeColour.errorColour(getApplicationContext(),
                    password_dy,
                    "Password is required",
                    R.drawable.edittext_error_effect,
                    R.drawable.key_vector_red_error
            );
            return false;
        } else if (password.length()<=5) {
            ChangeColour.errorColour(getApplicationContext(),
                    password_dy,
                    "Password is Too short Min 6 Char",
                    R.drawable.edittext_error_effect,
                    R.drawable.key_vector_red_error
            );
            return false;
        }
        return true;
    }

    // this method is for email is valid or not
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}