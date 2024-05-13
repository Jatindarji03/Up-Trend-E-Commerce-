package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.uptrend.Adapter.AllProductAdapter;
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
import java.util.List;

import DataModel.Product;
import DataModel.RecentlyViewProduct;

public class home extends AppCompatActivity implements Onclick {


    ImageSlider imageSlider;

    CardView searchCard;

    BottomNavigationView bottomNavigationView;
    RecyclerView recycleViewProduct;
    private DatabaseReference productReference, recentlyProductRef;
    private ArrayList<Product> productArrayList;
    private AllProductAdapter productAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout layoutRecentlyViewProduct;
    private RecyclerView recyclerViewRecentlyViewProduct;
    private FirebaseUser user;
    private ArrayList<RecentlyViewProduct> recentlyViewProductArrayList;
    private CardView cardViewNike, cardViewApple, cardViewZara, cardViewRolex, cardViewPuma, cardViewLoreal, cardViewCadbury, cardViewLevis, cardViewCartier;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchCard = findViewById(R.id.searchCard);

        imageSlider = findViewById(R.id.imageSlider);
        recycleViewProduct = findViewById(R.id.recycleView_product);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        recyclerViewRecentlyViewProduct = findViewById(R.id.recyclerViewRecentlyProduct);
        layoutRecentlyViewProduct = findViewById(R.id.layoutRecentlyProduct);

        //Brand Logo FindViewByID
        cardViewNike = findViewById(R.id.cardViewNike);
        cardViewApple = findViewById(R.id.cardViewApple);
        cardViewZara = findViewById(R.id.cardViewZara);
        cardViewRolex = findViewById(R.id.cardViewRolex);
        cardViewPuma = findViewById(R.id.cardViewPuma);
        cardViewLoreal = findViewById(R.id.cardViewLoreal);
        cardViewCadbury = findViewById(R.id.cardViewCadbury);
        cardViewLevis = findViewById(R.id.cardViewLevis);
        cardViewCartier = findViewById(R.id.cardViewCartier);


        cardViewNike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrandActivity("Nike");
            }
        });
        cardViewApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrandActivity("Apple");
            }
        });
        cardViewZara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrandActivity("Zara");
            }
        });
        cardViewRolex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrandActivity("Rolex");
            }
        });
        cardViewPuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrandActivity("Puma");
            }
        });
        cardViewLoreal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrandActivity("Loreal");
            }
        });
        cardViewCadbury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrandActivity("Cadbury");
            }
        });
        cardViewLevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrandActivity("Levi's");
            }
        });
        cardViewCartier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrandActivity("Cartier");
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        ArrayList<SlideModel> slideModels = new ArrayList<>();


        slideModels.add(new SlideModel(R.drawable.auto_img_21, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.auto_img_23, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.auto_img_24, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.auto_img_25, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.auto_img_32, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.auto_img_30, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.auto_img_26, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.auto_img_28, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.auto_img_33, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);


        bottomNavigationView.setSelectedItemId(R.id.bottom_home);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    return true;
                case R.id.bottom_categories:
                    startActivity(new Intent(getApplicationContext(), category_product.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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


        /*
                Displaying Product in RecyclerView
         */
        productArrayList = new ArrayList<>();
        productReference = FirebaseDatabase.getInstance().getReference("Product");
        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // productArrayList.clear();
                for (DataSnapshot productSnapShot : snapshot.getChildren()) {
                    Product product = productSnapShot.getValue(Product.class);
                    productArrayList.add(product);
                }
                Collections.shuffle(productArrayList);
                gridLayoutManager = new GridLayoutManager(home.this, 2, GridLayoutManager.VERTICAL, false);
                productAdapter = new AllProductAdapter(home.this, productArrayList, home.this);
                recycleViewProduct.setLayoutManager(gridLayoutManager);
                recycleViewProduct.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showRecentlyProduct();
    }


    @Override
    public void ItemOnClickListener(String productId) {
        productAlreadyAvailableInRecently(productId);
        Intent i = new Intent(home.this, open_product.class);
        i.putExtra("productId", productId);
        startActivity(i);
    }

    public void productAlreadyAvailableInRecently(String productId) {
        DatabaseReference recentlyProductRef = FirebaseDatabase.getInstance().getReference("RecentlyViewProduct");
        Query recentlyProductQuery = recentlyProductRef.orderByChild("userId").equalTo(user.getUid());

        recentlyProductQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean productAlreadyInRecently = false;

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    RecentlyViewProduct recentlyViewProduct = productSnapshot.getValue(RecentlyViewProduct.class);
                    if (recentlyViewProduct.getProductId().equals(productId)) {
                        // Product is already in the recently viewed list
                        productSnapshot.getRef().child("timeStamp").setValue(String.valueOf(System.currentTimeMillis()));
                        productAlreadyInRecently = true;
                        break; // No need to continue looping once we found the product
                    }
                }

                if (!productAlreadyInRecently) {
                    // Product is not in the recently viewed list, so add it
                    RecentlyViewProduct newProduct = new RecentlyViewProduct();
                    newProduct.setUserId(user.getUid());
                    newProduct.setProductId(productId);
                    newProduct.setTimeStamp(String.valueOf(System.currentTimeMillis()));
                    recentlyProductRef.push().setValue(newProduct);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void showRecentlyProduct() {
        recentlyViewProductArrayList = new ArrayList<>();
        recentlyProductRef = FirebaseDatabase.getInstance().getReference("RecentlyViewProduct");
        Query query = recentlyProductRef.orderByChild("userId").equalTo(user.getUid()).limitToLast(4);
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
                //List<RecentlyViewProduct> latestProducts = new ArrayList<>(recentlyViewProductArrayList.subList(0, Math.min(recentlyViewProductArrayList.size(), 4)));

                if (recentlyViewProductArrayList.size() != 0) {
                    layoutRecentlyViewProduct.setVisibility(View.VISIBLE);
                    RecentlyViewProductAdapter recentlyViewProductAdapter = new RecentlyViewProductAdapter(home.this, recentlyViewProductArrayList, home.this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(home.this, LinearLayoutManager.HORIZONTAL, false);
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

        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(home.this, search_product.class);
                i.putExtra("activityName", "home");
                startActivity(i);

            }
        });

    }

    public void openBrandActivity(String brandName) {
        Intent i = new Intent(home.this, open_brand_product.class);
        i.putExtra("brandName", brandName);
        startActivity(i);
    }
}