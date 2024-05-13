package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.CancelProduct;
import DataModel.Product;
import DataModel.UserAddress;

public class open_cancel_order extends AppCompatActivity {
    private String productId, userId, qty, size, nodeId;
    LinearLayout layoutSize,comment_layout;
    TextView closeBtn;
    private TextView txtBrandName, txtProductName, txtProductPrice, txtProductColourName,
            txtProductSize, txtProductQty, txtUserName, txtUserAddress, txtCancelDate, txtCancelTime,txtReason,txtComment;

    private DatabaseReference productRef, userAddressRef;
    TextView closeBtnCancel;
    private ImageSlider productImage;
    private ArrayList<SlideModel> slideModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cancel_order);

        //Getting data From previous Activity.
        productId = getIntent().getStringExtra("productId");
        userId = getIntent().getStringExtra("userId");
        qty = getIntent().getStringExtra("qty");
        size = getIntent().getStringExtra("size");
        nodeId = getIntent().getStringExtra("nodeId");


        //FindViewBy Id
        productImage=findViewById(R.id.productImageCancel);
        txtBrandName=findViewById(R.id.productBrandNameCancel);
        txtProductName=findViewById(R.id.productNameCancel);
        txtProductPrice=findViewById(R.id.productPriceCancel);
        txtProductColourName=findViewById(R.id.productColourNameCancel);
        txtProductSize=findViewById(R.id.productSizeCancel);
        txtProductQty=findViewById(R.id.productQtyCancel);
        txtUserName=findViewById(R.id.userNameCancel);
        txtUserAddress=findViewById(R.id.userAddressCancel);
        txtCancelDate=findViewById(R.id.dateCancel);
        txtCancelTime=findViewById(R.id.timeCancel);
        txtReason=findViewById(R.id.reason_cancel);
        txtComment=findViewById(R.id.user_comment);

        comment_layout=findViewById(R.id.comment_layout);
        layoutSize=findViewById(R.id.layoutSizeCancel);
        closeBtnCancel=findViewById(R.id.closeBtnCancel);


        closeBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),cancel_order.class));
                finish();
            }
        });


        displayUserDetails(userId);
        displayProductDetails(productId, qty, size);
        displayCancelDetails(nodeId);




    }
    public void displayUserDetails(String userId) {
        userAddressRef = FirebaseDatabase.getInstance().getReference("UserAddress");
        Query query = userAddressRef.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    UserAddress userAddress = userSnapshot.getValue(UserAddress.class);

                    String address = userAddress.getHouseNo() + " , " + userAddress.getRoadName() + " , " + userAddress.getCity() + " - " + userAddress.getPincode();
                    txtUserName.setText(userAddress.getFullName());
                    txtUserAddress.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void displayProductDetails(String productId, String qty, String size) {
        slideModelArrayList = new ArrayList<>();
        productRef = FirebaseDatabase.getInstance().getReference("Product").child(productId);
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                for (int i = 0; i < product.getProductImages().size(); i++) {
                    slideModelArrayList.add(new SlideModel(product.getProductImages().get(i), ScaleTypes.FIT));
                }
                productImage.setImageList(slideModelArrayList, ScaleTypes.FIT);
                txtBrandName.setText(product.getProductBrandName());
                txtProductName.setText(product.getProductName());
                txtProductPrice.setText(product.getSellingPrice());
                txtProductColourName.setText(product.getProductColour());
                txtProductQty.setText(qty);
                if (getProductSize(product.getProductCategory(), size).equals("no")) {
                    layoutSize.setVisibility(View.GONE);
                } else {
                    layoutSize.setVisibility(View.VISIBLE);
                    txtProductSize.setText(getProductSize(product.getProductCategory(), size));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void displayCancelDetails(String nodeId){
        DatabaseReference cancelOrderRef=FirebaseDatabase.getInstance().getReference("Cancel").child(nodeId);
        cancelOrderRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            CancelProduct cancelProduct=snapshot.getValue(CancelProduct.class);
                            txtReason.setText(cancelProduct.getCancelReason());
                            txtCancelDate.setText(cancelProduct.getCancelDate());
                            txtCancelTime.setText(cancelProduct.getCancelTime());
                            if(cancelProduct.getCancelComment().length()==0){
                                comment_layout.setVisibility(View.GONE);
                            }else{
                                comment_layout.setVisibility(View.VISIBLE);
                                txtComment.setText(cancelProduct.getCancelComment());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),cancel_order.class));
        finish();
    }
}