package com.example.uptrend;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.adapteranddatamodel.DateAndTime;
import com.example.uptrend.Adapter.Onclick;
import com.example.uptrend.Adapter.SuggestionProductAdapter;
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

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import DataModel.Cart;
import DataModel.LikeProduct;
import DataModel.Order;
import DataModel.Product;
import DataModel.Review;
import DataModel.User;
import io.github.muddz.styleabletoast.StyleableToast;

public class open_product extends AppCompatActivity implements PaymentResultListener, Onclick {
    CheckBox likeCheckBox;
    int totalReview=0;
    float rating,total;
    Order order;

    private LinearLayout linearLayout1, linearLayout2, linearLayout3, size_shirt_layout, size_shoes_layout,
            size_jeans_layout, layout_fabric, layout_washing, layout_weight, layout_occasion, layout_all_details, layoutOtherCategory,
            mobileLayoutVisible,mobileLayoutGone;

    private RatingBar ratingBar;


    private Animation right_side, right_side1, right_side2;
    private String productId;
    private TextView txtBrandName, txtProductName, txtSellingPrice, txtOriginalPrice, txtDiscount, txtRating, txtRatingCount,
            size_S1, size_S2, size_S3, size_S4, size_S5, size_SH1, size_SH2, size_SH3, size_SH4, size_SH5, date_txt, color_txt, color_name_txt, all_detils_txt,
            size_J1, size_J2, size_J3, size_J4, size_J5, size_chart_txt, few_left_shirt, few_left_shoes,
            few_left_jeans, fabric_name_txt, quantity_txt, washing_txt, weight_txt, occasion_txt, mrp_txt,
            market_txt, quntity2_txt, import_txt, mobile_txt, email_txt, share_btn, rating_txt, ratingBar_TOTAL, back_btn, selection_txt, txtOtherCategory,txt_rating,txt_rating_count,
            ram_txt,rom_storage_txt,processor_txt,rear_camera_txt,front_camera_txt,battery_txt;
    private DatabaseReference productReference, userReference, productRootNodeReference, cartRef;
    private Query query, categoryQuery;

    private double discount;
    private Product product;
    private AppCompatButton btnBuyNow, btnAddToCart;
    private Cart cart;

    private User user;

