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
import com.example.uptrendseller.Adapter.RequestOnClick;
import com.example.uptrendseller.Adapter.ReturnProductAdapter;
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
import DataModel.Return;

public class return_order extends AppCompatActivity implements RequestOnClick {

    private TextView cancelTxt55,returnTxt55;
    private RecyclerView recyclerViewReturnProduct;
    private DatabaseReference returnRef;
    private FirebaseUser firebaseUser;
    private ArrayList<Return> returnArrayList;
    private  ReturnProductAdapter returnProductAdapter;
    TextView btnCloseReturn55;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order);


        cancelTxt55 = findViewById(R.id.cancel_text22);
        returnTxt55 = findViewById(R.id.return_text22);
        btnCloseReturn55=findViewById(R.id.btnCloseReturn55);

        btnCloseReturn55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),dashboard_admin.class));
                finish();
            }
        });
        recyclerViewReturnProduct=findViewById(R.id.recyclerViewReturn);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        cancelTxt55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), cancel_order.class));
                finish();
            }
        });

        returnTxt55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), return_order.class));
                finish();
            }
        });

        displayReturnProduct(firebaseUser.getUid());

    }

    public void displayReturnProduct(String sellerId){
        returnArrayList=new ArrayList<>();
        returnRef = FirebaseDatabase.getInstance().getReference("Return");
        Query adminQuery = returnRef.orderByChild("sellerId").equalTo(sellerId);
        adminQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                returnArrayList.clear();
                for(DataSnapshot productSnapShot:snapshot.getChildren()){
                    Return returnProduct=productSnapShot.getValue(Return.class);
                    returnProduct.setNodeId(productSnapShot.getKey());
                    returnArrayList.add(returnProduct);
                }
                if (returnArrayList != null) {
                    returnProductAdapter = new ReturnProductAdapter(return_order.this, returnArrayList,return_order.this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(return_order.this, LinearLayoutManager.VERTICAL, false);
                    recyclerViewReturnProduct.setLayoutManager(linearLayoutManager);
                    recyclerViewReturnProduct.setAdapter(returnProductAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void RequestOnClickListener(String nodeId, String productId, String userId, String qty, String size) {
        Intent i=new Intent(return_order.this,open_return_order.class);
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