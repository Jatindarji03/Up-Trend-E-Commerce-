package com.example.uptrendseller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;


import android.content.Context;
import android.content.SharedPreferences;

import DataModel.Product;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;


public class dashboard_admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseUser user;
    TextView edit_txt;

    Toolbar toolbar;
    private CircleImageView imageView;
    private DatabaseReference adminNode;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);


        toolbar = findViewById(R.id.toolbar);
        user= FirebaseAuth.getInstance().getCurrentUser();


        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this,R.style.nav_text1);



        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new home()).commit();

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(dashboard_admin.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(dashboard_admin.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }


        View view=navigationView.getHeaderView(0);
        edit_txt=view.findViewById(R.id.edit_profile_txt);
        // display image in navigation drawer.
        imageView=view.findViewById(R.id.navProfileImage);
        adminNode= FirebaseDatabase.getInstance().getReference("Admin");
        Query query=adminNode.orderByChild("adminId").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot sellerSnapshot = snapshot.getChildren().iterator().next();
                String imageUri=sellerSnapshot.child("profileImage").getValue(String.class);
                Glide.with(view).load(imageUri).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
           edit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),profile_seller.class));
                finish();
            }
        });

           sendNotification(user.getUid());

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (id==R.id.nav_listing) {
            startActivity(new Intent(getApplicationContext(),listing_product.class));
            finish();

        } else if (id==R.id.nav_inventory) {
            startActivity(new Intent(getApplicationContext(), inventory_product.class));
            finish();

        } else if (id==R.id.nav_order) {
            startActivity(new Intent(getApplicationContext(), order_Details.class));
            finish();

        } else if (id==R.id.nav_return) {
            startActivity(new Intent(getApplicationContext(), cancel_order.class));
            finish();

        } else if (id==R.id.nav_report) {
            startActivity(new Intent(getApplicationContext(), report_selling.class));
            finish();
        } else if (id==R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            StyleableToast.makeText(getApplicationContext(),"Logout Successfully",R.style.UptrendToast).show();
            startActivity(new Intent(getApplicationContext(),admin_login.class));
            finish();
        }
        //drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void sendNotification(String adminId){
        DatabaseReference productRef=FirebaseDatabase.getInstance().getReference("Product");
        Query sellerQuery=productRef.orderByChild("adminId").equalTo(adminId);
        sellerQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Product product=dataSnapshot.getValue(Product.class);
                    if(product.getProductSizes()!=null){
                        for(int i=0;i<product.getProductSizes().size();i++){
                            if(Integer.parseInt(product.getProductSizes().get(i))<=3 && Integer.parseInt(product.getProductSizes().get(i))>=1){
                                NotificationHelper.lowStockNotification(getApplicationContext(),product.getProductName());
                                break;
                            } else if (Integer.parseInt(product.getProductSizes().get(i))==0) {
                                NotificationHelper.outOfStockNotification(getApplicationContext(),product.getProductName());
                                break;
                            }
                        }
                    }else{
                       if(Integer.parseInt(product.getTotalStock())<=3 && Integer.parseInt(product.getTotalStock())>=1){
                           NotificationHelper.lowStockNotification(getApplicationContext(),product.getProductName());
                       } else if (Integer.parseInt(product.getTotalStock())==0) {
                           NotificationHelper.outOfStockNotification(getApplicationContext(),product.getProductName());
                       }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
