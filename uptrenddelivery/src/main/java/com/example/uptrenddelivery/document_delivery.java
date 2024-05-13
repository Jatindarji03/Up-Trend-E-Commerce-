package com.example.uptrenddelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import DataModel.DeliveryBoyDocument;
import DataModel.DeliveryBoyInformation;

public class document_delivery extends AppCompatActivity {

    TextView txt_per_doc, v1, v2, v3, v4, v6, v7;
    ProgressBar progressbar_doc;

    LinearLayout linearLayout;
    private DeliveryBoyDocument deliveryBoyDocument;
    Animation top;
    RelativeLayout rl_anime5;
    int valueProgress = 0;
    EditText adhar_id, rc_book, driving_lc_no, pan_card_no, bank_holder_no, ac_no_delivery,ifsc_code;
    AppCompatButton btn_document_dy, btn_shine5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_delivery);


        progressbar_doc = findViewById(R.id.progress_doc);
        txt_per_doc = findViewById(R.id.text_per_doc);

        adhar_id = findViewById(R.id.adhar_card_dc);
        rc_book = findViewById(R.id.vehicle_rc_book);
        driving_lc_no = findViewById(R.id.driving_licence_no);
        pan_card_no = findViewById(R.id.pan_card_no);
        bank_holder_no = findViewById(R.id.bank_holder_name);
        ac_no_delivery = findViewById(R.id.account_no);
        ifsc_code = findViewById(R.id.ifsc_code_dy);
        btn_document_dy = findViewById(R.id.btn_delivery_document);

        deliveryBoyDocument = new DeliveryBoyDocument();

        v1 = findViewById(R.id.verify1);
        v2 = findViewById(R.id.verify2);
        v3 = findViewById(R.id.verify3);
        v4 = findViewById(R.id.verify4);
        v6 = findViewById(R.id.verify6);
        v7 = findViewById(R.id.verify7);

        rl_anime5 = findViewById(R.id.top_anim5);
        top = AnimationUtils.loadAnimation(this, R.anim.top);
        rl_anime5.setAnimation(top);

        btn_shine5 = findViewById(R.id.shine_btn5);


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

        adhar_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    v1.setText("Verify");

                }
            }
        });

        pan_card_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    v2.setText("Verify");

                }
            }
        });
        driving_lc_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    v3.setText("Verify");

                }
            }
        });

        rc_book.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    v4.setText("Verify");

                }
            }
        });

        ac_no_delivery.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    v6.setText("Verify");

                }
            }
        });


        ifsc_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    v7.setVisibility(View.VISIBLE);
                    v7.setText("Click To Verify");
                    v7.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            null,
                            null,
                            ContextCompat.getDrawable(getApplicationContext(),R.drawable.verify_vector),
                            null
                    );

                }
            }
        });
//


        btn_document_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryBoyDocument.setDeliveryBoyAdharCard(adhar_id.getText().toString().trim());
                deliveryBoyDocument.setDeliveryBoyRcBook(rc_book.getText().toString().trim());
                deliveryBoyDocument.setDeliveryBoyLicNo(driving_lc_no.getText().toString().trim());
                deliveryBoyDocument.setDeliveryBoyPanCardNo(pan_card_no.getText().toString().trim());
                deliveryBoyDocument.setDeliveryBoyAccountName(bank_holder_no.getText().toString().trim());
                ;
                deliveryBoyDocument.setDeliveryBoyAccountNo(ac_no_delivery.getText().toString().trim());
                if (validInput(deliveryBoyDocument)) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    valueProgress = sharedPreferences.getInt("process", 0);
                    valueProgress += 20;
                    editor.putInt("process", valueProgress);
                    editor.apply();
                    updateProgress(valueProgress);
                    startActivity(new Intent(getApplicationContext(), agreement_delivery.class));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        valueProgress = sharedPreferences.getInt("process", 0);
        if (valueProgress >= 0) {
            progressbar_doc.setProgress(valueProgress);
            txt_per_doc.setText(valueProgress + "%");
        }
    }

    private void updateProgress(int value) {
        progressbar_doc.setProgress(value);
        txt_per_doc.setText(valueProgress + "%");
    }

    private boolean validInput(DeliveryBoyDocument deliveryBoyDocument) {
        if (TextUtils.isEmpty(deliveryBoyDocument.getDeliveryBoyAdharCard())) {
            adhar_id.setError("required.");
            adhar_id.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(deliveryBoyDocument.getDeliveryBoyRcBook())) {
            rc_book.setError("Required.");
            rc_book.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(deliveryBoyDocument.getDeliveryBoyLicNo())) {
            driving_lc_no.setError("required.");
            driving_lc_no.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(deliveryBoyDocument.getDeliveryBoyPanCardNo())) {
            pan_card_no.setError("Pan Number is required.");
            pan_card_no.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(deliveryBoyDocument.getDeliveryBoyAccountName())) {
            bank_holder_no.setError(" required.");
            bank_holder_no.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(deliveryBoyDocument.getDeliveryBoyAccountNo())) {
            ac_no_delivery.setError("Required");
            ac_no_delivery.requestFocus();
        }
        return true;

    }

    private void shineStart() {
        Animation animation = new TranslateAnimation(
                0, btn_document_dy.getWidth() + btn_shine5.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        btn_shine5.startAnimation(animation);

    }

    public void DetailsVisible(){
        int isVisible= linearLayout.getVisibility();
        if (isVisible== View.VISIBLE){
            linearLayout.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}

