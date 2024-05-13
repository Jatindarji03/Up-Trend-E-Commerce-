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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import DataModel.Order;

public class report_monthly extends AppCompatActivity {
    private BarChart barChartMonthly;
    private FirebaseUser firebaseUser;
    private DatabaseReference orderRef;
    TextView closeBtnReport27;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_monthly);

        barChartMonthly=findViewById(R.id.barChartMonthly);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        closeBtnReport27=findViewById(R.id.closeBtnReport27);
        displayPreviousMonthChart(firebaseUser.getUid());

        closeBtnReport27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),report_selling.class));
                finish();
            }
        });


    }
    public void displayPreviousMonthChart(String sellerId){
        HashMap<Integer, Float> dayRevenueMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int previousMonth = calendar.get(Calendar.MONTH) + 1;;
        orderRef= FirebaseDatabase.getInstance().getReference("Order");
        Query sellerQuery=orderRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Order order=dataSnapshot.getValue(Order.class);
                            if(order!=null){
                                if (order.getOrderStatus().equals("delivered")) {
                                    String orderDate = order.getOrderDate();
                                    String[] parts = orderDate.split("-");
                                    int orderDay = Integer.parseInt(parts[0]);
                                    int orderMonth = Integer.parseInt(parts[1]);
                                    if (orderMonth == previousMonth) {
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
                        barChartMonthly.setData(barData);


                        YAxis yAxis = barChartMonthly.getAxisLeft();
                        yAxis.setAxisLineWidth(2f);
                        yAxis.setAxisLineColor(Color.BLACK);
                        yAxis.setLabelCount(10);


                        barChartMonthly.getDescription().setEnabled(false);
                        barChartMonthly.animateY(1500);

                        barChartMonthly.getXAxis().setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                // Convert float value (day) to date format
                                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                                calendar.set(Calendar.DAY_OF_MONTH, (int) value);
                                return sdf.format(calendar.getTime());
                            }
                        });
                        barChartMonthly.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        barChartMonthly.getXAxis().setGranularity(1f);

                        barChartMonthly.getXAxis().setGranularityEnabled(true);
                        barChartMonthly.invalidate();
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

    public int getPreviousMonth(){
        Calendar calendar=Calendar.getInstance();
        int currentMonth=calendar.get(Calendar.MONTH)+1;
        int previousMonth;
        if(currentMonth==1){
            previousMonth=12;
        }else{
            previousMonth=currentMonth-1;
        }
        return previousMonth;
    }
}