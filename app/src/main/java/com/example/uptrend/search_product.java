package com.example.uptrend;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapteranddatamodel.DiscountRange;
import com.example.adapteranddatamodel.PriceRange;
import com.example.uptrend.Adapter.Onclick;
import com.example.uptrend.Adapter.SearchProductAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

import DataModel.Product;

public class search_product extends AppCompatActivity implements Onclick {
    private TextView iv_mic, txtFilter;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private ArrayList<Product> searchProductArrayList;
    private SearchProductAdapter searchProductAdapter;
    private RecyclerView recyclerViewSearchProduct;
    private EditText searchView;
    private ArrayList<String> colour = new ArrayList<>();
    private String activityName;
    private ArrayList<PriceRange> price = new ArrayList<>();
    CardView filter_CardView;

    private String gender;
    private ArrayList<DiscountRange> discount = new ArrayList<>();
    private String brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);


        searchView = findViewById(R.id.searchView);
        txtFilter = findViewById(R.id.txtFilter);
        iv_mic = findViewById(R.id.iv_mic);
        recyclerViewSearchProduct = findViewById(R.id.recyclerViewSearchProduct);
        searchProductArrayList = new ArrayList<>();
        filter_CardView = findViewById(R.id.filter_CardView);

        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();

        if (searchView.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        colour = getIntent().getStringArrayListExtra("colour");
        activityName = getIntent().getStringExtra("activityName");
        gender = getIntent().getStringExtra("gender");
        brand = getIntent().getStringExtra("brand");
        price = (ArrayList<PriceRange>) getIntent().getSerializableExtra("price");
        discount = (ArrayList<DiscountRange>) getIntent().getSerializableExtra("discount");
        if (activityName.equals("filterActivity")) {
            searchView.clearFocus();
            filterData(gender, colour, brand, price, discount);

            // Toast.makeText(this, "Hi    "+gender, Toast.LENGTH_SHORT).show();
        } else {

        }


        iv_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast
                            .makeText(search_product.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), filter_product.class));
                finish();
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    searchProductArrayList.clear();
                    //filter_CardView.setVisibility(View.GONE);
                    searchProductAdapter.notifyDataSetChanged();
                } else {
                    //filter_CardView.setVisibility(View.VISIBLE);
                    searchProduct(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(search_product.this, LinearLayoutManager.VERTICAL, false);
        searchProductAdapter = new SearchProductAdapter(search_product.this, searchProductArrayList, search_product.this);
        recyclerViewSearchProduct.setLayoutManager(layoutManager);
        recyclerViewSearchProduct.setAdapter(searchProductAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                searchView.setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
    }

    public void searchProduct(String query) {
        searchProductArrayList.clear();
        HashSet<String> addedProductIds = new HashSet<>();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");

        //Query Based On Product Name
        Query productQuery = productRef.orderByChild("productName").startAt(query).endAt(query + "\uf8ff");
        productQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (!addedProductIds.contains(product.getProductId())) {
                        searchProductArrayList.add(product);
                        addedProductIds.add(product.getProductId()); // Add product ID to HashSet
                    }
                }
                searchProductAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Query based on Brand Name
        Query brandNameQuery = productRef.orderByChild("productBrandName").startAt(query).endAt(query + "\uf8ff");
        brandNameQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (!addedProductIds.contains(product.getProductId())) {
                        searchProductArrayList.add(product);
                        addedProductIds.add(product.getProductId()); // Add product ID to HashSet
                    }
                }
                searchProductAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Query Based on SearchKeyword

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataSnapshot searchKeywordNode = dataSnapshot.child("searchKeyWord");
                    for (DataSnapshot keywordSnapshot : searchKeywordNode.getChildren()) {
                        String keyword = keywordSnapshot.getValue(String.class);
                        if (keyword != null && keyword.toLowerCase().contains(query.toLowerCase())) {
                            Product product = dataSnapshot.getValue(Product.class);
                            if (!addedProductIds.contains(product.getProductId())) {
                                searchProductArrayList.add(product);
                                addedProductIds.add(product.getProductId()); // Add product ID to HashSet
                            }
                            break; // No need to check other keywords for this product
                        }
                    }
                }
                searchProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

    }


