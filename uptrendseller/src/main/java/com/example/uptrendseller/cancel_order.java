package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.uptrendseller.Adapter.CancelProductAdapter;
import com.example.uptrendseller.Adapter.OrderRequestAdapter;
import com.example.uptrendseller.Adapter.RequestOnClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.CancelProduct;
import DataModel.Order;

public class cancel_order extends AppCompatActivity implements RequestOnClick {
    private RecyclerView recyclerViewCancel;
    private ArrayList<CancelProduct> cancelProductArrayList;
    private DatabaseReference cancelProductRef;
    private FirebaseUser firebaseUser;


    private TextView cancelTxt,returnTxt,btnCloseReturn;
    CancelProductAdapter cancelProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);

        recyclerViewCancel=findViewById(R.id.recyclerViewCancel);


        cancelTxt = findViewById(R.id.cancel_text);
        returnTxt = findViewById(R.id.return_text);
        btnCloseReturn=findViewById(R.id.btnCloseReturn);

        btnCloseReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),dashboard_admin.class));
                finish();
            }
        });
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        returnTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), return_order.class));
                finish();
            }
        });

        displayCancelProduct(firebaseUser.getUid());


    }

    public void displayCancelProduct(String adminId) {
        cancelProductArrayList=new ArrayList<>();
        cancelProductRef = FirebaseDatabase.getInstance().getReference("Cancel");
        Query adminQuery = cancelProductRef.orderByChild("sellerId").equalTo(adminId);
        adminQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cancelProductArrayList.clear();
                for(DataSnapshot productSnapShot:snapshot.getChildren()){
                    CancelProduct cancelProduct=productSnapShot.getValue(CancelProduct.class);
                    cancelProduct.setNodeId(productSnapShot.getKey());
                    cancelProductArrayList.add(cancelProduct);
                }
                if (cancelProductArrayList != null) {
                    cancelProductAdapter = new CancelProductAdapter(cancel_order.this, cancelProductArrayList,cancel_order
                            .this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cancel_order.this, LinearLayoutManager.VERTICAL, false);
                    recyclerViewCancel.setLayoutManager(linearLayoutManager);
                    recyclerViewCancel.setAdapter(cancelProductAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void RequestOnClickListener(String nodeId, String productId, String userId, String qty, String size) {
        Intent i=new Intent(cancel_order.this,open_cancel_order.class);
        i.putExtra("nodeId",nodeId);
        i.putExtra("productId",productId);
        i.putExtra("userId",userId);
        i.putExtra("qty",qty);
        i.putExtra("size",size);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),dashboard_admin.class));
        finish();
    }
}