package com.example.uptrenddelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.adapteranddatamodel.Pattern;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import DataModel.Admin;
import DataModel.DeliveryBoy;
import io.github.muddz.styleabletoast.StyleableToast;

public class delivery_create_account extends AppCompatActivity {

    EditText name_ca_dy,mobile_ca_dy,email_ca_dy,password_ca_dy;
    private DeliveryBoy deliveryBoy;

    Animation top;

    RelativeLayout rl_anime1;


    TextView login_ca,txt_percentage,login_txt,txt_pass_msg1;

    ProgressBar progressbar_ca;
    int valueProgress=0;
    private CheckBox checkBox3;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    AppCompatButton btnCreateAccount,btn_shine2;
    Timer timer;
    loadingDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_create_account);

        name_ca_dy = findViewById(R.id.name_ca_delivery);
        mobile_ca_dy = findViewById(R.id.mobile_no_ca_delivery);
        email_ca_dy = findViewById(R.id.email_ca_delivery);
        password_ca_dy = findViewById(R.id.password__ca_delivery);
        checkBox3 = findViewById(R.id.checkbox_ca_dy);

        deliveryBoy=new DeliveryBoy();
        timer=new Timer();
        loading=new loadingDialog(this);

        progressbar_ca= findViewById(R.id.progress_create_account);
        txt_percentage = findViewById(R.id.text_ca_percentage);


        login_txt = findViewById(R.id.txt_login2);
        login_ca = findViewById(R.id.login_txt);
        txt_pass_msg1 = findViewById(R.id.password_msg1);
        btnCreateAccount = findViewById(R.id.btn_ca_delivery);


        rl_anime1 = findViewById(R.id.top_anim2);

        top = AnimationUtils.loadAnimation(this,R.anim.top);
        rl_anime1.setAnimation(top);


        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("DeliveryBoy");



        btn_shine2 = findViewById(R.id.shine_btn_Sign2);

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
                    password_ca_dy.setTransformationMethod(null);
                }else {
                    password_ca_dy.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        password_ca_dy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    txt_pass_msg1.setText("Password must be at list 6 characters");

                }
            }
        });

        login_ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),delivery_log_in.class));
            }
        });

        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),delivery_log_in.class));
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryBoy.setDeliveryBoyName(name_ca_dy.getText().toString().trim());
                deliveryBoy.setDeliveryBoyEmail(email_ca_dy.getText().toString().trim());
                deliveryBoy.setDeliveryBoyMobileNumber(mobile_ca_dy.getText().toString().trim());
                if(validInput(deliveryBoy,password_ca_dy.getText().toString().trim())){
                    createDeliveryBoyId(deliveryBoy,password_ca_dy.getText().toString().trim());
                }
            }
        });

        ChangeColour.changeColour(getApplicationContext(),mobile_ca_dy,R.drawable.edittext_touch_effect,R.drawable.phone_vector_effect);
        ChangeColour.changeColour(getApplicationContext(),name_ca_dy,R.drawable.edittext_touch_effect,R.drawable.person_vector_effect);
        ChangeColour.changeColour(getApplicationContext(),email_ca_dy,R.drawable.edittext_touch_effect,R.drawable.email_vector_effect);
        ChangeColour.changeColour(getApplicationContext(),password_ca_dy,R.drawable.edittext_touch_effect,R.drawable.lock_vector_effect);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        valueProgress=sharedPreferences.getInt("process",0);
        if(valueProgress>=0){
            progressbar_ca.setProgress(valueProgress);
            txt_percentage.setText(valueProgress+"%");
        }
    }
    private void updateProgress(int value)
    {
        progressbar_ca.setProgress(value);
        txt_percentage.setText(valueProgress+"%");
    }
    public void createDeliveryBoyId(DeliveryBoy deliveryBoy,String password){
        loading.show();
        auth.createUserWithEmailAndPassword(deliveryBoy.getDeliveryBoyEmail(),password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    deliveryBoy.setDeliveryBoyId(auth.getCurrentUser().getUid());
                    databaseReference.push().setValue(deliveryBoy);
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    valueProgress=sharedPreferences.getInt("process",0);
                    valueProgress+=20;
                    editor.putInt("process",valueProgress);
                    editor.apply();
                    updateProgress(valueProgress);
                    startActivity(new Intent(getApplicationContext(), delivery_boy_info.class));
                }else{
                    loading.cancel();
                    ChangeColour.errorColour(
                            getApplicationContext(),
                            email_ca_dy,
                            "Email is existed",
                            R.drawable.edittext_error_effect,
                            R.drawable.email_vector_red_error
                    );
                    StyleableToast.makeText(getApplicationContext(),"This Email is already existed",R.style.UptrendToast).show();
                }
            }
        });
    }
    private void shineStart() {
        Animation animation = new TranslateAnimation(
                0, btnCreateAccount.getWidth() + btn_shine2.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        btn_shine2.startAnimation(animation);

    }

    private boolean validInput(DeliveryBoy deliveryBoy, String password) {
        if (TextUtils.isEmpty(deliveryBoy.getDeliveryBoyEmail())) {
            ChangeColour.errorColour(getApplicationContext(),
                    email_ca_dy,
                    "Email is required",
                    R.drawable.edittext_error_effect,
                    R.drawable.email_vector_red_error
            );
            return false;
        } else if (!isValidEmail(deliveryBoy.getDeliveryBoyEmail())) {
            ChangeColour.errorColour(getApplicationContext(),
                    email_ca_dy,
                    "Invalid Email",
                    R.drawable.edittext_error_effect,
                    R.drawable.email_vector_red_error
            );
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            ChangeColour.errorColour(getApplicationContext(),
                    password_ca_dy,
                    "Password is required",
                    R.drawable.edittext_error_effect,
                    R.drawable.lock_vector_red_error
            );
            return false;
        } else if (password.length()<=5) {
            ChangeColour.errorColour(getApplicationContext(),
                    password_ca_dy,
                    "Password is Too short Min 6 Char",
                    R.drawable.edittext_error_effect,
                    R.drawable.lock_vector_red_error
            );
            return false;
        }
        if(TextUtils.isEmpty(deliveryBoy.getDeliveryBoyMobileNumber())){
            ChangeColour.errorColour(getApplicationContext(),
                    mobile_ca_dy,
                    "Mobile Number is required",
                    R.drawable.edittext_error_effect,
                    R.drawable.phone_vector_red_error
            );
            return false;
        }else if (!Pattern.isValidMobileNumber(deliveryBoy.getDeliveryBoyMobileNumber())) {
            ChangeColour.errorColour(getApplicationContext(),
                    mobile_ca_dy,
                    "Invalid Mobile Number",
                    R.drawable.edittext_error_effect,
                    R.drawable.phone_vector_red_error
            );
            return false;
        }
        if(TextUtils.isEmpty(deliveryBoy.getDeliveryBoyName())){
            ChangeColour.errorColour(getApplicationContext(),
                    name_ca_dy,
                    "Name is required",
                    R.drawable.edittext_error_effect,
                    R.drawable.person_vector_red_error
            );
            return false;
        }else if (!Pattern.isValidName(deliveryBoy.getDeliveryBoyName())) {
            ChangeColour.errorColour(getApplicationContext(),
                    name_ca_dy,
                    "This Filed Only Contain Character",
                    R.drawable.edittext_error_effect,
                    R.drawable.person_vector_red_error
            );
            return false;
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
