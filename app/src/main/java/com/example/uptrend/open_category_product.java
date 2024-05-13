package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.uptrend.Adapter.AllProductAdapter;
import com.example.uptrend.Adapter.CategoryAdapter;
import com.example.uptrend.Adapter.Onclick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.Product;

public class open_category_product extends AppCompatActivity implements Onclick {
    private String sortBy, value;
    private RecyclerView recyclerViewCategory;
    private ArrayList<Product> productArrayList;
    private DatabaseReference productRef;
    private Query categoryQuery, subCategoryQuery;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_category_product);

        //FindViewById
        recyclerViewCategory = findViewById(R.id.productCategory);


        sortBy = getIntent().getStringExtra("sortBy");
        value = getIntent().getStringExtra("value");

        if (sortBy.equals("Category")) {
            displayAccordingCategory(value);
        } else if (sortBy.equals("SubCategory")) {
            displayAccordingSubCategory(value);
        }
    }

    public void displayAccordingCategory(String value) {
        productArrayList = new ArrayList<>();
        productArrayList.clear();
        productRef = FirebaseDatabase.getInstance().getReference("Product");
        categoryQuery = productRef.orderByChild("productCategory").startAt(value).endAt(value + "\uf8ff");
        categoryQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        productArrayList.add(product);
                    }
                }
                GridLayoutManager gridLayoutManager = new GridLayoutManager(open_category_product.this, 2, GridLayoutManager.VERTICAL, false);
                categoryAdapter = new CategoryAdapter(open_category_product.this, productArrayList, open_category_product.this);
                recyclerViewCategory.setLayoutManager(gridLayoutManager);
                recyclerViewCategory.setAdapter(categoryAdapter);



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayAccordingSubCategory(String value) {
            productArrayList = new ArrayList<>();
            productArrayList.clear();
            productRef = FirebaseDatabase.getInstance().getReference("Product");
            categoryQuery = productRef.orderByChild("productSubCategory").startAt(value).endAt(value + "\uf8ff");
            categoryQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        if (product != null) {
                            productArrayList.add(product);
                        }
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(open_category_product.this, 2, GridLayoutManager.VERTICAL, false);
                    categoryAdapter = new CategoryAdapter(open_category_product.this, productArrayList, open_category_product.this);
                    recyclerViewCategory.setLayoutManager(gridLayoutManager);
                    recyclerViewCategory.setAdapter(categoryAdapter);



                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    @Override
    public void ItemOnClickListener(String productId) {
        Intent i=new Intent(open_category_product.this,open_product.class);
        i.putExtra("productId",productId);
        startActivity(i);
    }
}