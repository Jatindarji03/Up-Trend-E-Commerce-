package com.example.uptrend;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uptrend.Adapter.Onclick;
import com.example.uptrend.Adapter.RecentlyViewProductAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.Comparator;

import DataModel.RecentlyViewProduct;
import io.github.muddz.styleabletoast.StyleableToast;

public class account_user extends AppCompatActivity implements Onclick {


    AppCompatButton wishlist_btn, profile_btn,order_btn,rating_btn,notification_btn,btnLogout;
    BottomNavigationView bottomNavigationView;
    private FirebaseUser user;
    private LinearLayout layoutRecentlyViewProduct;
    private RecyclerView recyclerViewRecentlyViewProduct;
    private DatabaseReference recentlyViewProductRef;
    private ArrayList<RecentlyViewProduct> recentlyViewProductArrayList;
    private TextView txtUserName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_user);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        layoutRecentlyViewProduct = findViewById(R.id.layoutRecentlyProductuser);
        recyclerViewRecentlyViewProduct = findViewById(R.id.recyclerViewRecentlyProductuser);
        txtUserName = findViewById(R.id.txtUserName);

        wishlist_btn = findViewById(R.id.wishlist_btn);
        profile_btn = findViewById(R.id.profile_btn);
        order_btn = findViewById(R.id.orders_btn);
        rating_btn = findViewById(R.id.rating_btn);
        btnLogout=findViewById(R.id.btnLogout);
        notification_btn = findViewById(R.id.notification_btn);


        bottomNavigationView.setSelectedItemId(R.id.bottom_account);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), home.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_categories:
                    startActivity(new Intent(getApplicationContext(), category_product.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_account:
                    return true;
                case R.id.bottom_add_to_cart:
                    startActivity(new Intent(getApplicationContext(), add_to_cart_product.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), complete_order.class));
            }
        });
        wishlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), wishlist_poduct.class));
            }
        });

        notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), notification.class));
            }
        });
        rating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), rating_products.class));
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), user_profile.class));
            }
        });



        displayRecentlyViewProduct();
        displayUserName();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                StyleableToast.makeText(getApplicationContext(),"Logout Successfully",R.style.UptrendToast).show();
                Intent intent=new Intent(getApplicationContext(),signUp_and_logIn_page.class);
                intent.putExtra("status","SignIn");
                startActivity(intent);
            }
        });

    }

    public void displayLogoutDialog(){
        AlertDialog.Builder logoutDialog=new AlertDialog.Builder(getApplicationContext());
        logoutDialog.setIcon(R.drawable.fire);
        logoutDialog.setTitle("Logout");
        logoutDialog.setMessage("Do you sure you want to log out your account?");
        logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        logoutDialog.setNegativeButton("No",null);
        logoutDialog.show();
    }


    public void displayUserName() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
        Query userQuery = userRef.orderByChild("userId").equalTo(user.getUid());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                String userName = userSnapshot.child("userName").getValue(String.class);
                txtUserName.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayRecentlyViewProduct() {
        recentlyViewProductArrayList = new ArrayList<>();
        recentlyViewProductRef = FirebaseDatabase.getInstance().getReference("RecentlyViewProduct");
        Query query = recentlyViewProductRef.orderByChild("userId").equalTo(user.getUid()).limitToLast(4);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recentlyViewProductArrayList.clear();
                for (DataSnapshot productSnapShot : snapshot.getChildren()) {
                    RecentlyViewProduct recentlyViewProduct = productSnapShot.getValue(RecentlyViewProduct.class);
                    long difference = System.currentTimeMillis() - Long.parseLong(recentlyViewProduct.getTimeStamp());
                    if (difference > 0) {
                        recentlyViewProductArrayList.add(recentlyViewProduct);

                    }
                }
                Collections.sort(recentlyViewProductArrayList, new Comparator<RecentlyViewProduct>() {
                    @Override
                    public int compare(RecentlyViewProduct product1, RecentlyViewProduct product2) {
                        // Compare timestamps in descending order
                        return Long.compare(Long.parseLong(product2.getTimeStamp()), Long.parseLong(product1.getTimeStamp()));
                    }
                });
                if (recentlyViewProductArrayList.size() != 0) {
                    layoutRecentlyViewProduct.setVisibility(View.VISIBLE);
                    RecentlyViewProductAdapter recentlyViewProductAdapter = new RecentlyViewProductAdapter(account_user.this, recentlyViewProductArrayList, account_user.this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(account_user.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewRecentlyViewProduct.setLayoutManager(linearLayoutManager);
                    recyclerViewRecentlyViewProduct.setAdapter(recentlyViewProductAdapter);
                } else {
                    layoutRecentlyViewProduct.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void ItemOnClickListener(String productId) {
        Intent i = new Intent(account_user.this, open_product.class);
        i.putExtra("productId", productId);
        startActivity(i);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), home.class));
        finish();
    }
}