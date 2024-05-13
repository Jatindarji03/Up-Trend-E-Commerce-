package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.uptrend.Adapter.Onclick;
import com.example.uptrend.Adapter.OrderHistoryAdapter;
import com.example.uptrend.Adapter.OrderOnClick;
import com.example.uptrend.Adapter.ReturnOnClick;
import com.example.uptrend.Adapter.ReturnProductAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.Order;
import DataModel.Return;

public class complete_order extends AppCompatActivity implements OrderOnClick, ReturnOnClick {
    RecyclerView recyclerViewOrderHistory,recyclerViewReturn;
    ArrayList<Order> orderArrayList;
    ArrayList<Return> returnArrayList;
    OrderHistoryAdapter orderHistoryAdapter;
    ReturnProductAdapter returnProductAdapter;
    DatabaseReference orderRef,returnRef;
    FirebaseUser firebaseUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        recyclerViewOrderHistory=findViewById(R.id.recyclerViewOrderHistory);
        recyclerViewReturn=findViewById(R.id.recyclerViewReturnProduct);


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        displayOrderDetails(firebaseUser.getUid());
        displayReturnProduct(firebaseUser.getUid());
    }

    public void displayOrderDetails(String userId){
        orderArrayList=new ArrayList<>();
        orderArrayList.clear();
        orderRef= FirebaseDatabase.getInstance().getReference("Order");
        Query orderQuery=orderRef.orderByChild("userId").equalTo(userId);
        orderQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderArrayList.clear();
                for(DataSnapshot orderSnapShot:snapshot.getChildren()){
                    Order order=orderSnapShot.getValue(Order.class);
                    order.setNodeId(orderSnapShot.getKey());
                    orderArrayList.add(order);
                }
                orderHistoryAdapter=new OrderHistoryAdapter(complete_order.this,orderArrayList,complete_order.this);
                LinearLayoutManager layoutManager=new LinearLayoutManager(complete_order.this,LinearLayoutManager.VERTICAL,false);
                recyclerViewOrderHistory.setLayoutManager(layoutManager);
                recyclerViewOrderHistory.setAdapter(orderHistoryAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void displayReturnProduct(String userId){
        returnArrayList=new ArrayList<>();
        returnArrayList.clear();
        returnRef=FirebaseDatabase.getInstance().getReference("Return");
        Query returnQuery=returnRef.orderByChild("userId").equalTo(userId);
        returnQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot productSnapShot:snapshot.getChildren()){
                    Return returnProduct=productSnapShot.getValue(Return.class);
                    returnProduct.setNodeId(productSnapShot.getKey());
                    returnArrayList.add(returnProduct);
                }
                returnProductAdapter=new ReturnProductAdapter(complete_order.this,returnArrayList,complete_order.this);
                LinearLayoutManager layoutManager=new LinearLayoutManager(complete_order.this,LinearLayoutManager.VERTICAL,false);
                recyclerViewReturn.setLayoutManager(layoutManager);
                recyclerViewReturn.setAdapter(returnProductAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    @Override
    public void onClickItem(String orderId) {
        Intent i=new Intent(complete_order.this,open_history_pd.class);
        i.putExtra("orderId",orderId);
        i.putExtra("status","order");
        startActivity(i);
        finish();
    }

    @Override
    public void ReturnOnClickItem(String returnOrderId) {
        Intent i=new Intent(complete_order.this,open_history_pd.class);
        i.putExtra("orderId",returnOrderId);
        i.putExtra("status","return");
        startActivity(i);
        finish();
    }
}