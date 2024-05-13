package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import DataModel.Order;

public class report_weekly extends AppCompatActivity {
    BarChart barChartWeekly;
    FirebaseUser firebaseUser;
    DatabaseReference orderRef;
    TextView closeBtnReport25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_weekly);

        barChartWeekly = findViewById(R.id.barChartWeekly);
        closeBtnReport25=findViewById(R.id.closeBtnReport25);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        displayWeeklyChart(firebaseUser.getUid());

        closeBtnReport25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),report_selling.class));
                finish();

            }
        });

    }

    public void displayWeeklyChart(String sellerId) {
        HashMap<Integer, Float> dayRevenueMap = new HashMap<>();

        // Get the start and end dates of the current week
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String startDate = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String endDate = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());

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
                                    if (isDateInRange(orderDate, startDate, endDate)) {
                                        Calendar orderCalendar = Calendar.getInstance();
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                        try {
                                            orderCalendar.setTime(sdf.parse(orderDate));
                                            int orderDayOfWeek = orderCalendar.get(Calendar.DAY_OF_WEEK);
                                            int dayIndex = convertDayOfWeekToIndex(orderDayOfWeek);
                                            double sellingPrice = Double.parseDouble(order.getProductSellingPrice()) * Integer.parseInt(order.getProductQty());
                                            // If entry exists for the day, update the revenue
                                            if (dayRevenueMap.containsKey(dayIndex)) {
                                                float currentRevenue = dayRevenueMap.get(dayIndex);
                                                dayRevenueMap.put(dayIndex, currentRevenue + (float) sellingPrice);
                                            } else {
                                                // If entry doesn't exist for the day, add a new entry
                                                dayRevenueMap.put(dayIndex, (float) sellingPrice);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        ArrayList<BarEntry> data = new ArrayList<>();
                        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
                            int dayIndex = convertDayOfWeekToIndex(dayOfWeek);
                            float revenue = dayRevenueMap.containsKey(dayIndex) ? dayRevenueMap.get(dayIndex) : 0;
                            data.add(new BarEntry(dayOfWeek - Calendar.SUNDAY, revenue)); // Adjust day index
                        }

                        BarDataSet barDataSet = new BarDataSet(data, "Income");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        BarData barData = new BarData(barDataSet);
                        barChartWeekly.setData(barData);

                        YAxis yAxis = barChartWeekly.getAxisLeft();
                        yAxis.setAxisLineWidth(2f);
                        yAxis.setAxisLineColor(Color.BLACK);
                        yAxis.setLabelCount(1);

                        barChartWeekly.getDescription().setEnabled(false);
                        barChartWeekly.animateY(1500);

                        // Set X-axis labels to weekdays
                        barChartWeekly.getXAxis().setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                int dayOfWeek = (int) value + Calendar.SUNDAY;
                                // Convert day of week to weekday name or number as per your preference
                                switch (dayOfWeek) {
                                    case Calendar.SUNDAY:
                                        return "Sun";
                                    case Calendar.MONDAY:
                                        return "Mon";
                                    case Calendar.TUESDAY:
                                        return "Tue";
                                    case Calendar.WEDNESDAY:
                                        return "Wed";
                                    case Calendar.THURSDAY:
                                        return "Thu";
                                    case Calendar.FRIDAY:
                                        return "Fri";
                                    case Calendar.SATURDAY:
                                        return "Sat";
                                    default:
                                        return "";
                                }
                            }
                        });
                        barChartWeekly.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        barChartWeekly.getXAxis().setGranularity(1f);
                        barChartWeekly.getXAxis().setGranularityEnabled(true);
                        barChartWeekly.invalidate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private int convertDayOfWeekToIndex(int dayOfWeek) {
        // Adjust day of week to match the index in dayRevenueMap
        // For example, Sunday (Calendar.SUNDAY) corresponds to 1, Monday (Calendar.MONDAY) corresponds to 2, and so on
        return dayOfWeek == Calendar.SUNDAY ? 1 : dayOfWeek - Calendar.SUNDAY + 2;
    }


    private boolean isDateInRange(String date, String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date currentDate = sdf.parse(date);
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return currentDate.compareTo(start) >= 0 && currentDate.compareTo(end) <= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),report_selling.class));
        finish();
    }
}