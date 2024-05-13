package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapteranddatamodel.DateAndTime;
import com.example.uptrend.Adapter.CartAdapter;
import com.example.uptrend.Adapter.Onclick;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import DataModel.Cart;
import DataModel.Order;
import DataModel.Product;
import DataModel.UserAddress;

public class add_to_cart_product extends AppCompatActivity implements Onclick, PaymentResultListener {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    int totalOriginalPrice = 0, totalDiscountPrice = 0, totalPrice = 0;
    LinearLayout layoutPriceDetails;


    TextView close_btn, txtName, txtAddress, txtMobileNo, txtTotalQty, txtTotalOriginalPrice, txtTotalDiscountPrice, totalOrderPrice, txtPrice;

    Button change_btn_A2C;
    private CardView cardViewAddress;
    private UserAddress userAddress;
    private DatabaseReference userAddressRef, cartRef, productRef;
    private FirebaseUser firebaseUser;
    private Query addressQuery, cartQuery;
    private String address;
    private Cart cart;
    private Product product;
    private ArrayList<Cart> cartArrayList;
    private ArrayList<Product> productArrayList;
    AppCompatButton btnBuyProductCart;
    private String userName, userEmail, userMobileNo;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart_product);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        change_btn_A2C = findViewById(R.id.change_btn_A2C);
        close_btn = findViewById(R.id.close_btn_add_to_cart);
        cardViewAddress = findViewById(R.id.cardViewAddress);
        txtName = findViewById(R.id.name_A2C);
        txtAddress = findViewById(R.id.address_A2C);
        txtMobileNo = findViewById(R.id.mobile_no_A2C);
        recyclerViewCart = findViewById(R.id.recycleView_product_A2C);
        txtTotalQty = findViewById(R.id.txtTotal);
        totalOrderPrice = findViewById(R.id.total_order_A2C);
        txtTotalDiscountPrice = findViewById(R.id.discount_price_A2C);
        txtTotalOriginalPrice = findViewById(R.id.total_price_A2C);
        txtPrice = findViewById(R.id.txtPrice);
        btnBuyProductCart = findViewById(R.id.btn_buy_add_to_cart);
        layoutPriceDetails = findViewById(R.id.layoutPriceDetails);

        bottomNavigationView.setSelectedItemId(R.id.bottom_add_to_cart);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), home.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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
                    return true;
            }
            return false;
        });

        change_btn_A2C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), address_A2C.class);
                i.putExtra("status", "update");
                startActivity(i);
                finish();
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home.class));
                finish();
            }
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        /*
            This Method Will hide or show the Address layout
            if the user set the address it will visible
            otherwise it will not show.
         */
        hideShowAddressLayout();

        showCartProduct();
        calculation();
        // displayProductPricing();

        btnBuyProductCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAddress(firebaseUser.getUid());
            }
        });


    }

    public void checkAddress(String userId) {
        DatabaseReference addressReference = FirebaseDatabase.getInstance().getReference("UserAddress");
        Query addressQuery = addressReference.orderByChild("userId").equalTo(userId);
        addressQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(add_to_cart_product.this, "You Can Buy Product", Toast.LENGTH_SHORT).show();
                    Log.d("address", "Address exists");
                    makePayment(Double.parseDouble(txtPrice.getText().toString()), userEmail, userMobileNo);
                } else {
                    Intent i = new Intent(add_to_cart_product.this, address_A2C.class);
                    i.putExtra("status", "firsttime");
                    i.putExtra("activityName", "cart");
                    startActivity(i);
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void calculation() {
        cartArrayList = new ArrayList<>();
        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        cartQuery = cartRef.orderByChild("userId").equalTo(firebaseUser.getUid());
        cartQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartArrayList.clear();
                totalOriginalPrice = 0; // Reset totalOriginalPrice before recalculating
                totalDiscountPrice = 0;
                totalPrice = 0;
                txtTotalQty.setText(String.valueOf(snapshot.getChildrenCount()));
                for (DataSnapshot cartSnapShot : snapshot.getChildren()) {
                    cart = cartSnapShot.getValue(Cart.class);
                    cart.setCartId(cartSnapShot.getKey());
                    cartArrayList.add(cart);
                }

                // Perform calculations outside the loop
                for (Cart cartItem : cartArrayList) {
                    if (cartItem.getProductSize() != null) {
                        int productSize = Integer.parseInt(cartItem.getProductSize());
                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(cartItem.getProductId());
                        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    product = snapshot.getValue(Product.class);
                                    if (Integer.parseInt(product.getProductSizes().get(productSize)) == 0) {
                                        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(cartItem.getCartId());
                                        cartRef.removeValue();
                                    } else {
                                        int qty = Integer.parseInt(cartItem.getQty());
                                        int originalPrice = Integer.parseInt(product.getOriginalPrice());
                                        int sellingPrice = Integer.parseInt(product.getSellingPrice());
                                        int discountPrice = (originalPrice - sellingPrice) * qty;
                                        totalOriginalPrice += (qty * originalPrice); // Update totalOriginalPrice
                                        totalDiscountPrice += discountPrice;
                                        totalPrice += (qty * sellingPrice);

                                        // Update the UI only once after all calculations are completed
                                        if (cartArrayList.indexOf(cartItem) == cartArrayList.size() - 1) {
                                            updateUI();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(cartItem.getProductId());
                        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    product = snapshot.getValue(Product.class);
                                    if (Integer.parseInt(product.getTotalStock()) == 0) {
                                        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(cartItem.getCartId());
                                        cartRef.removeValue();
                                    } else {
                                        int qty = Integer.parseInt(cartItem.getQty());
                                        int originalPrice = Integer.parseInt(product.getOriginalPrice());
                                        int sellingPrice = Integer.parseInt(product.getSellingPrice());
                                        int discountPrice = (originalPrice - sellingPrice) * qty;
                                        totalOriginalPrice += (qty * originalPrice); // Update totalOriginalPrice
                                        totalDiscountPrice += discountPrice;
                                        totalPrice += (qty * sellingPrice);

                                        // Update the UI only once after all calculations are completed
                                        if (cartArrayList.indexOf(cartItem) == cartArrayList.size() - 1) {
                                            updateUI();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        // DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(cartItem.getProductId());
//                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()) {
//                                product = snapshot.getValue(Product.class);
//                                int qty = Integer.parseInt(cartItem.getQty());
//                                int originalPrice = Integer.parseInt(product.getOriginalPrice());
//                                int sellingPrice = Integer.parseInt(product.getSellingPrice());
//                                int discountPrice = (originalPrice - sellingPrice) * qty;
//                                totalOriginalPrice += (qty * originalPrice); // Update totalOriginalPrice
//                                totalDiscountPrice += discountPrice;
//                                totalPrice += (qty * sellingPrice);
//
//                                // Update the UI only once after all calculations are completed
//                                if (cartArrayList.indexOf(cartItem) == cartArrayList.size() - 1) {
//                                    updateUI();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            // Handle onCancelled
//                        }
//                    });
    }

    private void updateUI() {
        txtTotalOriginalPrice.setText(String.valueOf(totalOriginalPrice));
        txtTotalDiscountPrice.setText(String.valueOf(totalDiscountPrice));
        totalOrderPrice.setText(String.valueOf(totalPrice));
        txtPrice.setText(String.valueOf(totalPrice));
    }


    //        public void calculation() {
//        cartArrayList = new ArrayList<>();
//        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
//        cartQuery = cartRef.orderByChild("userId").equalTo(firebaseUser.getUid());
//        cartQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                cartArrayList.clear();
//                totalOriginalPrice = 0; // Reset totalOriginalPrice before recalculating
//                totalDiscountPrice = 0;
//                totalPrice = 0;
//                txtTotalQty.setText(String.valueOf(snapshot.getChildrenCount()));
//                for (DataSnapshot cartSnapShot : snapshot.getChildren()) {
//                    cart = cartSnapShot.getValue(Cart.class);
//                    cart.setCartId(cartSnapShot.getKey());
//                    cartArrayList.add(cart);
//                    DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(cart.getProductId());
//                    productRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()) {
//                                product = snapshot.getValue(Product.class);
//                                int qty = Integer.parseInt(cart.getQty());
//                                int originalPrice = Integer.parseInt(product.getOriginalPrice());
//                                int sellingPrice = Integer.parseInt(product.getSellingPrice());
//                                int DiscountPrice = (originalPrice - sellingPrice) * qty;
//                                totalOriginalPrice += (qty * originalPrice); // Update totalOriginalPrice
//                                totalDiscountPrice += DiscountPrice;
//                                totalPrice += (qty * sellingPrice);
//                                txtTotalOriginalPrice.setText(String.valueOf(totalOriginalPrice));
//                                txtTotalDiscountPrice.setText(String.valueOf(DiscountPrice));
//                                totalOrderPrice.setText(String.valueOf(totalPrice));
//                                txtPrice.setText(String.valueOf(totalPrice));
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//                                // Update the RecyclerView with the new cart data
////                LinearLayoutManager layoutManager = new LinearLayoutManager(add_to_cart_product.this, LinearLayoutManager.VERTICAL, false);
////                cartAdapter = new CartAdapter(getApplicationContext(), cartArrayList);
////                recyclerViewCart.setLayoutManager(layoutManager);
////                recyclerViewCart.setAdapter(cartAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    public void showCartProduct() {
        cartArrayList = new ArrayList<>();
        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        cartQuery = cartRef.orderByChild("userId").equalTo(firebaseUser.getUid());
        cartQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartArrayList.clear();
                for (DataSnapshot cartSnapShot : snapshot.getChildren()) {
                    cart = cartSnapShot.getValue(Cart.class);
                    cart.setCartId(cartSnapShot.getKey());
                    cartArrayList.add(cart);
                }
                if (cartArrayList.size() == 0) {
                    layoutPriceDetails.setVisibility(View.GONE);
                    btnBuyProductCart.setEnabled(false);
                    txtPrice.setText("0");
                } else {
                    layoutPriceDetails.setVisibility(View.VISIBLE);
                    btnBuyProductCart.setEnabled(true);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(add_to_cart_product.this, LinearLayoutManager.VERTICAL, false);
                cartAdapter = new CartAdapter(getApplicationContext(), cartArrayList, add_to_cart_product.this);
                recyclerViewCart.setLayoutManager(layoutManager);
                recyclerViewCart.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fetchUser();

    }

    public void makePayment(double price, String email, String mobileNumber) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_Y7YnikR706OvX9");
        //Set your logo here
        checkout.setImage(R.drawable.fire);
        //Reference to current activity
        final Activity activity = this;
        //Pass your payment options to the Razorpay Checkout as a JSONObject

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Up Trend");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", price * 100);//pass amount in currency subunits  500*100
            options.put("prefill.email", email);
            options.put("prefill.contact", "+91 " + mobileNumber);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("testcase 1", "Error in starting Razorpay Checkout", e);
        }
    }

    /*
          This Method Will hide or show the Address layout
          if the user set the address it will visible
          otherwise it will not show.
       */
    public void hideShowAddressLayout() {
        userAddressRef = FirebaseDatabase.getInstance().getReference("UserAddress");
        addressQuery = userAddressRef.orderByChild("userId").equalTo(firebaseUser.getUid());
        addressQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cardViewAddress.setVisibility(View.VISIBLE);
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    userAddress = userSnapshot.getValue(UserAddress.class);
                    txtName.setText(userAddress.getFullName());
                    address = userAddress.getHouseNo() + " , " + userAddress.getRoadName() + " , " + userAddress.getCity() + "  " + userAddress.getPincode();
                    txtAddress.setText(address);
                    txtMobileNo.setText(userAddress.getMobileNo());

                } else {
                    cardViewAddress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void ItemOnClickListener(String productId) {
        Intent i = new Intent(add_to_cart_product.this, open_product.class);
        i.putExtra("productId", productId);
        startActivity(i);
        finish();

    }

    @Override
    public void onPaymentSuccess(String s) {

        NotificationHelper.makeNotification(add_to_cart_product.this, userName);
        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        Query query = cartRef.orderByChild("userId").equalTo(firebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    cart = dataSnapshot.getValue(Cart.class);
                    reduceProductStock(cart);
                    Order order = new Order();
                    order.setOrderDate(DateAndTime.getDate());
                    order.setOrderTime(DateAndTime.getTime());
                    order.setOrderStatus("new");
                    order.setProductId(cart.getProductId());
                    order.setProductQty(cart.getQty());
                    order.setProductSize(cart.getProductSize());
                    order.setSellerId(cart.getAdminId());
                    order.setUserId(firebaseUser.getUid());
                    order.setDelliveryDate("");
                    order.setShipingDate("");
                    order.setProductSellingPrice(cart.getSellingPrice());
                    order.setProductOriginalPrice(cart.getOriginalPrice());
                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order");
                    orderRef.push().setValue(order);
                    DatabaseReference delete = dataSnapshot.getRef();
                    delete.removeValue();
                }
                query.removeEventListener(this);
                txtPrice.setText("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        startActivity(new Intent(add_to_cart_product.this, account_user.class));
        finish();
    }
    public void reduceProductStock(Cart cart){
        if(cart.getProductSize()!=null){
            DatabaseReference qtyRef=FirebaseDatabase.getInstance().getReference("Product").child(cart.getProductId()).child("productSizes");
            DatabaseReference totalStockQtyRef=FirebaseDatabase.getInstance().getReference("Product").child(cart.getProductId()).child("totalStock");
            productRef=FirebaseDatabase.getInstance().getReference("Product").child(cart.getProductId());
            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    product=snapshot.getValue(Product.class);
                    int sizeQty=Integer.parseInt(product.getProductSizes().get(Integer.parseInt(cart.getProductSize())));
                    int totalStock=Integer.parseInt(product.getTotalStock());
                    sizeQty=sizeQty-Integer.parseInt(cart.getQty());
                    totalStock=totalStock-Integer.parseInt(cart.getQty());
                    product.getProductSizes().remove(Integer.parseInt(cart.getProductSize()));
                    product.getProductSizes().add(Integer.parseInt(cart.getProductSize()),String.valueOf(sizeQty));
                    qtyRef.setValue(product.getProductSizes());
                    totalStockQtyRef.setValue(String.valueOf(totalStock));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            productRef=FirebaseDatabase.getInstance().getReference("Product").child(cart.getProductId());
            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        product=snapshot.getValue(Product.class);
                        DatabaseReference totalStockQtyRef=FirebaseDatabase.getInstance().getReference("Product").child(cart.getProductId()).child("totalStock");
                        int totalStock=Integer.parseInt(product.getTotalStock());
                        totalStock=totalStock-Integer.parseInt(cart.getQty());
                        totalStockQtyRef.setValue(String.valueOf(totalStock));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onPaymentError(int i, String s) {

    }

    public void fetchUser() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
        Query userQuery = userRef.orderByChild("userId").equalTo(firebaseUser.getUid());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                userName = userSnapshot.child("userName").getValue(String.class);
                userEmail = userSnapshot.child("userEmail").getValue(String.class);
                userMobileNo = userSnapshot.child("userMobileNumber").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String estimatedDeliveryDate() {
        String formattedEstimatedDeliveryDate = "";
        LocalDate todayDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            todayDate = LocalDate.now();
            LocalDate estimatedDeliveryDate = todayDate.plusDays(5);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, EEEE");
            formattedEstimatedDeliveryDate = estimatedDeliveryDate.format(formatter);

        }
        return formattedEstimatedDeliveryDate;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), home.class));
        finish();
    }
}