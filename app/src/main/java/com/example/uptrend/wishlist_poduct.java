package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.uptrend.Adapter.WishListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.LikeProduct;

public class wishlist_poduct extends AppCompatActivity {
    RecyclerView recyclerViewLikeProduct;
    GridLayoutManager gridLayoutManager;
    ArrayList<LikeProduct> likeProductArrayList;
    DatabaseReference likeProductRef;
    Query userQuery;
    FirebaseUser firebaseUser;
    WishListAdapter wishListAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_poduct);

        recyclerViewLikeProduct=findViewById(R.id.recyclerLikeProduct);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        displayProduct();
    }

    public void displayProduct(){
        likeProductArrayList=new ArrayList<>();
        likeProductRef= FirebaseDatabase.getInstance().getReference("WishListProduct");
        userQuery=likeProductRef.orderByChild("userId").equalTo(firebaseUser.getUid());
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likeProductArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    LikeProduct likeProduct=dataSnapshot.getValue(LikeProduct.class);
                    likeProduct.setNodeId(dataSnapshot.getKey());
                    likeProductArrayList.add(likeProduct);
                }
                gridLayoutManager=new GridLayoutManager(wishlist_poduct.this,2, LinearLayoutManager.VERTICAL,false);
                recyclerViewLikeProduct.setLayoutManager(gridLayoutManager);
                wishListAdapter=new WishListAdapter(wishlist_poduct.this,likeProductArrayList);
                recyclerViewLikeProduct.setAdapter(wishListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}