package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adapteranddatamodel.DateAndTime;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import DataModel.Order;
import DataModel.Product;
import DataModel.Return;

public class open_history_pd extends AppCompatActivity {
    String orderId, status;
    CardView completeOrderLayoutTop, returnOrderLayoutTop;
    TextView close_btn_RA;


    AppCompatButton return_order_btn, cancel_order_btn2;
    RelativeLayout returnOrderBottomLayout, return_layout1, return_layout2, return_layout3;

    DatabaseReference orderRef, productRef, requestRef;
    ShapeableImageView productImage;
    TextView productSize, productName, productColour, storeName, productPrice, productOriginalPrice, productDiscountPrice, productTotalPrice, productTotalAmount, txtReturnDate2;
    LinearLayout layoutSize, layoutQuntityTotal, cancel_return_layout, layoutPriceDetailsHI;
    RelativeLayout first_purchasing_layout, shipping_layout, delivered_layout;
    TextView txtOrderDate, shipingDate, orderShipped2, deliveredDate, outOfDelivedDate, txtTotalQty, deliverDate2, returnDate, txtDelivery3, retrunDate2, pickupDate, deliveryDate4, returnDate4, pickupdate4, refund;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_history_pd);

        productImage = findViewById(R.id.productImage);
        productSize = findViewById(R.id.productSize);
        productName = findViewById(R.id.productName);
        productColour = findViewById(R.id.productColour);
        storeName = findViewById(R.id.storeName);
        productPrice = findViewById(R.id.productPrice);
        productDiscountPrice = findViewById(R.id.sellingPrice);
        productOriginalPrice = findViewById(R.id.total_price_HI);
        productTotalPrice = findViewById(R.id.total_order_A2C);
        productTotalAmount = findViewById(R.id.totalAmt);
        layoutSize = findViewById(R.id.layoutSize);
        first_purchasing_layout = findViewById(R.id.first_purchasing_layout);
        shipping_layout = findViewById(R.id.shipping_layout);
        delivered_layout = findViewById(R.id.delivered_layout);
        txtOrderDate = findViewById(R.id.txtOrderDAte);
        shipingDate = findViewById(R.id.shipingDate);
        orderShipped2 = findViewById(R.id.orderShipped2);
        deliveredDate = findViewById(R.id.deliveredDate);
        outOfDelivedDate = findViewById(R.id.outOfDelivedDate);
        layoutQuntityTotal = findViewById(R.id.layoutQuntityTotal);
        return_order_btn = findViewById(R.id.return_order_btn);
        cancel_order_btn2 = findViewById(R.id.cancel_order_btn);
        txtTotalQty = findViewById(R.id.txtTotalHI);
        cancel_return_layout = findViewById(R.id.cancel_return_layout);
        returnOrderLayoutTop = findViewById(R.id.returnOrderLayoutTop);
        completeOrderLayoutTop = findViewById(R.id.completeOrderLayoutTop);
        layoutPriceDetailsHI = findViewById(R.id.layoutPriceDetailsHI);
        returnOrderBottomLayout = findViewById(R.id.returnOrderBottomLayout);
        txtReturnDate2 = findViewById(R.id.txtReturnDate2);
        deliverDate2 = findViewById(R.id.deliverDate2);
        return_layout1 = findViewById(R.id.return_layout1);
        returnDate = findViewById(R.id.returnDate);
        return_layout2 = findViewById(R.id.return_layout2);
        txtDelivery3 = findViewById(R.id.txtDelivery3);
        retrunDate2 = findViewById(R.id.retrunDate2);
        pickupDate = findViewById(R.id.pickupDate);
        return_layout3 = findViewById(R.id.return_layout3);
        deliveryDate4 = findViewById(R.id.deliveryDate4);
        returnDate4 = findViewById(R.id.returnDate4);
        pickupdate4 = findViewById(R.id.pickupdate4);
        refund = findViewById(R.id.refund);
        close_btn_RA=findViewById(R.id.close_btn_RA);



        orderId = getIntent().getStringExtra("orderId");
        status = getIntent().getStringExtra("status");
        if (status.equals("order")) {
            completeOrderLayoutTop.setVisibility(View.VISIBLE);
            returnOrderLayoutTop.setVisibility(View.GONE);
            layoutPriceDetailsHI.setVisibility(View.VISIBLE);
            returnOrderBottomLayout.setVisibility(View.GONE);
            displayOrderDetails(orderId);
        } else if (status.equals("return")) {
            completeOrderLayoutTop.setVisibility(View.GONE);
            returnOrderLayoutTop.setVisibility(View.VISIBLE);
            cancel_return_layout.setVisibility(View.GONE);
            layoutPriceDetailsHI.setVisibility(View.GONE);
            returnOrderBottomLayout.setVisibility(View.VISIBLE);
            displayReturnOrderDetail(orderId);
        }

        close_btn_RA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),complete_order.class));
                finish();
            }
        });

        return_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(open_history_pd.this, return_order.class);
                i.putExtra("orderId", orderId);
                startActivity(i);
                finish();
            }
        });

        cancel_order_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(open_history_pd.this, cancel_order.class);
                i.putExtra("orderId", orderId);
                startActivity(i);
                finish();
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

    public void displayOrderDetails(String orderId) {
        orderRef = FirebaseDatabase.getInstance().getReference("Order").child(orderId);
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    productRef = FirebaseDatabase.getInstance().getReference("Product").child(order.getProductId());
                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Product product = snapshot.getValue(Product.class);
                                productName.setText(product.getProductName());
                                Glide.with(getApplicationContext()).load(product.getProductImages().get(0)).into(productImage);
                                productColour.setText(product.getProductColour());
                                productPrice.setText(product.getSellingPrice());
                                long totalOriginalPrice = Integer.parseInt(order.getProductQty()) *
                                        Integer.parseInt(order.getProductOriginalPrice());
                                productOriginalPrice.setText(String.valueOf(totalOriginalPrice));
                                long discountPrice = Long.parseLong(order.getProductOriginalPrice()) - Long.parseLong(order.getProductSellingPrice());
                                discountPrice = discountPrice * Integer.parseInt(order.getProductQty());
                                productDiscountPrice.setText(String.valueOf(discountPrice));
                                long totalPrice = totalOriginalPrice - discountPrice;
                                productTotalAmount.setText(String.valueOf(totalPrice));
                                productTotalPrice.setText(String.valueOf(totalPrice));
                                if (getProductSize(product.getProductCategory(), order.getProductSize()) == "no") {
                                    layoutSize.setVisibility(View.GONE);
                                } else {
                                    layoutSize.setVisibility(View.VISIBLE);
                                    productSize.setText(getProductSize(product.getProductCategory(), order.getProductSize()));
                                }
                                displayStoreName(order.getSellerId());
                                if (order.getOrderStatus().equals("new")) {
                                    first_purchasing_layout.setVisibility(View.VISIBLE);
                                    shipping_layout.setVisibility(View.GONE);
                                    delivered_layout.setVisibility(View.GONE);
                                    txtOrderDate.setText(DateAndTime.convertDateFormat(order.getOrderDate()));
                                    cancel_return_layout.setVisibility(View.VISIBLE);
                                    cancel_order_btn2.setVisibility(View.VISIBLE);
                                } else if (order.getOrderStatus().equals("shiping")) {
                                    first_purchasing_layout.setVisibility(View.GONE);
                                    shipping_layout.setVisibility(View.VISIBLE);
                                    delivered_layout.setVisibility(View.GONE);
                                    txtOrderDate.setText(DateAndTime.convertDateFormat(order.getOrderDate()));
                                    shipingDate.setText(DateAndTime.convertDateFormat(order.getShipingDate()));
                                    cancel_return_layout.setVisibility(View.GONE);
                                } else if (order.getOrderStatus().equals("delivered")) {
                                    first_purchasing_layout.setVisibility(View.GONE);
                                    shipping_layout.setVisibility(View.GONE);
                                    delivered_layout.setVisibility(View.VISIBLE);
                                    txtOrderDate.setText(DateAndTime.convertDateFormat(order.getOrderDate()));
                                    orderShipped2.setText(DateAndTime.convertDateFormat(order.getShipingDate()));
                                    deliveredDate.setText(DateAndTime.convertDateFormat(order.getDelliveryDate()));
                                    outOfDelivedDate.setText(DateAndTime.convertDateFormat(order.getDelliveryDate()));
                                    String deliveryDate = order.getDelliveryDate();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    Calendar calendar = Calendar.getInstance();
                                    try {
                                        Date parsedDeliveryDate = dateFormat.parse(deliveryDate);
                                        calendar.setTime(parsedDeliveryDate);
                                        calendar.add(Calendar.DAY_OF_MONTH, 7);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (DateAndTime.getDate().compareTo(
                                            dateFormat.format(calendar.getTime())
                                    ) > 0) {
                                        cancel_return_layout.setVisibility(View.GONE);
                                        cancel_order_btn2.setVisibility(View.GONE);
                                        return_order_btn.setVisibility(View.GONE);
                                    } else {
                                        cancel_return_layout.setVisibility(View.VISIBLE);
                                        cancel_order_btn2.setVisibility(View.GONE);
                                        return_order_btn.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (order.getProductQty().equals("1")) {
                                    layoutQuntityTotal.setVisibility(View.GONE);
                                } else {
                                    layoutQuntityTotal.setVisibility(View.VISIBLE);
                                    txtTotalQty.setText(order.getProductQty());
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void displayReturnOrderDetail(String orderId) {
        requestRef = FirebaseDatabase.getInstance().getReference("Return").child(orderId);
        requestRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Return returnProduct = snapshot.getValue(Return.class);
                            productRef = FirebaseDatabase.getInstance().getReference("Product").child(returnProduct.getProductId());
                            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Product product = snapshot.getValue(Product.class);
                                        productName.setText(product.getProductName());
                                        Glide.with(getApplicationContext()).load(product.getProductImages().get(0)).into(productImage);
                                        productColour.setText(product.getProductColour());
                                        productPrice.setText(product.getSellingPrice());
                                        long totalOriginalPrice = Integer.parseInt(returnProduct.getProductQty()) *
                                                Integer.parseInt(returnProduct.getProductOriginalPrice());
                                        productOriginalPrice.setText(String.valueOf(totalOriginalPrice));
                                        long discountPrice = Long.parseLong(returnProduct.getProductOriginalPrice()) - Long.parseLong(returnProduct.getProductSellingPrice());
                                        discountPrice = discountPrice * Integer.parseInt(returnProduct.getProductQty());
                                        productDiscountPrice.setText(String.valueOf(discountPrice));
                                        long totalPrice = totalOriginalPrice - discountPrice;
                                        productTotalAmount.setText(String.valueOf(totalPrice));
                                        productTotalPrice.setText(String.valueOf(totalPrice));
                                        if (getProductSize(product.getProductCategory(), returnProduct.getProductSize()) == "no") {
                                            layoutSize.setVisibility(View.GONE);
                                        } else {
                                            layoutSize.setVisibility(View.VISIBLE);
                                            productSize.setText(getProductSize(product.getProductCategory(), returnProduct.getProductSize()));
                                        }
                                        displayStoreName(returnProduct.getSellerId());
                                        txtReturnDate2.setText(returnProduct.getReturnDate());
                                        if (returnProduct.getReturnStatus().equals("return")) {
                                            return_layout1.setVisibility(View.VISIBLE);
                                            txtOrderDate.setText(DateAndTime.convertDateFormat(returnProduct.getOrderDate()));
                                            deliverDate2.setText(DateAndTime.convertDateFormat(returnProduct.getDeliveryDAte()));
                                            returnDate.setText(DateAndTime.convertDateFormat(returnProduct.getReturnDate()));
                                        } else if (returnProduct.getReturnStatus().equals("pickup")) {
                                            return_layout2.setVisibility(View.VISIBLE);
                                            txtOrderDate.setText(DateAndTime.convertDateFormat(returnProduct.getOrderDate()));
                                            txtDelivery3.setText(DateAndTime.convertDateFormat(returnProduct.getDeliveryDAte()));
                                            retrunDate2.setText(DateAndTime.convertDateFormat(returnProduct.getReturnDate()));
                                            pickupDate.setText(DateAndTime.convertDateFormat(returnProduct.getPickupDate()));

                                        } else if (returnProduct.getReturnStatus().equals("refund")) {
                                            return_layout3.setVisibility(View.VISIBLE);
                                            txtOrderDate.setText(DateAndTime.convertDateFormat(returnProduct.getOrderDate()));
                                            deliveryDate4.setText(DateAndTime.convertDateFormat(returnProduct.getDeliveryDAte()));
                                            returnDate4.setText(DateAndTime.convertDateFormat(returnProduct.getReturnDate()));
                                            pickupdate4.setText(DateAndTime.convertDateFormat(returnProduct.getPickupDate()));
                                            refund.setText(DateAndTime.convertDateFormat(returnProduct.getRefundDate()));
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    public void displayStoreName(String adminId) {
        DatabaseReference adminNode = FirebaseDatabase.getInstance().getReference("AdminStoreInformation");
        Query sellerNodeQuery = adminNode.orderByChild("adminId").equalTo(adminId);
        sellerNodeQuery.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot sellerSnapshot = snapshot.getChildren().iterator().next();
                        String txtStoreName = sellerSnapshot.child("storeName").getValue(String.class);
                        storeName.setText(txtStoreName);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    public void updateTrackingDate(String orderDate, String orderID) {
        DateTimeFormatter formatter = null;
        LocalDate parsedDate = null;
        LocalDate shipingDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            parsedDate = LocalDate.parse(orderDate, formatter);
            shipingDate = parsedDate.plusDays(2);
            LocalDate deliveryDate = parsedDate.plusDays(4);
            String shipingDay = shipingDate.format(formatter);
            String deliveryDay = deliveryDate.format(formatter);
            DatabaseReference shipingRef = FirebaseDatabase.getInstance().getReference("Order").child(orderID).child("shipingDate");
            DatabaseReference deliveryRef = FirebaseDatabase.getInstance().getReference("Order").child(orderID).child("delliveryDate");
            shipingRef.setValue(shipingDay);
            deliveryRef.setValue(deliveryDay);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),complete_order.class));
        finish();
    }
}