package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import DataModel.CancelProduct;

public class report_cancel_order extends AppCompatActivity {
    BarChart barChartCancel;
    private FirebaseUser firebaseUser;
    DatabaseReference cancelRef;
    TextView txtClose;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_cancel_order);
        barChartCancel = findViewById(R.id.barChartCancel);
        txtClose=findViewById(R.id.closeBtnReport22);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        displayCancelChart(firebaseUser.getUid());

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),report_selling.class));
                finish();
            }
        });

    }

    public void displayCancelChart(String sellerId) {
        HashMap<Integer, Integer> totalCancel = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        cancelRef = FirebaseDatabase.getInstance().getReference("Cancel");
        Query sellerQuery = cancelRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            CancelProduct cancelProduct = dataSnapshot.getValue(CancelProduct.class);
                            if (cancelProduct != null) {
                                String cancelDate = cancelProduct.getCancelDate();
                                String[] parts = cancelDate.split("-");
                                int cancelMonth = Integer.parseInt(parts[1]);
                                int cancelDay = Integer.parseInt(parts[0]);
                                if(cancelMonth==currentMonth){
                                    int qty=Integer.parseInt(cancelProduct.getProductQty());
                                    if(totalCancel.containsKey(cancelDay)){
                                        int current=totalCancel.get(cancelDay);
                                        totalCancel.put(cancelDay,current+qty);

                                    }else{
                                        totalCancel.put(cancelDay,qty);
                                    }
                                }
                            }
                        }
                        ArrayList<BarEntry> data = new ArrayList<>();
                        for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
                            int qty = totalCancel.containsKey(day) ? totalCancel.get(day) : 0;
                            data.add(new BarEntry(day, qty));
                        }

                        BarDataSet barDataSet = new BarDataSet(data, "Cancel Product");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        BarData barData = new BarData(barDataSet);
                        barChartCancel.setData(barData);


                        YAxis yAxis = barChartCancel.getAxisLeft();
                        yAxis.setAxisLineWidth(2f);
                        yAxis.setAxisLineColor(Color.BLACK);
                        yAxis.setLabelCount(10);

                        // Set formatter to display Y-axis values as integers
                        yAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                // Convert float value to integer
                                return String.valueOf((int) value);
                            }
                        });


                        barChartCancel.getDescription().setEnabled(false);
                        barChartCancel.animateY(1500);

                        barChartCancel.getXAxis().setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                // Convert float value (day) to date format
                                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                                calendar.set(Calendar.DAY_OF_MONTH, (int) value);
                                return sdf.format(calendar.getTime());
                            }
                        });
                        barChartCancel.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        barChartCancel.getXAxis().setGranularity(1f);

                        barChartCancel.getXAxis().setGranularityEnabled(true);
                        barChartCancel.invalidate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),report_selling.class));
        finish();
    }
}