//    public void filterData(String gender,ArrayList<String> colour){
//        searchProductArrayList.clear();
//        HashSet<String> productId = new HashSet<>();
//        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");
//
//        Query query = productRef;
//        if(gender!=null){
//            if(gender.equals("male")){
//                query = query.orderByChild("productCategory").equalTo("Men's(Top)").getRef()
//                        .orderByChild("productCategory").equalTo("Men's(Bottom)").getRef();
//            } else if (gender.equals("female")) {
//                query = query.orderByChild("productCategory").equalTo("Women's(Top)").getRef()
//                        .orderByChild("productCategory").equalTo("Women's(Bottom)").getRef();
//            }
//        }
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot productSnapShot:snapshot.getChildren()){
//                    Product product=productSnapShot.getValue(Product.class);
//                    if(product!=null){
////                        if(gender==null || (gender.equals("male") || gender.equals("female"))){
////                            if (!productId.contains(product.getProductId())) {
//                                searchProductArrayList.add(product);
//                                productId.add(product.getProductId());
//                            //}
//                        //}
//                    }
//                }
//                searchProductAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void filterData(String gender, ArrayList<String> colour) {
//        searchProductArrayList.clear();
//        HashSet<String> productId = new HashSet<>();
//        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");
//        productRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Product product = dataSnapshot.getValue(Product.class);
//                    if (gender !=null){
//                        if (gender.equals("male")) {
//                            if (product.getProductCategory().equals("Men's(Top)") || product.getProductCategory().equals("Men's(Bottom)")) {
//                                if (!productId.contains(product.getProductId())) {
//                                    searchProductArrayList.add(product);
//                                    productId.add(product.getProductId());
//                                }
//                            }
//                        } else if (gender.equals("female")) {
//                            if (product.getProductCategory().equals("Women's(Top)") || product.getProductCategory().equals("Women's(Bottom)")) {
//                                // searchProductArrayList.add(product);
//                                if (!productId.contains(product.getProductId())) {
//                                    searchProductArrayList.add(product);
//                                    productId.add(product.getProductId());
//                                }
//                            }
//                        }
//                    }else{
//
//                    }
//                }
//                searchProductAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        for(int i=0;i<colour.size();i++){
//            String colourName=colour.get(i);
//            Query colourQuery=productRef.orderByChild("productColour").equalTo(colourName);
//            colourQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                        Product product=dataSnapshot.getValue(Product.class);
//                        if(!productId.contains(product.getProductId())){
//                            searchProductArrayList.add(product);
//                            productId.add(product.getProductId());
//                        }
//                    }
//                    searchProductAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//    }


//    public  void filterData(String gender,ArrayList<String> colour){
//        searchProductArrayList.clear();
//        HashSet<String> productId = new HashSet<>();
//        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");
//        productRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    Product product=dataSnapshot.getValue(Product.class);
//                    if(product!=null){
//                        if(gender==null || gender.equals("male")){
//                            if (product.getProductCategory().equals("Men's(Top)") || product.getProductCategory().equals("Men's(Bottom)")) {
//                                if (!productId.contains(product.getProductId())) {
//                                    searchProductArrayList.add(product);
//                                    productId.add(product.getProductId());
//                                }
//                            }
//                        } else if (gender==null || gender.equals("female")) {
//                            if (product.getProductCategory().equals("Women's(Top)") || product.getProductCategory().equals("Women's(Bottom)")) {
//                                // searchProductArrayList.add(product);
//                                if (!productId.contains(product.getProductId())) {
//                                    searchProductArrayList.add(product);
//                                    productId.add(product.getProductId());
//                                }
//                            }
//                        }
//                    }
//                }
//                searchProductAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

//    public void filterData(String gender, ArrayList<String> colour,ArrayList<String> price) {
//        searchProductArrayList.clear();
//        HashSet<String> productId = new HashSet<>();
//        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");
//
//        productRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Product product = dataSnapshot.getValue(Product.class);
//                    if (product != null) {
//                        if (gender == null || gender.isEmpty()) {
//                            // If gender is not specified, filter by color
//                            if (colour.isEmpty() || colour.contains(product.getProductColour())) {
//                                if (!productId.contains(product.getProductId())) {
//                                    searchProductArrayList.add(product);
//                                    productId.add(product.getProductId());
//                                }
//                            }
//                        } else {
//                            // If gender is specified, filter by gender
//                            if ((gender.equals("male") && product.getProductCategory().startsWith("Men's")) ||
//                                    (gender.equals("female") && product.getProductCategory().startsWith("Women's"))) {
//                                if (!productId.contains(product.getProductId())) {
//                                    searchProductArrayList.add(product);
//                                    productId.add(product.getProductId());
//                                }
//                            }
//                        }
//                    }
//                }
//                searchProductAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//    }

    public void filterData(String gender, ArrayList<String> colour, String brand, ArrayList<PriceRange> price, ArrayList<DiscountRange> discount) {
        searchProductArrayList.clear();
        HashSet<String> productId = new HashSet<>();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        boolean passFilter = true;
                        if (gender != null && !gender.isEmpty()) {
                            // If gender is specified, check if the product's category matches the selected gender
                            passFilter = passFilter && ((gender.equals("male") && product.getProductSuitFor().startsWith("Men")) ||
                                    (gender.equals("female") && product.getProductSuitFor().startsWith("Women")));
                        }
                        if (!colour.isEmpty()) {
                            // If color is selected, check if the product's color matches any selected color
                            passFilter = passFilter && colour.contains(product.getProductColour());
                        }
                        if (brand != null && !brand.isEmpty()) {
                            // If Brand  is selected, check if the product's Brand matches any selected Brand
                            passFilter = passFilter && brand.equals(product.getProductBrandName());
                        }
                        if (!price.isEmpty()) {
                            passFilter = passFilter && isProductInPriceRange(Long.parseLong(product.getSellingPrice()), price);
                        }
                        if (!discount.isEmpty()) {
                            passFilter = passFilter && isDiscountInRange(Long.parseLong(product.getOriginalPrice()), Long.parseLong(product.getSellingPrice()), discount);
                        }


                        if (passFilter && !productId.contains(product.getProductId())) {
                            // If the product passes all filters and hasn't been added yet, add it to the list
                            searchProductArrayList.add(product);
                            productId.add(product.getProductId());
                        }
                    }
                }
                searchProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


    private boolean isProductInPriceRange(long price, ArrayList<PriceRange> priceRanges) {
        // Check if the product's price falls within any of the selected price ranges
        for (PriceRange range : priceRanges) {
            if (price >= Long.parseLong(range.getMinPrice()) && price <= Long.parseLong(range.getMaxPrice())) {
                return true;
            }
        }
        return false;
    }

    private boolean isDiscountInRange(long originalPrice, long sellingPrice, ArrayList<DiscountRange> discount) {
        for (DiscountRange range : discount) {
            double discountPer = calculateDiscountPercentage(originalPrice, sellingPrice);
            if (discountPer >= Double.parseDouble(range.getMinDiscount()) && discountPer <= Double.parseDouble(range.getMaxDiscount())) {
                return true;
            }
        }
        return false;
    }

    public double calculateDiscountPercentage(long originalPrice, long sellingPrice) {
        double discount = originalPrice - sellingPrice;
        return (discount / originalPrice) * 100;
    }


    @Override
    public void ItemOnClickListener(String productId) {
        Intent i = new Intent(search_product.this, open_product.class);
        i.putExtra("productId", productId);
        startActivity(i);
        finish();
    }
}
