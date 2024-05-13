package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.adapteranddatamodel.DateAndTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.CancelProduct;
import DataModel.Product;
import DataModel.Return;
import DataModel.UserAddress;

public class open_return_order extends AppCompatActivity {

    private String productId, userId, qty, size, nodeId;
    LinearLayout layoutSize, comment_layout, upiLayout, bankLayout;
    TextView closeBtn;
    private TextView txtBrandName, txtProductName, txtProductPrice, txtProductColourName,
            txtProductSize, txtProductQty, txtUserName, txtUserAddress, txtReturnDate, txtReturnTime, txtReason, txtComment, txtUPI, txtAccountHolderName, txtAccountNumber;

    private DatabaseReference productRef, userAddressRef;
    private ImageSlider productImage;
    private ArrayList<SlideModel> slideModelArrayList;

    private TextView txtDeliveryDate, txtReturnDate2,txtReturnDate3,txtPickUpDate3,txtReturnDate4,txtPickUpDate4,txtRefundDate4;
    RelativeLayout Return_layout1,Return_layout2,Return_layout3;
    TextView btnCloseReturn55;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_return_order);


        //Getting data From previous Activity.
        productId = getIntent().getStringExtra("productId");
        userId = getIntent().getStringExtra("userId");
        qty = getIntent().getStringExtra("qty");
        size = getIntent().getStringExtra("size");
        nodeId = getIntent().getStringExtra("nodeId");

        btnCloseReturn55=findViewById(R.id.closeBtnReturn);



        //FindViewBy Id
        productImage = findViewById(R.id.productImageReturn);
        txtBrandName = findViewById(R.id.productBrandNameReturn);
        txtProductName = findViewById(R.id.productNameReturn);
        txtProductPrice = findViewById(R.id.productPriceReturn);
        txtProductColourName = findViewById(R.id.productColourNameReturn);
        txtProductSize = findViewById(R.id.productSizeReturn);
        txtProductQty = findViewById(R.id.productQtyReturn);
        txtUserName = findViewById(R.id.userNameReturn);
        txtUserAddress = findViewById(R.id.userAddressReturn);
        txtReturnDate = findViewById(R.id.dateReturn);
        txtReturnTime = findViewById(R.id.timeReturn);
        txtReason = findViewById(R.id.reason_Return);
        txtComment = findViewById(R.id.user_comment_Return);
        txtUPI = findViewById(R.id.upi_ID);
        txtAccountHolderName = findViewById(R.id.account_holder_name_return);
        txtAccountNumber = findViewById(R.id.account_holder_number_return);
        txtDeliveryDate = findViewById(R.id.deliveryDate);
        txtReturnDate2=findViewById(R.id.returnDate2);
        txtReturnDate3=findViewById(R.id.txtReturnDate3);
        txtPickUpDate3=findViewById(R.id.txtPickUpDate3);
        txtReturnDate4=findViewById(R.id.txtReturnDate4);
        txtPickUpDate4=findViewById(R.id.txtPickUpDate4);
        txtRefundDate4=findViewById(R.id.txtRefundDate4);




        Return_layout1=findViewById(R.id.Return_layout1);
        Return_layout2=findViewById(R.id.Return_layout2);
        upiLayout = findViewById(R.id.user_uid_layout);
        bankLayout = findViewById(R.id.user_bank_layout);
        comment_layout = findViewById(R.id.comment_layout_Return);
        layoutSize = findViewById(R.id.layoutSizeReturn);
        Return_layout3=findViewById(R.id.Return_layout3);


        btnCloseReturn55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),return_order.class));
                finish();
            }
        });

        displayUserDetails(userId);
        displayProductDetails(productId, qty, size);
        displayReturnDetails(nodeId);
        displayReturnOrderStatus(nodeId);
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


    public void displayReturnDetails(String nodeId) {
        DatabaseReference returnOrderRef = FirebaseDatabase.getInstance().getReference("Return").child(nodeId);
        returnOrderRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Return returnProduct = snapshot.getValue(Return.class);
                            txtReason.setText(returnProduct.getReturnReason());
                            txtReturnDate.setText(returnProduct.getReturnDate());
                            txtReturnTime.setText(returnProduct.getReturnTime());
                            if (returnProduct.getReturnComment().length() == 0) {
                                comment_layout.setVisibility(View.GONE);
                            } else {
                                comment_layout.setVisibility(View.VISIBLE);
                                txtComment.setText(returnProduct.getReturnComment());
                            }
                            if (returnProduct.getRefundType().equals("upi")) {
                                upiLayout.setVisibility(View.VISIBLE);
                                bankLayout.setVisibility(View.GONE);
                                txtUPI.setText(returnProduct.getUpiNo());
                            } else if (returnProduct.getRefundType().equals("account")) {
                                bankLayout.setVisibility(View.VISIBLE);
                                upiLayout.setVisibility(View.GONE);
                                txtAccountHolderName.setText(returnProduct.getAccountName());
                                txtAccountNumber.setText(returnProduct.getAccountNumber());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void displayReturnOrderStatus(String nodeId) {
        DatabaseReference returnOrderRef = FirebaseDatabase.getInstance().getReference("Return").child(nodeId);
        returnOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Return returnProduct = snapshot.getValue(Return.class);
                    if (returnProduct != null) {
                        if (returnProduct.getReturnStatus().equals("return")) {
                            Return_layout1.setVisibility(View.VISIBLE);
                            txtDeliveryDate.setText(DateAndTime.convertDateFormat(returnProduct.getDeliveryDAte()));
                            txtReturnDate2.setText(DateAndTime.convertDateFormat(returnProduct.getReturnDate()));
                        } else if (returnProduct.getReturnStatus().equals("pickup")) {
                            Return_layout2.setVisibility(View.VISIBLE);
                            txtDeliveryDate.setText(DateAndTime.convertDateFormat(returnProduct.getDeliveryDAte()));
                            txtReturnDate3.setText(DateAndTime.convertDateFormat(returnProduct.getReturnDate()));
                            txtPickUpDate3.setText(DateAndTime.convertDateFormat(returnProduct.getPickupDate()));
                        } else if (returnProduct.getReturnStatus().equals("refund")) {
                            Return_layout3.setVisibility(View.VISIBLE);
                            txtDeliveryDate.setText(DateAndTime.convertDateFormat(returnProduct.getDeliveryDAte()));
                            txtReturnDate4.setText(DateAndTime.convertDateFormat(returnProduct.getReturnDate()));
                            txtPickUpDate4.setText(DateAndTime.convertDateFormat(returnProduct.getPickupDate()));
                            txtRefundDate4.setText(DateAndTime.convertDateFormat(returnProduct.getRefundDate()));
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        startActivity(new Intent(getApplicationContext(),return_order.class));
        finish();
    }
}