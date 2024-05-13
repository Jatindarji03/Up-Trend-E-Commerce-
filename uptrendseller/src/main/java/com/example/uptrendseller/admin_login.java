package com.example.uptrendseller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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

public class admin_login extends AppCompatActivity {

    private EditText email_SI,password_SI;
    private CheckBox checkBox2;


    loadingDialog loading;
    Timer timer;

    AppCompatButton btn_login,shine1;
    private FirebaseAuth auth;

    TextView txt,forgetPass_txt,txt_sign,marquee_txt;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        email_SI = findViewById(R.id.email_login);
        password_SI = findViewById(R.id.password_login);
        forgetPass_txt=findViewById(R.id.forget_password);
        checkBox2 = findViewById(R.id.checkbox_signin);
        btn_login= findViewById(R.id.btn_login_admin);

        txt = findViewById(R.id.new_account_txt);
        txt_sign = findViewById(R.id.signUp_txt);

        shine1 = findViewById(R.id.shine_btn_Sign);

        marquee_txt = findViewById(R.id.marquee_text);
        marquee_txt.setSelected(true);

        timer=new Timer();

        databaseReference= FirebaseDatabase.getInstance().getReference("Admin");
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

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=email_SI.getText().toString().trim();
                String password=password_SI.getText().toString().trim();
                if(validInputForLogin(email,password)){
                    FirebaseUtils.checkUserEmailInDatabase(databaseReference, email, new FirebaseUtils.EmailCheckListener() {
                        @Override
                        public void onEmailCheckResult(boolean emailExists) {
                            if(emailExists){
                                loginAdmin(email,password);
                            }else{
                                ChangeColour.errorColour(
                                        getApplicationContext(),
                                        email_SI,
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


        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    password_SI.setTransformationMethod(null);
                }else {
                    password_SI.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        forgetPass_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),forget_pass_seller.class));
            }
        });
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),admin_create_account.class));
            }
        });
        txt_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),admin_create_account.class));
            }
        });

        ChangeColour.changeColour(
                getApplicationContext(),
                password_SI,
                R.drawable.edittext_touch_effect,
                R.drawable.key_vector_effect
        );
        ChangeColour.changeColour(
                getApplicationContext(),
                email_SI,R.drawable.edittext_touch_effect,
                R.drawable.email_vector_effect
        );
    }
    public void loginAdmin(String email,String password){

        loading.show();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            startActivity(new Intent(getApplicationContext(),dashboard_admin.class));
                            finish();
                            // load2.setVisibility(View.GONE);
                        }
                    },3200);

                }else{

                    loading.cancel();

                    ChangeColour.errorColour(getApplicationContext(),
                            password_SI,
                            "Wrong Password",
                            R.drawable.edittext_error_effect,
                            R.drawable.key_vector_red_error
                    );
                    StyleableToast.makeText(getApplicationContext(),"Login Failed",R.style.UptrendToast).show();
                }
            }
        });
    }
    private void shineStart() {
        Animation animation = new TranslateAnimation(
                0, btn_login.getWidth()+shine1.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        shine1.startAnimation(animation);
    }

    //Validation For EditText

    private boolean validInputForLogin(String email,String password){

        if (TextUtils.isEmpty(email)) {
            ChangeColour.errorColour(getApplicationContext(),
                    email_SI,
                    "Email is required",
                    R.drawable.edittext_error_effect,
                    R.drawable.email_vector_red_error
            );
            return false;
        } else if (!isValidEmail(email)) {
            ChangeColour.errorColour(getApplicationContext(),
                    email_SI,
                    "Invalid Email",
                    R.drawable.edittext_error_effect,
                    R.drawable.email_vector_red_error
            );
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            ChangeColour.errorColour(getApplicationContext(),
                    password_SI,
                    "Password is required",
                    R.drawable.edittext_error_effect,
                    R.drawable.key_vector_red_error
            );
            return false;
        } else if (password.length()<=5) {
            ChangeColour.errorColour(getApplicationContext(),
                    password_SI,
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