package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.uptrend.Adapter.RatingProductAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import DataModel.Order;

public class rating_products extends AppCompatActivity {
    RecyclerView productReview;
    DatabaseReference orderRef;
    FirebaseUser firebaseUser;
    ArrayList<Order> orderArrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_products);
        productReview=findViewById(R.id.productReview);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        displayProductReview(firebaseUser.getUid());
    }

    public void displayProductReview(String userId){
        orderArrayList = new ArrayList<>();
        orderRef = FirebaseDatabase.getInstance().getReference("Order");
        Query userQuery = orderRef.orderByChild("userId").equalTo(userId);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashSet<String> reviewedProducts = new HashSet<>(); // HashSet to keep track of reviewed product IDs
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    if(order.getOrderStatus().equals("delivered")) {
                        // Check if the product ID has already been reviewed
                        if (!reviewedProducts.contains(order.getProductId())) {
                            orderArrayList.add(order);
                            reviewedProducts.add(order.getProductId());
                        }
                    }
                }
                // Reverse the orderArrayList
                Collections.reverse(orderArrayList);
                RatingProductAdapter ratingProductAdapter = new RatingProductAdapter(rating_products.this, orderArrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rating_products.this, LinearLayoutManager.VERTICAL, false);
                productReview.setLayoutManager(linearLayoutManager);
                productReview.setAdapter(ratingProductAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

//    public void displayProductReview(String userId){
//        orderArrayList=new ArrayList<>();
//        orderRef= FirebaseDatabase.getInstance().getReference("Order");
//        Query userQuery=orderRef.orderByChild("userId").equalTo(userId);
//        userQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    Order order=dataSnapshot.getValue(Order.class);
//                    if(order.getOrderStatus().equals("delivered")){
//                        orderArrayList.add(order);
//                    }
//                }
//                RatingProductAdapter ratingProductAdapter=new RatingProductAdapter(rating_products.this,orderArrayList);
//                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(rating_products.this,LinearLayoutManager.VERTICAL,false);
//                productReview.setLayoutManager(linearLayoutManager);
//                productReview.setAdapter(ratingProductAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}