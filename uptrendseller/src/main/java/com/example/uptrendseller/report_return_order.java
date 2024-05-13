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

import DataModel.CancelProduct;
import DataModel.Return;

public class report_return_order extends AppCompatActivity {
    BarChart barChartReturn;
    private DatabaseReference returnRef;
    private FirebaseUser firebaseUser;
    TextView txtClose;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_return_order);

        barChartReturn = findViewById(R.id.barChartReturn);
        txtClose=findViewById(R.id.closeBtnReport22);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        displayReturnProductChart(firebaseUser.getUid());

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),report_selling.class));
                finish();
            }
        });


    }

    public void displayReturnProductChart(String sellerId) {

        HashMap<Integer, Integer> totalReturn = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        returnRef = FirebaseDatabase.getInstance().getReference("Return");
        Query sellerQuery = returnRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Return returnProduct = dataSnapshot.getValue(Return.class);
                            if (returnProduct != null) {
                                String returnDate = returnProduct.getReturnDate();
                                String[] parts = returnDate.split("-");
                                int returnMonth = Integer.parseInt(parts[1]);
                                int returnDay = Integer.parseInt(parts[0]);
                                if (returnMonth == currentMonth) {
                                    int qty = Integer.parseInt(returnProduct.getProductQty());
                                    if (totalReturn.containsKey(returnDay)) {
                                        int current = totalReturn.get(returnDay);
                                        totalReturn.put(returnDay, current + qty);

                                    } else {
                                        totalReturn.put(returnDay, qty);
                                    }
                                }
                            }
                        }
                        ArrayList<BarEntry> data = new ArrayList<>();
                        for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
                            int qty = totalReturn.containsKey(day) ? totalReturn.get(day) : 0;
                            data.add(new BarEntry(day, qty));
                        }

                        BarDataSet barDataSet = new BarDataSet(data, "Return Product");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        BarData barData = new BarData(barDataSet);
                        barChartReturn.setData(barData);


                        YAxis yAxis = barChartReturn.getAxisLeft();
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


                        barChartReturn.getDescription().setEnabled(false);
                        barChartReturn.animateY(1500);

                        barChartReturn.getXAxis().setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                // Convert float value (day) to date format
                                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                                calendar.set(Calendar.DAY_OF_MONTH, (int) value);
                                return sdf.format(calendar.getTime());
                            }
                        });
                        barChartReturn.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        barChartReturn.getXAxis().setGranularity(1f);

                        barChartReturn.getXAxis().setGranularityEnabled(true);
                        barChartReturn.invalidate();
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
