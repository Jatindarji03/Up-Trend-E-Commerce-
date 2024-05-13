package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import DataModel.Order;

public class report_high_demand extends AppCompatActivity {


    private PieChart pieChart;
    private FirebaseUser firebaseUser;
    private DatabaseReference orderRef;
    TextView txtClose;

    int[] colorClassArray = new int[]{Color.LTGRAY,Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GREEN,Color.MAGENTA,Color.RED};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_high_demand);


        pieChart = findViewById(R.id.barChart24);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        txtClose=findViewById(R.id.closeBtnReport33);
        displayPieChart(firebaseUser.getUid());


        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),report_selling.class));
                finish();
            }
        });

    }

    public void displayPieChart(String sellerId) {
        HashMap<String, Integer> totalSell = new HashMap<>();
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
                                    int orderMonth = Integer.parseInt(parts[1]);
                                    if (orderMonth == currentMonth) {
                                        int stock = Integer.parseInt(order.getProductQty());
                                        if (totalSell.containsKey(order.getProductId())) {
                                            int current = totalSell.get(order.getProductId());
                                            totalSell.put(order.getProductId(), current + stock);
                                        } else {
                                            totalSell.put(order.getProductId(), stock);
                                        }
                                    }
                                }
                            }
                        }

                        ArrayList<PieEntry> data = new ArrayList<>();
                        for (Map.Entry<String, Integer> entry : totalSell.entrySet()) {
                            String productId = entry.getKey();
                            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(productId);
                            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String productName = snapshot.child("productName").getValue(String.class);
                                        int quantitySold = entry.getValue();
                                        data.add(new PieEntry(quantitySold, productName)); // Add PieEntry for each product
                                        // After adding the PieEntry, update the chart
                                        updateChart(data);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle onCancelled event
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event
                    }
                }
        );
    }

    private void updateChart(ArrayList<PieEntry> data) {
        PieDataSet dataSet = new PieDataSet(data, "Product Sales");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Set colors for the Pie Chart slices
        PieData pieData = new PieData(dataSet);

        // Customize Pie Chart as needed
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false); // Disable description
        pieChart.setEntryLabelTextSize(12f); // Set label text size
        pieChart.animateY(1500); // Animation duration

        pieChart.invalidate(); // Refresh chart
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),report_selling.class));
        finish();
    }

}