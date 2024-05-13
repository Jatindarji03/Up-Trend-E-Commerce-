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

public class report_monthly_sell extends AppCompatActivity {
    BarChart barChartMonthlySell;
    FirebaseUser firebaseUser;
    DatabaseReference orderRef;
    TextView txtClose;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_monthly_sell);

        barChartMonthlySell=findViewById(R.id.barChartMonthlySell);
        txtClose=findViewById(R.id.closeBtnReport30);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),report_selling.class));
                finish();
            }
        });
        displayMonthlySellChart(firebaseUser.getUid());



    }

    public void displayMonthlySellChart(String sellerId){
        HashMap<Integer, Integer> daySellMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        orderRef = FirebaseDatabase.getInstance().getReference("Order");
        Query sellerQuery = orderRef.orderByChild("sellerId").equalTo(sellerId);
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
                                    if (orderMonth == currentMonth) {
                                        int stock = Integer.parseInt(order.getProductQty());
                                        if (daySellMap.containsKey(orderDay)) {
                                            int current = daySellMap.get(orderDay);
                                            daySellMap.put(orderDay, (int) current + stock);
                                        } else {
                                            daySellMap.put(orderDay, (int) stock);
                                        }
                                    }
                                }
                            }
                        }
                        ArrayList<BarEntry> data = new ArrayList<>();
                        for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
                            int revenue = daySellMap.containsKey(day) ? daySellMap.get(day) : 0;
                            data.add(new BarEntry(day, revenue));
                        }

                        BarDataSet barDataSet = new BarDataSet(data, "Income");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        BarData barData = new BarData(barDataSet);
                        barChartMonthlySell.setData(barData);


                        YAxis yAxis = barChartMonthlySell.getAxisLeft();
                        yAxis.setAxisLineWidth(2f);
                        yAxis.setAxisLineColor(Color.BLACK);
                        yAxis.setLabelCount(3
                        );


                        barChartMonthlySell.getDescription().setEnabled(false);
                        barChartMonthlySell.animateY(1500);

                        barChartMonthlySell.getXAxis().setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                // Convert float value (day) to date format
                                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                                calendar.set(Calendar.DAY_OF_MONTH, (int) value);
                                return sdf.format(calendar.getTime());
                            }
                        });
                        barChartMonthlySell.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        barChartMonthlySell.getXAxis().setGranularity(1f);

                        barChartMonthlySell.getXAxis().setGranularityEnabled(true);
                        barChartMonthlySell.invalidate();


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