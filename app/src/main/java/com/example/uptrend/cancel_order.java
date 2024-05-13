package com.example.uptrend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.adapteranddatamodel.DateAndTime;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import DataModel.CancelProduct;
import DataModel.Order;
import DataModel.Product;
import io.github.muddz.styleabletoast.StyleableToast;

public class cancel_order extends AppCompatActivity {

    private AppCompatButton continue_btn24;
    private RadioGroup radioGroupReason;
    private String orderId;
    private DatabaseReference orderRef, productRef;
    private ShapeableImageView productImage;
    private TextView txtProductName, txtPrice, txtQty,txtProductColour,productSize;

    EditText txtComment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);


        continue_btn24 = findViewById(R.id.continue_btn_splash);
        radioGroupReason = findViewById(R.id.reasonRadioGroup);
        productImage = findViewById(R.id.historyProductImageCancel);
        txtProductName = findViewById(R.id.productNameCancel);
        txtQty = findViewById(R.id.txtQty);
        txtPrice = findViewById(R.id.productPriceCancel);
        txtComment=findViewById(R.id.txtComment);
        txtProductColour=findViewById(R.id.productColour);
        productSize=findViewById(R.id.productSize);

        orderId = getIntent().getStringExtra("orderId");


        continue_btn24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedButtonId = radioGroupReason.getCheckedRadioButtonId();
                if (checkedButtonId == -1) {
                    StyleableToast.makeText(getApplicationContext(), "Please Select Reason For Cancellation", R.style.UptrendToast).show();
                } else {
                    RadioButton radioButton=findViewById(checkedButtonId);
                    cancellationProcess(orderId,radioButton.getText().toString(),txtComment.getText().toString());
                    Intent i=new Intent(getApplicationContext(),splash_return_pd.class);
                    i.putExtra("status","cancel");
                    startActivity(i);
                }
            }
        });


        displayProductDetails(orderId);

    }

    public void cancellationProcess(String orderId,String reason,String comment) {

        orderRef = FirebaseDatabase.getInstance().getReference("Order").child(orderId);
        orderRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Order order = snapshot.getValue(Order.class);
                            if (order != null) {
                                CancelProduct cancelProduct=new CancelProduct();
                                cancelProduct.setUserId(order.getUserId());
                                cancelProduct.setSellerId(order.getSellerId());
                                cancelProduct.setProductId(order.getProductId());
                                cancelProduct.setProductSize(order.getProductSize());
                                cancelProduct.setProductQty(order.getProductQty());
                                cancelProduct.setCancelDate(DateAndTime.getDate());
                                cancelProduct.setCancelTime(DateAndTime.getTime());
                                cancelProduct.setProductOriginalPrice(order.getProductOriginalPrice());
                                cancelProduct.setProductSellingPrice(order.getProductSellingPrice());
                                cancelProduct.setCancelComment(comment);
                                cancelProduct.setCancelReason(reason);
                                if (order.getProductSize() != null) {
                                    int sizeQty = Integer.parseInt(order.getProductQty());
                                    int size = Integer.parseInt(order.getProductSize());
                                    productRef = FirebaseDatabase.getInstance().getReference("Product").child(order.getProductId());
                                    productRef.addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        Product product = snapshot.getValue(Product.class);
                                                        if (product != null) {
                                                            DatabaseReference qtyRef = snapshot.getRef().child("productSizes");
                                                            DatabaseReference totalStockRef = snapshot.getRef().child("totalStock");
                                                            int currentQty = Integer.parseInt(product.getProductSizes().get(size));
                                                            int currentTotal = Integer.parseInt(product.getTotalStock());
                                                            currentQty = currentQty + sizeQty;
                                                            product.getProductSizes().remove(size);
                                                            product.getProductSizes().add(size, String.valueOf(currentQty));
                                                            currentTotal = currentTotal + sizeQty;
                                                            qtyRef.setValue(product.getProductSizes());
                                                            totalStockRef.setValue(String.valueOf(currentTotal));
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            }
                                    );
                                } else {
                                    int qty = Integer.parseInt(order.getProductQty());
                                    productRef = FirebaseDatabase.getInstance().getReference("Product").child(order.getProductId());
                                    productRef.addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        Product product = snapshot.getValue(Product.class);
                                                        if (product != null) {
                                                            DatabaseReference totalStockRef = snapshot.getRef().child("totalStock");
                                                            int currentTotal = Integer.parseInt(product.getTotalStock());
                                                            currentTotal = currentTotal + qty;
                                                            totalStockRef.setValue(String.valueOf(currentTotal));
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            }
                                    );
                                }
                                DatabaseReference cancelOrderRef=FirebaseDatabase.getInstance().getReference("Cancel");
                                cancelOrderRef.push().setValue(cancelProduct);
                                orderRef.removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }



    public void displayProductDetails(String orderId) {
        orderRef = FirebaseDatabase.getInstance().getReference("Order").child(orderId);
        orderRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Order order = snapshot.getValue(Order.class);
                            if (order != null) {
                                txtQty.setText(order.getProductQty());
                                txtPrice.setText(
                                        String.valueOf(
                                                Integer.parseInt(order.getProductQty()) *
                                                        Integer.parseInt(order.getProductSellingPrice())
                                        )
                                );

                                productRef = FirebaseDatabase.getInstance().getReference("Product").child(order.getProductId());
                                productRef.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Product product = snapshot.getValue(Product.class);
                                                    if (product != null) {
                                                        txtProductName.setText(product.getProductName());
                                                        Glide.with(getApplicationContext()).load(product.getProductImages().get(0)).into(productImage);
                                                        txtProductColour.setText(product.getProductColour());
                                                        if(getProductSize(product.getProductCategory(), order.getProductSize()).equals("no")){
                                                            productSize.setText("");
                                                        }else{
                                                            productSize.setText(getProductSize(product.getProductCategory(), order.getProductSize())+" , ");
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        }
                                );
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(cancel_order.this, open_history_pd.class);
        i.putExtra("orderId", orderId);
        startActivity(i);
        finish();
    }

    public String getProductSize(String category, String index) {
        String size = "";
        if (category.equals("Men's(Top)") || category.equals("Women's(Top)")) {
            if (index.equals("0")) size = "S";
            else if (index.equals("1")) size = "M";
            else if (index.equals("2")) size = "L";
            else if (index.equals("3")) size = "XL";
            else if (index.equals("4")) size = "XXL";
        } else if (category.equals("Men's(Bottom)") || category.equals("Women's(Bottom)")) {
            if (index.equals("0")) size = "28";
            else if (index.equals("1")) size = "30";
            else if (index.equals("2")) size = "32";
            else if (index.equals("3")) size = "34";
            else if (index.equals("4")) size = "36";
            else if (index.equals("5")) size = "38";
            else if (index.equals("6")) size = "40";

        } else if (category.equals("Footware(Men)") || category.equals("Footware(Women)")) {
            if (index.equals("0")) size = "6";
            else if (index.equals("1")) size = "7";
            else if (index.equals("2")) size = "8";
            else if (index.equals("3")) size = "9";
            else if (index.equals("3")) size = "10";
        } else {
            size = "no";
        }
        return size;
    }
}