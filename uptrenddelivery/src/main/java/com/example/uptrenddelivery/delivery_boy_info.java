package com.example.uptrenddelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import DataModel.DeliveryBoyInformation;

public class delivery_boy_info extends AppCompatActivity {

    EditText name_info_dy,address_info_dy,vehicle_no_dy,driver_no_dy;

    private DatePicker dataPicker;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private TextView ageTextview, currentDateTextview;

    Animation top;
    RelativeLayout rl_anime4;


    TextView txt_info_per;
    private DeliveryBoyInformation deliveryBoyInformation;

    ProgressBar progressbar_info;
    int valueProgress=0;

    Spinner citySpinner,stateSpinner,vehiclespinner;

    AppCompatButton btn_dy_info,btn_shine4;
    ArrayAdapter<CharSequence>cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_info);

//        citySpinner = findViewById(R.id.spinner_city);
//        stateSpinner = findViewById(R.id.spinner_state);
//        vehiclespinner = findViewById(R.id.spinner_vehicle);
        btn_dy_info = findViewById(R.id.btn_delivery_info);

        progressbar_info= findViewById(R.id.progress_delivery_info);
        txt_info_per = findViewById(R.id.text_per_info);
        name_info_dy=findViewById(R.id.name_dy_info);
        address_info_dy=findViewById(R.id.address_dy_info);
        vehicle_no_dy=findViewById(R.id.vehicle_no_dy);
        driver_no_dy=findViewById(R.id.driver_no_dy);

        rl_anime4 = findViewById(R.id.top_anim4);



        top = AnimationUtils.loadAnimation(this, R.anim.top);
        rl_anime4.setAnimation(top);


        btn_shine4 = findViewById(R.id.shine_btn4);







        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("DeliveryBoyInfo");


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

//
//
//        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this,R.array.state_spinner,
//                android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence>vehicleAdapter = ArrayAdapter.createFromResource(this,R.array.vehicle_spinner,
//                android.R.layout.simple_spinner_item);
//
//        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        stateSpinner.setAdapter(stateAdapter);
//        vehiclespinner.setAdapter(vehicleAdapter);

        deliveryBoyInformation=new DeliveryBoyInformation();
        dataPicker = findViewById(R.id.datePicker);
        ageTextview = findViewById(R.id.ageTextview);
        currentDateTextview = findViewById(R.id.currentDateTextview);


        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = DateFormat.getDateInstance();///

        String formattedDate = dateFormat.format(currentDate);
        currentDateTextview.setText(formattedDate);
//
//        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                deliveryBoyInformation.setState(getResources().getStringArray(R.array.state_spinner)[i]);
//                changeCity(deliveryBoyInformation.getState());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                deliveryBoyInformation.setCity(adapterView.getItemAtPosition(i).toString());
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        vehiclespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                deliveryBoyInformation.setVehicleType(getResources().getStringArray(R.array.vehicle_spinner)[i]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//
//
        btn_dy_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryBoyInformation.setDeliveryBoyName(name_info_dy.getText().toString().trim());
                deliveryBoyInformation.setDeliveryBoyAddress(address_info_dy.getText().toString().trim());
                deliveryBoyInformation.setDeliveryBoyVehicleNumber(vehicle_no_dy.getText().toString().trim());
                deliveryBoyInformation.setDeliveryBoyMobileNumber(driver_no_dy.getText().toString().trim());
                if(validInput(deliveryBoyInformation) && calculateAge(view)) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    valueProgress = sharedPreferences.getInt("process", 0);
                    valueProgress += 20;
                    editor.putInt("process", valueProgress);
                    editor.apply();
                    updateProgress(valueProgress);
                    startActivity(new Intent(getApplicationContext(), document_delivery.class));
                }
            }
        });
    }


    private boolean validInput(DeliveryBoyInformation deliveryBoyInformation){
        if(TextUtils.isEmpty(deliveryBoyInformation.getDeliveryBoyName())){
            name_info_dy.setError("Name is required.");
            name_info_dy.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(deliveryBoyInformation.getDeliveryBoyAddress())){
            address_info_dy.setError("Address is Required.");
            address_info_dy.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(deliveryBoyInformation.getDeliveryBoyVehicleNumber())){
            vehicle_no_dy.setError("Vehicle Number is required.");
            vehicle_no_dy.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(deliveryBoyInformation.getDeliveryBoyMobileNumber())){
            driver_no_dy.setError("Mobile Number is required.");
            driver_no_dy.requestFocus();
            return false;
        }
        return  true;

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        valueProgress=sharedPreferences.getInt("process",0);
        if(valueProgress>=0){
            progressbar_info.setProgress(valueProgress);
            txt_info_per.setText(valueProgress+"%");
        }
    }
    private void updateProgress(int value)
    {
        progressbar_info.setProgress(value);
        txt_info_per.setText(valueProgress+"%");
    }
    //    public void changeCity(String state){
//        switch (state){
//            case "Gujarat":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Gujarat, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//            case "Rajasthan":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Rajasthan, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//            case "Maharashtra":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Maharashtra, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//            case "Uttar Pradesh":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Uttar_Pradesh, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//            case "Madhya Pradesh":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Madhya_Pradesh, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//            case "Tamil Nadu":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Tamil_Nadu, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//            case "West Bengal":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.West_Bengal, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//            case "Assam":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Assam, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//            case "Punjab":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Punjab, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//            case "Goa":
//                cityAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Goa, android.R.layout.simple_spinner_dropdown_item);
//                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                citySpinner.setAdapter(cityAdapter);
//                break;
//
//
//
//        }
//    }
    public boolean calculateAge(View view) {
        int day = dataPicker.getDayOfMonth();
        int month = dataPicker.getMonth();
        int year = dataPicker.getYear();

        Calendar dob = Calendar.getInstance();
        dob.set(year, month, day);

        Calendar today = Calendar.getInstance();

        int years = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        int months = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        int days = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);

        if (days < 0) {
            months--;
            days += today.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        if (months < 0) {
            years--;
            months += 12;
        }

        String ageString = years + " Years, " + months + " Months, " + days + " Days";
        ageTextview.setText(ageString);
        if(years<=17){
            Toast.makeText(this, "You are UnderAge", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void shineStart() {
        Animation animation = new TranslateAnimation(
                0, btn_dy_info.getWidth() + btn_shine4.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        btn_shine4.startAnimation(animation);

    }
}
