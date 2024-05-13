package com.example.uptrendseller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import DataModel.Order;

public class report_daily extends AppCompatActivity {


    BarChart barChart;
    private FirebaseUser firebaseUser;
    DatabaseReference orderRef;
    TextView closeBtnReport22;
    private List<String> values = Arrays.asList("1 Day", "2 Day", "3 Day", "4 Day", "5 Day", "6 Day", "7 Day");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_daily);


        barChart = findViewById(R.id.barChart);
        closeBtnReport22 = findViewById(R.id.closeBtnReport22);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        displayChart(firebaseUser.getUid());

        closeBtnReport22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), report_selling.class));
                finish();
            }
        });

    }

    public void displayChart(String sellerId) {
        HashMap<Integer, Float> dayRevenueMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        orderRef = FirebaseDatabase.getInstance().getReference("Order");
        Query sellerQuery = orderRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Order order = dataSnapshot.getValue(Order.class);
                            if (order != null) {
                                if (order.getOrderStatus().equals("delivered")) {
                                    String orderDate = order.getOrderDate();
                                    String[] parts = orderDate.split("-");
                                    int orderDay = Integer.parseInt(parts[0]);
                                    int orderMonth = Integer.parseInt(parts[1]);
                                    if (orderMonth == currentMonth) {
                                        double sellingPrice = Double.parseDouble(order.getProductSellingPrice()) * Integer.parseInt(order.getProductQty());
                                        // If entry exists for the day, update the revenue
                                        if (dayRevenueMap.containsKey(orderDay)) {
                                            float currentRevenue = dayRevenueMap.get(orderDay);
                                            dayRevenueMap.put(orderDay, currentRevenue + (float) sellingPrice);
                                        } else {
                                            // If entry doesn't exist for the day, add a new entry
                                            dayRevenueMap.put(orderDay, (float) sellingPrice);
                                        }
                                    }
                                }
                            }

                        }
                        ArrayList<BarEntry> data = new ArrayList<>();
                        for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
                            float revenue = dayRevenueMap.containsKey(day) ? dayRevenueMap.get(day) : 0;
                            data.add(new BarEntry(day, revenue));
                        }

                        BarDataSet barDataSet = new BarDataSet(data, "Income");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        BarData barData = new BarData(barDataSet);
                        barChart.setData(barData);


                        YAxis yAxis = barChart.getAxisLeft();
                        yAxis.setAxisLineWidth(2f);
                        yAxis.setAxisLineColor(Color.BLACK);
                        yAxis.setLabelCount(10);


                        barChart.getDescription().setEnabled(false);
                        barChart.animateY(1500);

                        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                // Convert float value (day) to date format
                                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                                calendar.set(Calendar.DAY_OF_MONTH, (int) value);
                                return sdf.format(calendar.getTime());
                            }
                        });
                        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        barChart.getXAxis().setGranularity(1f);

                        barChart.getXAxis().setGranularityEnabled(true);
                        barChart.invalidate();
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
        startActivity(new Intent(getApplicationContext(), report_selling.class));
        finish();
    }
}