package com.example.uptrendseller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class report_selling extends AppCompatActivity {


    TextView daily_txt,monthly_txt,weekly_txt,monthly_selling_txt,high_demand_txt,return_order_txt,cancel_order_txt,closeBtnReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_selling);


        daily_txt = findViewById(R.id.daily_Income_txt);
        monthly_txt = findViewById(R.id.monthly_Income_txt);
        weekly_txt = findViewById(R.id.weekly_Income_txt);
        monthly_selling_txt = findViewById(R.id.monthly_product_txt);
        high_demand_txt = findViewById(R.id.high_demand_txt);
        return_order_txt=findViewById(R.id.return_order_txt);
        closeBtnReport=findViewById(R.id.closeBtnReport);
        cancel_order_txt=findViewById(R.id.cancel_order_txt);



        closeBtnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),dashboard_admin.class));
                finish();
            }
        });

        daily_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),report_daily.class));
                finish();
            }
        });
        weekly_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),report_weekly.class));
                finish();
            }
        });
        monthly_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),report_monthly.class));
                finish();
            }
        });
        monthly_selling_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),report_monthly_sell.class));
                finish();
            }
        });
        high_demand_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),report_high_demand.class));
                finish();
            }
        });
        return_order_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),report_return_order.class));
                finish();
            }
        });

        cancel_order_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), report_cancel_order.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),dashboard_admin.class));
        finish();
    }
}