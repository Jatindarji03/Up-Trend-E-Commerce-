package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uptrendseller.Adapter.ProductImageAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import DataModel.Product;
import io.github.muddz.styleabletoast.StyleableToast;

public class add_product_image extends AppCompatActivity {
    private ViewPager2 viewPager2;

    private TextView save_image_btn,txt;
    private AppCompatButton btnSelectImage, shine_btn;
    private static final int Read_Permission = 101;
    private ArrayList<Uri> uri = new ArrayList<>();
    private ProductImageAdapter productImageAdapter;
    private Handler sliderHandler = new Handler();
    private String category,key,SubCategory;
    private StorageReference storageReference;
    private loadingDialog2 loading;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_image);

        //FindView By Id of Widget
        viewPager2 = findViewById(R.id.viewPager2);
        btnSelectImage = findViewById(R.id.selectImage);
        shine_btn = findViewById(R.id.shine_btn_image);

        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();


        //save image
        save_image_btn = findViewById(R.id.save_image_txt);


        txt = findViewById(R.id.text_hide);


        txt.postDelayed(new Runnable() {
            @Override
            public void run() {
                txt.setVisibility(View.GONE);
            }

        },4200);

        //GetIng the Category from previous activity and store in variable.
        category = getIntent().getStringExtra("Category");
        key=getIntent().getStringExtra("Key");
        SubCategory=getIntent().getStringExtra("SubCategory");



        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(add_product_image.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }

        productImageAdapter = new ProductImageAdapter(uri, viewPager2);
        viewPager2.setAdapter(productImageAdapter);

        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);


        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.14f);
            }
        });
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread((new Runnable() {
                    @Override
                    public void run() {
                        shineStart();
                    }
                }));
            }
        }, 1, 2, TimeUnit.SECONDS);

        //getting Instance of Object
        storageReference= FirebaseStorage.getInstance().getReference();
        loading=new loadingDialog2(this);

//        save_image_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loading.show();
//                uploadMultipleImage(uri);
////                if (uri.size() >= 3 && uri.size()<=5) {
//
////                    Intent i = new Intent(getApplicationContext(), add_product_details.class);
////                    i.putExtra("Category", category);
////                    startActivity(i);
////                } else {
////                    Toast.makeText(add_product_image.this, "Select between 3 and 5 Images", Toast.LENGTH_SHORT).show();
////                }
//
//            }
//        });
        save_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri.size() >= 2 && uri.size() <= 5) {
                    loading.show();
                    uploadMultipleImage(uri);
                } else {
                    StyleableToast.makeText(getApplicationContext(), "Select Image Between 2 to 5", R.style.UptrendToast).show();

                }

            }
        });


        viewPager2.setPageTransformer(transformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000);
            }
        });


        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uri.clear();
                Intent intent = new Intent();
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
            }
        });

    }
private void uploadMultipleImage(ArrayList<Uri> imageUri) {
    Product product = new Product();
    ArrayList<String> list = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product").child(key);



    for (Uri images : imageUri) {
        StorageReference imageRef = storageReference.child("Product Images/images" + UUID.randomUUID().toString());
        imageRef.putFile(images)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        list.add(uri.toString());

                        // Check if all images have been uploaded
                        if (list.size() == imageUri.size()) {
                            // All images uploaded, update the data model and push to the database

                            product.setProductImages(list);
                            HashMap<String,Object> image=new HashMap<>();
                            image.put("productImages",product.getProductImages());
                            image.put("productId",key);
                            databaseReference.updateChildren(image);
                            loading.cancel();
                            StyleableToast.makeText(this, "Image Uploaded", R.style.UptrendToast).show();
                            Intent i=new Intent(getApplicationContext(),add_product_details.class);
                            i.putExtra("Category",category);
                            i.putExtra("Key",key);
                            i.putExtra("SubCategory",SubCategory);
                            startActivity(i);
                            finish();
                           // startActivity(new Intent(getApplicationContext(),profile_seller.class));
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }
}



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();

                for (int i = 0; i < x; i++) {
                    uri.add(data.getClipData().getItemAt(i).getUri());
                }
                productImageAdapter.notifyDataSetChanged();
                //textView.setText("Photos ("+uri.size()+")");
            } else if (data.getData() != null) {
                String imageURL = data.getData().getPath();
                uri.add(Uri.parse(imageURL));
            }
        }
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }

    private void shineStart() {
        Animation animation = new TranslateAnimation(
                0, btnSelectImage.getWidth() + shine_btn.getWidth(), 0, 0);

        animation.setDuration(600);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        shine_btn.startAnimation(animation);
    }
}

