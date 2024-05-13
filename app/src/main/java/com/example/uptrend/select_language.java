package com.example.uptrend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class select_language extends AppCompatActivity {

    RadioGroup radioGroup;
    loadingDialog loadingDialog;

    TextView txt;
    Timer timer;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        user= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String status = sharedPreferences.getString("status", "");
        String code = sharedPreferences.getString("code", "");
        if (user!=null) {
            setLanguage(code);
            startActivity(new Intent(getApplicationContext(), home.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        radioGroup = findViewById(R.id.radioGroup);


        loadingDialog = new loadingDialog(this);
        timer=new Timer();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                    changeLanguage(checkedId);
                }

        });


    }
    public void setLanguage(String language_code) {
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(language_code);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    /*
        it will change the language when radio button is selected and navigate the
        next activity.
     */
    public void changeLanguage(int checkedId){
        if (checkedId==R.id.hindi) {
            //load1.setVisibility(View.VISIBLE);
            loadingDialog.show();
            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("status", "login");
            editor.putString("code", "hi");
            editor.apply();
            //setLanguage("hi");
            loading("hi");
            //startActivity(new Intent(getApplicationContext(), skip_signin.class));
        } else if (checkedId==R.id.telugu) {
            //load1.setVisibility(View.VISIBLE);
            loadingDialog.show();
            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("status", "login");
            editor.putString("code", "te");
            editor.apply();
            //setLanguage("te");
            loading("te");
            //startActivity(new Intent(getApplicationContext(), skip_signin.class));
        } else if (checkedId==R.id.punjabi) {
            //load1.setVisibility(View.VISIBLE);
            loadingDialog.show();
            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("status", "login");
            editor.putString("code", "pa");
            editor.apply();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setLanguage("pa");
                    startActivity(new Intent(getApplicationContext(),skip_signin.class));
                }
            },3000);
            //setLanguage("pa");
            //loading("pa");
            //startActivity(new Intent(getApplicationContext(), skip_signin.class));
        } else if (checkedId==R.id.english) {
           // load1.setVisibility(View.VISIBLE);
            loadingDialog.show();
            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("status", "login");
            editor.putString("code", "en");
            editor.apply();
            //setLanguage("en");
            loading("en");
            //startActivity(new Intent(getApplicationContext(), skip_signin.class));
        } else if (checkedId==R.id.marathi) {
           // load1.setVisibility(View.VISIBLE);
            loadingDialog.show();
            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("status", "login");
            editor.putString("code", "mr");
            editor.apply();
            //setLanguage("mr");
            loading("mr");
            //startActivity(new Intent(getApplicationContext(), skip_signin.class));
        } else if (checkedId==R.id.gujarati) {
            //load1.setVisibility(View.VISIBLE);
            loadingDialog.show();
            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("status", "login");
            editor.putString("code", "gu");
            editor.apply();
            //setLanguage("gu");
            loading("gu");
            //startActivity(new Intent(getApplicationContext(), skip_signin.class));
        }
    }
    public void loading(String languageCode){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setLanguage(languageCode);
                startActivity(new Intent(getApplicationContext(), skip_signin.class));
                finish();
            }
        },3000);
    }
}