    private String number;
    private FirebaseUser firebaseUser;
    ImageSlider imageSlider;
    ArrayList<SlideModel> slideModels;
    CardView sizeCardView;
    RecyclerView recyclerViewSuggestedProduct;
    SuggestionProductAdapter suggestionProductAdapter;
    ArrayList<Product> suggestedProducts;
    RadioGroup radioGroupShoes, radioGroupShirt, radioGroupJeans;
    ScrollView scrollView;
    TextView txtSizeChart01, txtSizeChart02, txtSizeChart03;
    private DatabaseReference wishListRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_product);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        likeCheckBox = findViewById(R.id.likeCheckBox);


        linearLayout1 = findViewById(R.id.linear1);
        linearLayout2 = findViewById(R.id.linear2);
        linearLayout3 = findViewById(R.id.linear3);
        txtBrandName = findViewById(R.id.txt_brand_name);
        txtProductName = findViewById(R.id.txt_product_name);
        txtRating = findViewById(R.id.txt_rating);
        txtRatingCount = findViewById(R.id.txt_rating_count);
        txtSellingPrice = findViewById(R.id.txt_product_selling_price);
        txtOriginalPrice = findViewById(R.id.text_product_original_price);
        txtDiscount = findViewById(R.id.txtDiscount);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        imageSlider = findViewById(R.id.imageSlider);
        txtSizeChart01 = findViewById(R.id.txtSizeChart01);


        //**********
        txtSizeChart02 = findViewById(R.id.txtSizeChart02);
        txtSizeChart03 = findViewById(R.id.txtSizeChart03);
        date_txt = findViewById(R.id.date_txt);
        color_name_txt = findViewById(R.id.get_PD_color);
        color_txt = findViewById(R.id.color_name_txt);


        //for shirt size layout
        size_shirt_layout = findViewById(R.id.layout_shirt_size);

        size_S1 = findViewById(R.id.shirt_size1);
        size_S2 = findViewById(R.id.shirt_size2);
        size_S3 = findViewById(R.id.shirt_size3);
        size_S4 = findViewById(R.id.shirt_size4);
        size_S5 = findViewById(R.id.shirt_size5);
        few_left_shirt = findViewById(R.id.few_left_txt_shirt);


        //for shoes size layout
        size_shoes_layout = findViewById(R.id.layout_shoes_size);

        size_SH1 = findViewById(R.id.shoes_size1);
        size_SH2 = findViewById(R.id.shoes_size2);
        size_SH3 = findViewById(R.id.shoes_size3);
        size_SH4 = findViewById(R.id.shoes_size4);
        size_SH5 = findViewById(R.id.shoes_size5);
        few_left_shoes = findViewById(R.id.few_left_txt_shoes);


        //for jeans size layout
        size_jeans_layout = findViewById(R.id.layout_jeans_size);

        size_J1 = findViewById(R.id.jeans_size1);
        size_J2 = findViewById(R.id.jeans_size2);
        size_J3 = findViewById(R.id.jeans_size3);
        size_J4 = findViewById(R.id.jeans_size4);
        size_J5 = findViewById(R.id.jeans_size5);
        few_left_jeans = findViewById(R.id.jeans_left_txt_shirt);

        //CardView For Size Layout
        sizeCardView = findViewById(R.id.sizeCardView);

        //for PD details
        all_detils_txt = findViewById(R.id.all_details_txt);
        fabric_name_txt = findViewById(R.id.fabric_name_txt);
        quantity_txt = findViewById(R.id.quantity_txt);
        washing_txt = findViewById(R.id.washing_txt);
        weight_txt = findViewById(R.id.weight_txt);
        occasion_txt = findViewById(R.id.occasion_txt);
        mrp_txt = findViewById(R.id.mrp_details_txt);
        market_txt = findViewById(R.id.market_details);
        quntity2_txt = findViewById(R.id.quntity2_details);
        import_txt = findViewById(R.id.import_details);
        mobile_txt = findViewById(R.id.mobile_txt);
        email_txt = findViewById(R.id.email_txt);
        share_btn = findViewById(R.id.share_btn);
        back_btn = findViewById(R.id.back_btn);
        selection_txt = findViewById(R.id.selection_txt);
        btnAddToCart = findViewById(R.id.btnAddToCart);


        //RATING BAR
        rating_txt = findViewById(R.id.rating_txt);
        ratingBar = findViewById(R.id.rating_Bar);
        ratingBar_TOTAL = findViewById(R.id.ratingBar_total_count);
        txt_rating=findViewById(R.id.txt_rating);
        txt_rating_count=findViewById(R.id.txt_rating_count);


        //
        layout_fabric = findViewById(R.id.fabric_layout);
        layout_washing = findViewById(R.id.layout_washing);
        layout_weight = findViewById(R.id.layout_weight);
        layout_occasion = findViewById(R.id.layout_occasion);
        layout_all_details = findViewById(R.id.layout_all_details_HS);


        //Radio Group FindViewBy Id
        radioGroupJeans = findViewById(R.id.radioGroupJeans);
        radioGroupShirt = findViewById(R.id.radioGroupShirt);
        radioGroupShoes = findViewById(R.id.radioGroupShoes);

        //Layout For Other Category Stock Error
        layoutOtherCategory = findViewById(R.id.layoutOtherCategory);
        txtOtherCategory = findViewById(R.id.product_not_available);

        scrollView = findViewById(R.id.scrollView);


        right_side = AnimationUtils.loadAnimation(this, R.anim.right_side);
        linearLayout1.setAnimation(right_side);

        right_side1 = AnimationUtils.loadAnimation(this, R.anim.right_side1);
        linearLayout2.setAnimation(right_side1);

        right_side2 = AnimationUtils.loadAnimation(this, R.anim.right_side2);
        linearLayout3.setAnimation(right_side2);
        mobileLayoutGone=findViewById(R.id.mobileLayoutGone);
        mobileLayoutVisible=findViewById(R.id.mobileLayoutVisible);

        //Mobile

        ram_txt=findViewById(R.id.ram_txt);
        rom_storage_txt=findViewById(R.id.rom_storage_txt);
        processor_txt=findViewById(R.id.processor_txt);
        rear_camera_txt=findViewById(R.id.rear_camera_txt);
        front_camera_txt=findViewById(R.id.front_camera_txt);
        battery_txt=findViewById(R.id.battery_txt);

        recyclerViewSuggestedProduct = findViewById(R.id.recyclerViewSuggestedProduct);
        /* geting product id from home activity so we can fetch product data
            and Displaying Product Details .
         */
        productId = getIntent().getStringExtra("productId");
        changeProduct(productId);


        //************* apply for manifests file to click to open our app
        Uri uri = getIntent().getData();
        if (uri != null) {
            String path = uri.toString();

        }

        // giving intent to Mobile no so Dialed pad can open
        mobile_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = mobile_txt.getText().toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)));
                startActivity(intent);
            }
        });

        all_detils_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (layout_all_details.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(layout_all_details, new AutoTransition());
                layout_all_details.setVisibility(var);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home.class));
                finish();
                addProductInWishList();
            }
        });


        // share our app link
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                intent.putExtra(Intent.EXTRA_TEXT, "Welcome to INDIA's Top Branded Company Up Trend.\n\n https://www.uptrend.google.com/maps?saddr=33.77.73.22" + getPackageName());
                intent.setType("text/plain");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        email_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://mail.google.com/chat/u/0/#chat/home");
            }
        });



        /*
                Geting user email and User mobile number
         */
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("User");
        query = userReference.orderByChild("userId").equalTo(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                user = userSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        /*
                Integration of Payment Gateway
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(open_product.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(open_product.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        Checkout.preload(getApplicationContext());
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getProductSizes() != null) {
                    if (isRadioButtonChecked()) {
                        if (firebaseUser != null) {
                            checkAddress(firebaseUser.getUid());
                        } else {
                            makePayment(Double.parseDouble(product.getSellingPrice()), user.getUserEmail(), user.getUserMobileNumber());
                        }
                    } else {
                        Toast.makeText(open_product.this, "Please Select Product Size", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    makePayment(Double.parseDouble(product.getSellingPrice()), user.getUserEmail(), user.getUserMobileNumber());
                }
            }
        });

        cart = new Cart();
        order = new Order();

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartRef = FirebaseDatabase.getInstance().getReference("Cart");
                Query productQuery = cartRef.orderByChild("userId").equalTo(firebaseUser.getUid());
//                productQuery.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        boolean productAlreadyInCart=false;
//                        for(DataSnapshot cartSnapShot:snapshot.getChildren()){
//                            cart=cartSnapShot.getValue(Cart.class);
//                                if(cart.getProductId().equals(productId)){
//                                    productAlreadyInCart=true;
//                                    break;
//                                }
//                            }
//
//
//                        if(productAlreadyInCart){
//                            Toast.makeText(open_product.this, "Product Is Already In Cart", Toast.LENGTH_SHORT).show();
//                        }else{

                if(product.getProductSizes()!=null){
                    if(isRadioButtonChecked()){
                        cart.setAdminId(product.getAdminId());
                        cart.setProductId(productId);
                        cart.setUserId(firebaseUser.getUid());
                        cart.setQty("1");
                        cartRef = FirebaseDatabase.getInstance().getReference("Cart");
                        cart.setOriginalPrice(product.getOriginalPrice());
                        cart.setSellingPrice(product.getSellingPrice());
                        cartRef.push().setValue(cart);
                        StyleableToast.makeText(open_product.this,"Product Added In The Cart",R.style.UptrendToast).show();
                    }else {
                        StyleableToast.makeText(open_product.this,"Please Select Product Size",R.style.UptrendToast).show();
                    }

                }else {
                    cart.setAdminId(product.getAdminId());
                    cart.setProductId(productId);
                    cart.setUserId(firebaseUser.getUid());
                    cart.setQty("1");
                    cartRef = FirebaseDatabase.getInstance().getReference("Cart");
                    cartRef.push().setValue(cart);
                    StyleableToast.makeText(open_product.this,"Product Added In The Cart",R.style.UptrendToast).show();

                }



            }
        });


        /*
                Checking Stock when user Select the Size of Product.
         */
        radioGroupShirt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonS) {
                    checkProductStock(few_left_shirt, Integer.parseInt(product.getProductSizes().get(0)));
                    cart.setProductSize(String.valueOf(0));
                    order.setProductSize(String.valueOf(0));
                } else if (i == R.id.radioButtonM) {
                    checkProductStock(few_left_shirt, Integer.parseInt(product.getProductSizes().get(1)));
                    cart.setProductSize(String.valueOf(1));
                    order.setProductSize(String.valueOf(1));
                } else if (i == R.id.radioButtonL) {
                    checkProductStock(few_left_shirt, Integer.parseInt(product.getProductSizes().get(2)));
                    cart.setProductSize(String.valueOf(2));
                    order.setProductSize(String.valueOf(2));
                } else if (i == R.id.radioButtonXL) {
                    checkProductStock(few_left_shirt, Integer.parseInt(product.getProductSizes().get(3)));
                    cart.setProductSize(String.valueOf(3));
                    order.setProductSize(String.valueOf(3));
                } else if (i == R.id.radioButtonXXl) {
                    checkProductStock(few_left_shirt, Integer.parseInt(product.getProductSizes().get(4)));
                    cart.setProductSize(String.valueOf(4));
                    order.setProductSize(String.valueOf(4));
                }
            }
        });
        radioGroupJeans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton28) {
                    checkProductStock(few_left_jeans, Integer.parseInt(product.getProductSizes().get(0)));
                    cart.setProductSize(String.valueOf(0));
                    order.setProductSize(String.valueOf(0));
                } else if (i == R.id.radioButton30) {
                    checkProductStock(few_left_jeans, Integer.parseInt(product.getProductSizes().get(1)));
                    cart.setProductSize(String.valueOf(1));
                    order.setProductSize(String.valueOf(1));

                } else if (i == R.id.radioButton32) {
                    checkProductStock(few_left_jeans, Integer.parseInt(product.getProductSizes().get(2)));
                    cart.setProductSize(String.valueOf(2));
                    order.setProductSize(String.valueOf(2));

                } else if (i == R.id.radioButton34) {
                    checkProductStock(few_left_jeans, Integer.parseInt(product.getProductSizes().get(3)));
                    cart.setProductSize(String.valueOf(3));
                    order.setProductSize(String.valueOf(3));

                } else if (i == R.id.radioButton36) {
                    checkProductStock(few_left_jeans, Integer.parseInt(product.getProductSizes().get(4)));
                    cart.setProductSize(String.valueOf(4));
                    order.setProductSize(String.valueOf(4));
                } else if (i == R.id.radioButton38) {
                    checkProductStock(few_left_jeans, Integer.parseInt(product.getProductSizes().get(5)));
                    cart.setProductSize(String.valueOf(5));
                    order.setProductSize(String.valueOf(5));
                } else if (i == R.id.radioButton40) {
                    checkProductStock(few_left_jeans, Integer.parseInt(product.getProductSizes().get(6)));
                    cart.setProductSize(String.valueOf(6));
                    order.setProductSize(String.valueOf(6));
                }
            }
        });
        radioGroupShoes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton6) {
                    checkProductStock(few_left_shoes, Integer.parseInt(product.getProductSizes().get(0)));
                    cart.setProductSize(String.valueOf(0));
                    order.setProductSize(String.valueOf(0));
                } else if (i == R.id.radioButton7) {
                    checkProductStock(few_left_shoes, Integer.parseInt(product.getProductSizes().get(1)));
                    cart.setProductSize(String.valueOf(1));
                    order.setProductSize(String.valueOf(1));

                } else if (i == R.id.radioButton8) {
                    checkProductStock(few_left_shoes, Integer.parseInt(product.getProductSizes().get(2)));
                    cart.setProductSize(String.valueOf(2));
                    order.setProductSize(String.valueOf(2));
                } else if (i == R.id.radioButton9) {
                    checkProductStock(few_left_shoes, Integer.parseInt(product.getProductSizes().get(3)));
                    cart.setProductSize(String.valueOf(3));
                    order.setProductSize(String.valueOf(3));
                } else if (i == R.id.radioButton10) {
                    checkProductStock(few_left_shoes, Integer.parseInt(product.getProductSizes().get(4)));
                    cart.setProductSize(String.valueOf(4));
                    order.setProductSize(String.valueOf(4));
                }
            }
        });


        /*
                Displaying CheckBox Checked When Product is Available in
                WishList Node.
         */
        wishListRef = FirebaseDatabase.getInstance().getReference("WishListProduct");
        checkProductInWishList(productId, wishListRef, firebaseUser, new com.example.uptrend.Adapter.ValueEventListener() {
            @Override
            public void result(boolean alreadyProduct) {
                if (alreadyProduct) {
                    likeCheckBox.setChecked(true);
                }
            }
        });

        displayRating(productId);

    }

    public void checkProductInWishList(String productId, DatabaseReference wishListRef, FirebaseUser firebaseUser, com.example.uptrend.Adapter.ValueEventListener listener) {
        Query query = wishListRef.orderByChild("userId").equalTo(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean alreadyProduct = false;
                for (DataSnapshot productSnapShot : snapshot.getChildren()) {
                    LikeProduct likeProduct = productSnapShot.getValue(LikeProduct.class);
                    if (likeProduct != null && likeProduct.getProductId().equals(productId)) {
                        alreadyProduct = true;
                        break;
                    }
                }
                listener.result(alreadyProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void addProductInWishList() {
        if (likeCheckBox.isChecked()) {
            wishListRef = FirebaseDatabase.getInstance().getReference("WishListProduct");
            checkProductInWishList(productId, wishListRef, firebaseUser, new com.example.uptrend.Adapter.ValueEventListener() {
                @Override
                public void result(boolean alreadyProduct) {
                    if (alreadyProduct) {

                    } else {
                        LikeProduct likeProduct = new LikeProduct();
                        likeProduct.setUserId(firebaseUser.getUid());
                        likeProduct.setProductId(productId);
                        likeProduct.setQty("1");
                        likeProduct.setAdminId(product.getAdminId());
                        likeProduct.setProductOriginalPrice(product.getOriginalPrice());
                        likeProduct.setProductSellingPrice(product.getSellingPrice());
                        if (product.getProductSizes()!=null) {
                            likeProduct.setProductSize("0");
                            wishListRef.push().setValue(likeProduct);
                        }else{
                            wishListRef.push().setValue(likeProduct);
                        }

                    }
                }
            });
        } else {
            wishListRef = FirebaseDatabase.getInstance().getReference("WishListProduct");
            Query query = wishListRef.orderByChild("userId").equalTo(firebaseUser.getUid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        LikeProduct likeProduct = dataSnapshot.getValue(LikeProduct.class);
                        if (likeProduct.getProductId().equals(productId)) {
                            DatabaseReference deleteRef = dataSnapshot.getRef();
                            deleteRef.removeValue();
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
    public void onBackPressed() {
        super.onBackPressed();
        addProductInWishList();
    }

    public boolean isRadioButtonChecked() {
        boolean radioButtonChecked = false;
        if (radioGroupJeans.getCheckedRadioButtonId() != -1) {
            radioButtonChecked = true;
        } else if (radioGroupShirt.getCheckedRadioButtonId() != -1) {
            radioButtonChecked = true;
        } else if (radioGroupShoes.getCheckedRadioButtonId() != -1) {
            radioButtonChecked = true;

        }
        return radioButtonChecked;
    }

    public void checkAddress(String userId) {
        DatabaseReference addressReference = FirebaseDatabase.getInstance().getReference("UserAddress");
        Query addressQuery = addressReference.orderByChild("userId").equalTo(userId);
        addressQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    makePayment(Double.parseDouble(product.getSellingPrice()), user.getUserEmail(), user.getUserMobileNumber());
                    Log.d("address", "Address exists");
                } else {
                    Intent i = new Intent(open_product.this, address_A2C.class);
                    i.putExtra("status", "firsttime");
                    i.putExtra("activityName", "openProduct");
                    i.putExtra("productId", productId);
                    startActivity(i);
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /*
            This Method Will Change Product Details According to
            Product Id. And Display It Details.
     */

    public void changeProduct(String productId) {
        slideModels = new ArrayList<>();
        productReference = FirebaseDatabase.getInstance().getReference("Product").child(productId);
        productReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    product = snapshot.getValue(Product.class);
                    txtBrandName.setText(product.getProductBrandName());
                    txtProductName.setText(product.getProductName());
                    txtSellingPrice.setText(product.getSellingPrice());
                    txtOriginalPrice.setText(product.getOriginalPrice());
                    discount = calculateDiscountPercentage(Double.parseDouble(product.getOriginalPrice()),
                            Double.parseDouble(product.getSellingPrice()));
                    DecimalFormat df = new DecimalFormat("#.##");
                    String formattedDiscountPercentage = df.format(discount);
                    txtDiscount.setText(formattedDiscountPercentage);
                    for (int i = 0; i < product.getProductImages().size(); i++) {
                        slideModels.add(new SlideModel(product.getProductImages().get(i), ScaleTypes.FIT));
                    }
                    imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                    date_txt.setText(estimatedDeliveryDate());
                    color_name_txt.setText(product.getProductColour());
                    setTextViewBackgroundTint(color_txt, getColorResourceId(product.getProductColour()));
                    mrp_txt.setText(product.getSellingPrice());
                    market_txt.setText(product.getProductManufactureDetails());
                    import_txt.setText(product.getProductPackerDetail());
                    quntity2_txt.setText(product.getProductPacking());
                    weight_txt.setText(product.getProductWeight());
                    selection_txt.setText(product.getProductSuitFor());
                    if (product.getProductCategory().equals("Men's(Top)") || product.getProductCategory().equals("Women's(Top)")) {
                        sizeCardView.setVisibility(View.VISIBLE);
                        size_shirt_layout.setVisibility(View.VISIBLE);
                        layout_occasion.setVisibility(View.VISIBLE);
                        mobileLayoutGone.setVisibility(View.VISIBLE);
                        mobileLayoutVisible.setVisibility(View.GONE);
                        fabric_name_txt.setText(product.getProductFabric());
                        washing_txt.setText(product.getProductWashcare());
                        occasion_txt.setText(product.getProductOccasion());
                        few_left_shirt.setVisibility(View.GONE);
                        radioGroupShirt.clearCheck();
                        layoutOtherCategory.setVisibility(View.GONE);
                        txtSizeChart01.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), size_chart_shirts.class);
                                i.putExtra("chart", product.getProductCategory());
                                i.putExtra("productId",productId);
                                startActivity(i);
                                finish();
                            }
                        });

                    } else if (product.getProductCategory().equals("Men's(Bottom)") || product.getProductCategory().equals("Women's(Bottom)")) {
                        sizeCardView.setVisibility(View.VISIBLE);
                        size_jeans_layout.setVisibility(View.VISIBLE);
                        layout_occasion.setVisibility(View.VISIBLE);
                        mobileLayoutGone.setVisibility(View.VISIBLE);
                        mobileLayoutVisible.setVisibility(View.GONE);
                        fabric_name_txt.setText(product.getProductFabric());
                        washing_txt.setText(product.getProductWashcare());
                        occasion_txt.setText(product.getProductOccasion());
                        few_left_jeans.setVisibility(View.GONE);
                        radioGroupJeans.clearCheck();
                        layoutOtherCategory.setVisibility(View.GONE);
                        txtSizeChart03.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), size_chart_jeans.class);
                                i.putExtra("chart", product.getProductCategory());
                                i.putExtra("productId",productId);
                                startActivity(i);
                                finish();
                            }
                        });
                    } else if (product.getProductCategory().equals("Footware(Men)") || product.getProductCategory().equals("Footware(Women)")) {
                        sizeCardView.setVisibility(View.VISIBLE);
                        size_shoes_layout.setVisibility(View.VISIBLE);
                        layout_fabric.setVisibility(View.GONE);
                        layout_washing.setVisibility(View.GONE);
                        few_left_shoes.setVisibility(View.GONE);
                        mobileLayoutGone.setVisibility(View.VISIBLE);
                        mobileLayoutVisible.setVisibility(View.GONE);
                        radioGroupShoes.clearCheck();
                        layoutOtherCategory.setVisibility(View.GONE);
                        txtSizeChart02.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), size_chart_shoes.class);
                                i.putExtra("chart", product.getProductCategory());
                                i.putExtra("productId",productId);
                                startActivity(i);
                                finish();
                            }
                        });
                    } else if (product.getProductSubCategory().equals("Smartphones")) {
                        mobileLayoutGone.setVisibility(View.GONE);
                        layoutOtherCategory.setVisibility(View.GONE);
                        mobileLayoutVisible.setVisibility(View.VISIBLE);
                        ram_txt.setText(product.getRam());
                        rom_storage_txt.setText(product.getStorage());
                        processor_txt.setText(product.getProcessor());
                        rear_camera_txt.setText(product.getRearCamera());
                        front_camera_txt.setText(product.getFrontCamera());
                        battery_txt.setText(product.getBattery());
                    } else {
                        mobileLayoutGone.setVisibility(View.VISIBLE);
                        mobileLayoutVisible.setVisibility(View.GONE);
                        layout_fabric.setVisibility(View.GONE);
                        layout_washing.setVisibility(View.GONE);
                        layout_occasion.setVisibility(View.GONE);
                        if (Integer.parseInt(product.getTotalStock()) <= 3 && Integer.parseInt(product.getTotalStock()) >= 1) {
                            layoutOtherCategory.setVisibility(View.VISIBLE);
                            txtOtherCategory.setVisibility(View.VISIBLE);
                            txtOtherCategory.setText("Hurry, Only few items left");
                        } else if (Integer.parseInt(product.getTotalStock()) == 0) {
                            layoutOtherCategory.setVisibility(View.VISIBLE);
                            txtOtherCategory.setVisibility(View.VISIBLE);
                            txtOtherCategory.setText("Out Of Stock");
                        } else {
                            layoutOtherCategory.setVisibility(View.GONE);
                            txtOtherCategory.setVisibility(View.GONE);
                        }

                    }
                    suggestionProduct(product.getProductCategory());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
        suggestionProduct Method Will display Product According to its suggestions
     */

    public void suggestionProduct(String category) {
        suggestedProducts = new ArrayList<>();
        productRootNodeReference = FirebaseDatabase.getInstance().getReference("Product");
        categoryQuery = productRootNodeReference.orderByChild("productCategory").equalTo(category);
        categoryQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapShot : snapshot.getChildren()) {
                    Product product1 = productSnapShot.getValue(Product.class);
                    suggestedProducts.add(product1);
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(open_product.this, LinearLayoutManager.HORIZONTAL, false);
                suggestionProductAdapter = new SuggestionProductAdapter(open_product.this, suggestedProducts, open_product.this);
                recyclerViewSuggestedProduct.setLayoutManager(linearLayoutManager);
                recyclerViewSuggestedProduct.setAdapter(suggestionProductAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // This Method Will CheckProductStock and Display error massage
    public void checkProductStock(TextView txtErrorMassage, int stock) {
        if (stock <= 3 && stock >= 1) {
            txtErrorMassage.setVisibility(View.VISIBLE);
            txtErrorMassage.setText("Hurry, Only few items left");
            btnBuyNow.setEnabled(true);
            btnAddToCart.setEnabled(true);
        } else if (stock == 0) {
            txtErrorMassage.setVisibility(View.VISIBLE);
            txtErrorMassage.setText("Out OF Stock");
            btnBuyNow.setEnabled(false);
            btnAddToCart.setEnabled(false);
        } else {
            txtErrorMassage.setVisibility(View.GONE);
            btnBuyNow.setEnabled(true);
            btnAddToCart.setEnabled(true);
        }

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

    public double calculateDiscountPercentage(double originalPrice, double sellingPrice) {
        double discount = originalPrice - sellingPrice;
        return (discount / originalPrice) * 100;
    }


    @Override
    public void onPaymentSuccess(String s) {
        startActivity(new Intent(getApplicationContext(), account_user.class));
        finish();
        reduceProductStock();
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order");
        order.setUserId(firebaseUser.getUid());
        order.setProductId(productId);
        order.setProductQty("1");
        order.setOrderStatus("new");
        order.setDelliveryDate("");
        order.setShipingDate("");
        order.setSellerId(product.getAdminId());
        order.setOrderDate(DateAndTime.getDate());
        order.setOrderTime(DateAndTime.getTime());
        order.setProductSellingPrice(product.getSellingPrice());
        order.setProductOriginalPrice(product.getOriginalPrice());
        orderRef.push().setValue(order);
        NotificationHelper.makeNotification(open_product.this, user.getUserName());
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d("pay", "payment  is failed:   " + s);

    }
    public void reduceProductStock(){
        if(product.getProductSizes()!=null){
            DatabaseReference qtyRef=FirebaseDatabase.getInstance().getReference("Product").child(productId).child("productSizes");
            DatabaseReference totalStockQtyRef=FirebaseDatabase.getInstance().getReference("Product").child(productId).child("totalStock");
            int sizeQty=Integer.parseInt(product.getProductSizes().get(Integer.parseInt(order.getProductSize())));
            int totalStock=Integer.parseInt(product.getTotalStock());
            sizeQty=sizeQty-1;
            totalStock=totalStock-1;
            product.getProductSizes().remove(Integer.parseInt(order.getProductSize()));
            product.getProductSizes().add(Integer.parseInt(order.getProductSize()),String.valueOf(sizeQty));
            qtyRef.setValue(product.getProductSizes());
            totalStockQtyRef.setValue(String.valueOf(totalStock));
        }else{
            DatabaseReference totalStockQtyRef=FirebaseDatabase.getInstance().getReference("Product").child(productId).child("totalStock");
            int totalStock=Integer.parseInt(product.getTotalStock());
            totalStock=totalStock-1;
            totalStockQtyRef.setValue(String.valueOf(totalStock));
        }
    }

    public void displayRating(String productId){
        DatabaseReference reviewRef=FirebaseDatabase.getInstance().getReference("Review");
        Query productReviewQuery=reviewRef.orderByChild("productId").equalTo(productId);
        productReviewQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total=0;
                rating=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    totalReview= (int) snapshot.getChildrenCount();
                    Review review=dataSnapshot.getValue(Review.class);
                    rating= Float.parseFloat(review.getProductStar());
                    total+=rating;
                }
                if(totalReview==0){
                    rating_txt.setText("0.0");
                    ratingBar.setRating(total/totalReview);
                    ratingBar_TOTAL.setText(String.valueOf(totalReview));
                    txtRating.setText("0.0");
                    txtRatingCount.setText("0");
                }else{
                    rating_txt.setText(String.valueOf(total/totalReview));
                    ratingBar.setRating(total/totalReview);
                    ratingBar_TOTAL.setText(String.valueOf(totalReview));
                    txt_rating.setText(String.valueOf(total/totalReview));
                    txtRatingCount.setText(String.valueOf(totalReview));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void gotoUrl(String s) {
        try {
            Uri uri = Uri.parse(s);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "no website link", Toast.LENGTH_SHORT).show();
        }

    }

    public String estimatedDeliveryDate() {
        String formattedEstimatedDeliveryDate = "";
        LocalDate todayDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            todayDate = LocalDate.now();
            LocalDate estimatedDeliveryDate = todayDate.plusDays(4);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, EEEE");
            formattedEstimatedDeliveryDate = estimatedDeliveryDate.format(formatter);

        }
        return formattedEstimatedDeliveryDate;
    }

    //This Method Will Change The Textview BackgroundTint According which Colour is Selected.
    @SuppressLint("RestrictedApi")
    private void setTextViewBackgroundTint(TextView textView, int colorResId) {
        // Use AppCompatTextView if you're working with the AppCompat library
        if (textView instanceof AppCompatTextView) {
            ((AppCompatTextView) textView).setSupportBackgroundTintList(
                    ContextCompat.getColorStateList(this, colorResId)
            );
        } else {
            // For standard TextView
            textView.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, colorResId)
            );
        }
    }

    private int getColorResourceId(String colorName) {
        // Map color names to color resource IDs
        switch (colorName) {
            case "Aquamarine":
                return R.color.Aquamarine;
            case "Azure":
                return R.color.Azure;
            case "Black":
                return R.color.black;
            case "Brown":
                return R.color.Brown;
            case "Coral":
                return R.color.Coral;
            case "Crimson":
                return R.color.Crimson;
            case "Cyan":
                return R.color.Cyan;
            case "Golden":
                return R.color.Golden;
            case "Gray":
                return R.color.Gray;
            case "Green":
                return R.color.Green;
            case "Hot Pink":
                return R.color.Hot_Pink;
            case "Lime":
                return R.color.Lime;
            case "Magent":
                return R.color.Magent;
            case "Maroon":
                return R.color.Maroon;
            case "Navy Blue":
                return R.color.Navy_Blue;
            case "Olive":
                return R.color.Olive;
            case "Orange":
                return R.color.Orange;
            case "Purple":
                return R.color.Purple;
            case "Red":
                return R.color.red;
            case "Royal Blue":
                return R.color.Royal_Blue;
            case "Silver":
                return R.color.Silver;
            case "Teal":
                return R.color.Teal;
            case "Wheat":
                return R.color.Wheat;
            case "White":
                return R.color.white;
            case "Yellow":
                return R.color.yellow;
            default:
                return R.color.transparent;
        }
    }

    @Override
    public void ItemOnClickListener(String productId) {
//        loadingDialog dialog = new loadingDialog(open_product.this);
//        dialog.show();
        changeProduct(productId);
        scrollView.fullScroll(View.FOCUS_UP);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.cancel();
//                scrollView.fullScroll(View.FOCUS_UP);
//            }
//        }, 3000);

    }

}