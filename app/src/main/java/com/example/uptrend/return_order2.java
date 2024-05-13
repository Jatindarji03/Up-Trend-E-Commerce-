package com.example.uptrend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.adapteranddatamodel.DateAndTime;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import DataModel.CancelProduct;
import DataModel.Order;
import DataModel.Product;
import DataModel.Return;
import DataModel.UserAddress;

public class return_order2 extends AppCompatActivity {
    private String orderId, reason, comment,returnType;
    DatabaseReference orderRef, productRef;


    AppCompatButton continueReturnBtn;


    TextView terms_txt;
    private FirebaseUser firebaseUser;

    private ShapeableImageView productImage;
    EditText upiId,accountName,accountNumber;
    private TextView txtProductName, txtPrice, txtQty, txtProductColour, productSize, txtRefundAmount, txtUserName, txtAddress, txtMobileNumber;


    RadioButton upi_btn, bank_account_btn, radioButtonRefund;
    LinearLayout upi_layout, bank_account_layout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order2);

        terms_txt = findViewById(R.id.terms_txt);
        upi_layout = findViewById(R.id.upi_layout);
        upi_btn = findViewById(R.id.upi_btn);
        bank_account_layout = findViewById(R.id.bank_account_layout);
        bank_account_btn = findViewById(R.id.bank_account_btn);
        continueReturnBtn = findViewById(R.id.return2Continuebtn);
        productImage = findViewById(R.id.historyProductImageReturn2);
        txtProductName = findViewById(R.id.productNameReturn2);
        txtPrice = findViewById(R.id.productPriceReturn2);
        txtQty = findViewById(R.id.txtQty);
        txtProductColour = findViewById(R.id.productColour);
        productSize = findViewById(R.id.txtSize);
        txtRefundAmount = findViewById(R.id.refundAmount);
        txtUserName = findViewById(R.id.userName);
        txtMobileNumber = findViewById(R.id.mobileNumber);
        txtAddress = findViewById(R.id.txtAddress);
        radioButtonRefund = findViewById(R.id.refund);
        upiId=findViewById(R.id.upiId);
        accountName=findViewById(R.id.accountName);
        accountNumber=findViewById(R.id.accountNumber);

        orderId = getIntent().getStringExtra("orderId");
        reason = getIntent().getStringExtra("reason");
        comment = getIntent().getStringExtra("comment");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        upi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (upi_btn.isChecked()) {
                    int var = (upi_layout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                    TransitionManager.beginDelayedTransition(upi_layout, new AutoTransition());
                    upi_layout.setVisibility(var);
                    bank_account_layout.setVisibility(View.GONE);
                    bank_account_btn.setChecked(false);
                    returnType="upi";
                }
            }
        });


        bank_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bank_account_btn.isChecked()) {
                    int var = (bank_account_layout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                    TransitionManager.beginDelayedTransition(bank_account_layout, new AutoTransition());
                    bank_account_layout.setVisibility(var);
                    upi_layout.setVisibility(View.GONE);
                    upi_btn.setChecked(false);
                    returnType="account";
                }
            }
        });


        terms_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), terms_condition.class));
            }
        });


        continueReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioButtonRefund.isChecked() && (upi_btn.isChecked() || bank_account_btn.isChecked())) {
                    returnProcess(orderId,reason,comment,returnType);
                    Intent i=new Intent(getApplicationContext(),splash_return_pd.class);
                    i.putExtra("status","return");
                    startActivity(i);

                }else{
                    Toast.makeText(return_order2.this, "Please Payment Method Select ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        displayProductDetails(orderId);
        showAddress(firebaseUser.getUid());

    }

    public void returnProcess(String orderId,String reason,String comment,String paymentType) {

        orderRef = FirebaseDatabase.getInstance().getReference("Order").child(orderId);
        orderRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Order order = snapshot.getValue(Order.class);
                            if (order != null) {
                                Return returnProduct=new Return();
                                returnProduct.setUserId(order.getUserId());
                                returnProduct.setSellerId(order.getSellerId());
                                returnProduct.setProductId(order.getProductId());
                                returnProduct.setProductSize(order.getProductSize());
                                returnProduct.setProductQty(order.getProductQty());
                                returnProduct.setReturnDate(DateAndTime.getDate());
                                returnProduct.setReturnTime(DateAndTime.getTime());
                                returnProduct.setProductOriginalPrice(order.getProductOriginalPrice());
                                returnProduct.setProductSellingPrice(order.getProductSellingPrice());
                                returnProduct.setReturnComment(comment);
                                returnProduct.setReturnReason(reason);
                                returnProduct.setRefundType(paymentType);
                                returnProduct.setUpiNo(upiId.getText().toString());
                                returnProduct.setAccountName(accountName.getText().toString());
                                returnProduct.setAccountNumber(accountNumber.getText().toString());
                                returnProduct.setReturnStatus("return");
                                returnProduct.setReturnDate(DateAndTime.getDate());
                                returnProduct.setPickupDate(getProjectedDate(3));
                                returnProduct.setRefundDate(getProjectedDate(4));
                                returnProduct.setOrderDate(order.getOrderDate());
                                returnProduct.setShipingDate(order.getShipingDate());
                                returnProduct.setDeliveryDAte(order.getDelliveryDate());
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
                                DatabaseReference returnRef=FirebaseDatabase.getInstance().getReference("Return");
                                returnRef.push().setValue(returnProduct);
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
                                txtRefundAmount.setText(
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
                                                        if (getProductSize(product.getProductCategory(), order.getProductSize()).equals("no")) {
                                                            productSize.setText("");
                                                        } else {
                                                            productSize.setText(getProductSize(product.getProductCategory(), order.getProductSize()) + " , ");
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


    public void showAddress(String userId) {
        DatabaseReference userAddressRef = FirebaseDatabase.getInstance().getReference("UserAddress");
        Query userQuery = userAddressRef.orderByChild("userId").equalTo(userId);
        userQuery.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                            UserAddress userAddress = userSnapshot.getValue(UserAddress.class);
                            txtUserName.setText(userAddress.getFullName());
                            String address = userAddress.getHouseNo() + " , " + userAddress.getRoadName() + " , " + userAddress.getCity() + "  " + userAddress.getPincode();
                            txtAddress.setText(address);
                            txtMobileNumber.setText(userAddress.getMobileNo());
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
    public  String getProjectedDate(int daysToAdd) {
        String formattedEstimatedDeliveryDate = "";
        LocalDate todayDate = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            todayDate = LocalDate.now();
            LocalDate estimatedDeliveryDate = todayDate.plusDays(daysToAdd);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            formattedEstimatedDeliveryDate = estimatedDeliveryDate.format(formatter);
        }

        return formattedEstimatedDeliveryDate;
    }

}