package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uptrendseller.Adapter.InventoryProductAdapter;
import com.example.uptrendseller.Adapter.Onclick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.Product;

public class inventory_product extends AppCompatActivity implements Onclick {
    private DatabaseReference productNode;
    private FirebaseUser user;
    private Query productQuery;
    private ArrayList<Product> productArrayList;
    private RecyclerView recyclerView;
    private InventoryProductAdapter productAdapter;
    TextView btnClose;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_product);

        //findViewById of Widget
        recyclerView=findViewById(R.id.recyclerView);
        btnClose=findViewById(R.id.btnClose);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),dashboard_admin.class));
                finish();
            }
        });

        productArrayList=new ArrayList<>();



        //Making Instance of Object
        user= FirebaseAuth.getInstance().getCurrentUser();
        productNode= FirebaseDatabase.getInstance().getReference("Product");
        productQuery=productNode.orderByChild("adminId").equalTo(user.getUid());
        productQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productArrayList.clear();
                for(DataSnapshot productSnapShot:snapshot.getChildren()){
                    Product product=productSnapShot.getValue(Product.class);
                    productArrayList.add(product);
                }
                productAdapter=new InventoryProductAdapter(inventory_product.this,productArrayList,inventory_product.this);
                recyclerView.setAdapter(productAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),dashboard_admin.class));
        finish();
    }

    @Override
    public void ItemOnClickListener(String productId) {
        Intent i=new Intent(inventory_product.this,edit_product.class);
        i.putExtra("productId",productId);
        startActivity(i);
        finish();
    }
}