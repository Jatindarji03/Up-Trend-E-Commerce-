package com.example.uptrendseller;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapteranddatamodel.DateAndTime;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import DataModel.Order;
import DataModel.Product;

public class home extends Fragment {
    private TextView txtTotalSellStock, txtTotalSellingIncome, txtTotalProductSelling, txtTotalProductSell,txtTotalCancelProduct,txtTotalReturnProduct;

    private DatabaseReference orderRef;
    private FirebaseUser firebaseUser;
    private long totalQty = 0;
    private long qty = 0;
    private long totalProduct=0;
    private long sellingPrice = 0;
    private long totalSellingPrice = 0;
    private LineChart lineChart;


    public home() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //FindViewBy Id of Widget
        txtTotalSellStock = view.findViewById(R.id.txtTotalSellStock);
        txtTotalSellingIncome = view.findViewById(R.id.txtTotalSellingIncome);
        txtTotalProductSelling = view.findViewById(R.id.txtTotalProductSelling);
        txtTotalProductSell = view.findViewById(R.id.txtTotalProductSell);
        lineChart = view.findViewById(R.id.getTheGraph);
        txtTotalCancelProduct=view.findViewById(R.id.txtTotalCancelProduct);
        txtTotalReturnProduct=view.findViewById(R.id.txtReturnProduct);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        displayTotalProductSelling(firebaseUser.getUid());
        displayTotalSellingIncome(firebaseUser.getUid());
        displayTotalProductSellToday(firebaseUser.getUid());
        displayTotalProductSell(firebaseUser.getUid());
        displayTotalCancelProduct(firebaseUser.getUid());
        displayTotalReturnProduct(firebaseUser.getUid());

        //lineChartData();
        showMonthChart(firebaseUser.getUid());
        return view;
    }

    public void showMonthChart(String sellerId) {
        HashMap<Integer, Float> dayRevenueMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        orderRef = FirebaseDatabase.getInstance().getReference("Order");
        Query sellerQuery = orderRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the map before populating it with new data
                dayRevenueMap.clear();
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
                // Convert map entries to Entry objects
                ArrayList<Entry> entries = new ArrayList<>();
                for (int day = 1; day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
                    float revenue = dayRevenueMap.containsKey(day) ? dayRevenueMap.get(day) : 0;
                    entries.add(new Entry(day, revenue));
                }
                // Create LineDataSet with the aggregated data
                LineDataSet lineDataSet = new LineDataSet(entries, "Income");
                lineDataSet.setColor(getResources().getColor(R.color.black));
                lineDataSet.setValueTextColor(getResources().getColor(R.color.blue));
                lineDataSet.setValueTextSize(12f);
                lineDataSet.setDrawFilled(true);
                LineData lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);

                // Animation and other work in chart

                YAxis yAxis = lineChart.getAxisLeft();
                yAxis.setAxisLineWidth(2f);
                yAxis.setAxisLineColor(Color.BLACK);
                yAxis.setLabelCount(10);

                lineChart.getDescription().setEnabled(false);
                lineChart.animateY(1500);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.getXAxis().setGranularity(1f);
                lineChart.getXAxis().setGranularityEnabled(true);


                // Set custom value formatter for x-axis
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        // Convert float value (day) to date format
                        SimpleDateFormat sdf = new SimpleDateFormat("dd");
                        calendar.set(Calendar.DAY_OF_MONTH, (int) value);
                        return sdf.format(calendar.getTime());
                    }
                });

                lineChart.invalidate(); // Refresh the chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }


    public void displayTotalProductSelling(String sellerId) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        orderRef = FirebaseDatabase.getInstance().getReference("Order");
        Query sellerQuery = orderRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        totalQty = 0;
                        qty = 0;

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Order order = dataSnapshot.getValue(Order.class);
                            if (order != null) {
                                if (order.getOrderStatus().equals("delivered")) {
                                    int orderYear = getYearFromDateString(order.getOrderDate());
                                    if (currentYear == orderYear) {
                                        qty = Long.valueOf(order.getProductQty());
                                        totalQty = totalQty + qty;
                                    }
                                }
                            }
                        }
                        txtTotalSellStock.setText(String.valueOf(totalQty));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void displayTotalSellingIncome(String sellerId) {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        orderRef = FirebaseDatabase.getInstance().getReference("Order");
        Query sellerQuery = orderRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        qty = 0;
                        totalSellingPrice = 0;
                        sellingPrice = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Order order = dataSnapshot.getValue(Order.class);
                            if (order != null) {
                                if (order.getOrderStatus().equals("delivered")) {
                                    int orderYear = getYearFromDateString(order.getOrderDate());
                                    if (currentYear == orderYear) {
                                        qty = Long.parseLong(order.getProductQty());
                                        sellingPrice = qty * Long.parseLong(order.getProductSellingPrice());
                                        totalSellingPrice = totalSellingPrice + sellingPrice;
                                    }
                                }
                            }
                            txtTotalSellingIncome.setText(String.valueOf(totalSellingPrice));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    public void displayTotalProductSellToday(String sellerId) {

        orderRef = FirebaseDatabase.getInstance().getReference("Order");
        Query sellerQuery = orderRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        qty = 0;
                        totalQty = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Order order = dataSnapshot.getValue(Order.class);
                            if (order != null) {
                                if (order.getOrderStatus().equals("delivered")) {
                                    if (order.getOrderDate().equals(DateAndTime.getDate())) {
                                        qty = Long.parseLong(order.getProductQty());
                                        totalQty = totalQty + qty;
                                    }
                                }
                            }
                        }
                        txtTotalProductSelling.setText(String.valueOf(totalQty));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void displayTotalProductSell(String sellerId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");
        Query sellerQuery = productRef.orderByChild("adminId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        qty = 0;
                        totalQty = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Product product = dataSnapshot.getValue(Product.class);
                            if (product != null) {
                                qty = Long.parseLong(product.getTotalStock());
                                totalQty = totalQty + qty;
                            }
                        }
                        txtTotalProductSell.setText(String.valueOf(totalQty));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void displayTotalCancelProduct(String sellerId){
        DatabaseReference cancelRef=FirebaseDatabase.getInstance().getReference("Cancel");
        Query sellerQuery=cancelRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        totalProduct=snapshot.getChildrenCount();
                        txtTotalCancelProduct.setText(String.valueOf(totalProduct));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void displayTotalReturnProduct(String sellerId){
        DatabaseReference returnRef=FirebaseDatabase.getInstance().getReference("Return");
        Query sellerQuery=returnRef.orderByChild("sellerId").equalTo(sellerId);
        sellerQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        totalProduct=snapshot.getChildrenCount();
                        txtTotalReturnProduct.setText(String.valueOf(totalProduct));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private int getYearFromDateString(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Error occurred
        }
    }

}