package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.Product;
import io.github.muddz.styleabletoast.StyleableToast;

public class listing_product extends AppCompatActivity {
    private AutoCompleteTextView spinnerCategory, spinnerSubCategory;
    private ArrayAdapter<String> adapterCategory, adapterSubCategory;
    private ArrayList<String> categoryList, subCategoryList;

    private TextView save_btn_category,using_txt;

    private LinearLayout using_layout;
    private String temp;
    private DatabaseReference categoryData, subCategoryData;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_product);

        using_txt = findViewById(R.id.using_txt);
        using_layout = findViewById(R.id.using_layout);

        //findView by Id of Every Widget.
        spinnerCategory = findViewById(R.id.category);
        spinnerSubCategory = findViewById(R.id.subcategory);
        save_btn_category = findViewById(R.id.save_category_txt);

        // Making Instance of Category Node.
        categoryData = FirebaseDatabase.getInstance().getReference("Category");
        categoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();

        // Displaying The Category in Spinner.
        adapterCategory = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                categoryList);
        spinnerCategory.setAdapter(adapterCategory);

        //getting Instance of Object
        databaseReference = FirebaseDatabase.getInstance().getReference("Product");
        user = FirebaseAuth.getInstance().getCurrentUser();


        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.artist);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        //Fetching The Category From Database.
        categoryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    temp = dataSnapshot.getValue(String.class);
                    categoryList.add(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /* Displaying SubCategory From The Database According to which Category User Select.
         For E.X User Select Book then all Sub Category of Book Will Display In Spinner.*/
        spinnerCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerSubCategory.setText("");
                changeCategory(categoryList.get(i));
            }
        });


        using_txt .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (using_layout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(using_layout, new AutoTransition());

                using_layout.setVisibility(var);
            }
        });

        save_btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput()) {
                    product = new Product();
                    product.setProductCategory(spinnerCategory.getText().toString());
                    product.setProductSubCategory(spinnerSubCategory.getText().toString());
                    product.setAdminId(user.getUid());
                    //this method will add the data into database.
                    addData(product);
                }
            }
        });

    }

    //this Method Will Add the new node and the data to product node.
    private void addData(Product product) {
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(product);
        Intent i = new Intent(getApplicationContext(), add_product_image.class);
        i.putExtra("Category", product.getProductCategory());
        i.putExtra("SubCategory",product.getProductSubCategory());
        i.putExtra("Key", key);
        startActivity(i);
        finish();
    }

    public boolean isValidInput() {
        if (TextUtils.isEmpty(spinnerCategory.getText().toString())) {
            Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(spinnerSubCategory.getText().toString())) {
            Toast.makeText(this, "Please Select Subcategory", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), dashboard_admin.class));
        finish();
    }

    //This Method Will Change The SubCategory Data According to which Category Will Select
    public void changeCategory(String category) {
        subCategoryData = FirebaseDatabase.getInstance().getReference("Subcategory").child(category);
        subCategoryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subCategoryList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    temp = dataSnapshot.getValue(String.class);
                    subCategoryList.add(temp);
                }
                // Displaying The Sub Category Data in Spinner
                adapterSubCategory = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        subCategoryList);
                spinnerSubCategory.setAdapter(adapterSubCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
