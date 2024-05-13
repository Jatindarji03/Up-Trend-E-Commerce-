package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class category_product extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private RelativeLayout layoutShoes, layoutMobiles, layoutHoodies, layoutJewellery, layoutShirts, layoutChocolates,
            layoutJeansPant, layoutTeddyBear, layoutBeauty, layoutWatches, layoutSports, layoutSarees, layoutGlasses, layoutDresses, layoutArts;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);

        //FindViewById
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        layoutShoes = findViewById(R.id.layoutShoes);
        layoutMobiles = findViewById(R.id.layoutMobiles);
        layoutHoodies = findViewById(R.id.layoutHoodies);
        layoutJewellery = findViewById(R.id.layoutJewellery);
        layoutShirts = findViewById(R.id.layoutShirt);
        layoutChocolates = findViewById(R.id.layoutChocolates);
        layoutJeansPant = findViewById(R.id.layoutJeansPant);
        layoutTeddyBear = findViewById(R.id.layoutTeddyBear);
        layoutBeauty = findViewById(R.id.layoutBeauty);
        layoutWatches = findViewById(R.id.layoutWatches);
        layoutSports = findViewById(R.id.layoutSports);
        layoutSarees = findViewById(R.id.layoutSarees);
        layoutGlasses = findViewById(R.id.layoutGlasses);
        layoutDresses = findViewById(R.id.layoutDresses);
        layoutArts = findViewById(R.id.layoutArts);

        bottomNavigationView.setSelectedItemId(R.id.bottom_categories);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), home.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_categories:
                    return true;
                case R.id.bottom_account:
                    startActivity(new Intent(getApplicationContext(), account_user.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_add_to_cart:
                    startActivity(new Intent(getApplicationContext(), add_to_cart_product.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });


        //ClickEvent Of Category.
        layoutShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("Category", "Footware");

            }
        });
        layoutMobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("SubCategory", "Smartphones");
            }
        });
        layoutHoodies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("SubCategory", "Sweaters and Hoodies");
            }
        });
        layoutJewellery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("Category", "Jewellery");
            }
        });
        layoutShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("SubCategory", "Shirts");
            }
        });
        layoutChocolates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("Category", "Chocolate");
            }
        });
        layoutJeansPant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("SubCategory", "Jeans");
            }
        });
        layoutTeddyBear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("SubCategory", "Teddy Bear");
            }
        });
        layoutBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("Category", "Beauty");
            }
        });
        layoutWatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("Category", "Watches");
            }
        });
        layoutSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("Category", "Sports");
            }
        });
        layoutSarees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("SubCategory", "Saree");
            }
        });
        layoutGlasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("Category", "EyeWear");
            }
        });
        layoutDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("SubCategory", "Dresses");
            }
        });
        layoutArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryActivity("Category","Art");
            }
        });


    }

    public void openCategoryActivity(String sortBy, String value) {
        Intent i = new Intent(category_product.this, open_category_product.class);
        i.putExtra("sortBy", sortBy);
        i.putExtra("value", value);
        startActivity(i);

    }
}