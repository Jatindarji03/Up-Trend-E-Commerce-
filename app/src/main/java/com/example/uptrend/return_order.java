package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import DataModel.Order;
import DataModel.Product;
import io.github.muddz.styleabletoast.StyleableToast;

public class return_order extends AppCompatActivity {


   private AppCompatButton continue_btn;
    private ShapeableImageView productImage;
    private TextView txtProductName, txtPrice, txtQty,txtProductColour,productSize;
    private RadioGroup radioGroupReason;
    private String orderId;
    private DatabaseReference orderRef, productRef;
    EditText txtComment;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order);


        continue_btn = findViewById(R.id.continue_btn_next);
        productImage=findViewById(R.id.historyProductImageReturn);
        txtProductName=findViewById(R.id.productNameReturn);
        txtQty=findViewById(R.id.txtQty);
        txtProductColour=findViewById(R.id.productColour);
        productSize=findViewById(R.id.productSize);
        radioGroupReason=findViewById(R.id.reasonRadioGroupReturn);
        txtComment=findViewById(R.id.txtComment);
        txtPrice=findViewById(R.id.productPriceReturn);


        orderId = getIntent().getStringExtra("orderId");


        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedButtonId = radioGroupReason.getCheckedRadioButtonId();
                if (checkedButtonId == -1) {
                    StyleableToast.makeText(getApplicationContext(), "Please Select Reason For Cancellation", R.style.UptrendToast).show();
                } else {
                    RadioButton radioButton=findViewById(checkedButtonId);
                    Intent i=new Intent(getApplicationContext(),return_order2.class);
                    i.putExtra("orderId",orderId);
                    i.putExtra("reason",radioButton.getText().toString());
                    i.putExtra("comment",txtComment.getText().toString());

                   startActivity(i);
                }
                //startActivity(new Intent(getApplicationContext(), return_order2.class));
            }
        });

        displayProductDetails(orderId);
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