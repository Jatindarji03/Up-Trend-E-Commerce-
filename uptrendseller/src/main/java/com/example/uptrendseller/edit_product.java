package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import DataModel.Product;
import io.github.muddz.styleabletoast.StyleableToast;

public class edit_product extends AppCompatActivity {
    private int count = 0,total=0;
    private StorageReference storageReference;
    private ArrayList<Uri> imagesUri=new ArrayList<>();

    private LinearLayout search_word_linear1, search_word_linear2, search_word_linear3,
            search_word_linear4, search_word_linear5, search_gone2, search_gone3, search_gone4, search_gone5,
            otherCategoryLayout,shirtLayout,jeansLayout,footWareLayout;

    private EditText search_wordET1, search_wordET2, search_wordET3, search_wordET4, search_wordET5, txtBrandName, txtProductName, txtSellingPrice, txtOriginalPrice;


    private TextView plus_click1, plus_click2, plus_click3, plus_click4,txtValueOtherCategory,txtTotalStockOtherCategory,
                    txtS,txtM,txtL,txtXL,txtXXL,txtTotalStockShirt,txt28,txt30,txt32,txt34,txt36,txt38,txt40,txtTotalStockJeans,
                    txt6,txt7,txt8,txt9,txt10, txtTotalStockFootWare,btnUpdate,close_btn;
    private String productId;
    private DatabaseReference productRef;
    private Product product;
    private ImageSlider productImage;
    ArrayList<SlideModel> slideModelsArrayList;
    AppCompatButton btnClearImages,btnAddImage;
    loadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        //search linear layout hide gone
        search_word_linear1 = findViewById(R.id.search_linear11);
        search_word_linear2 = findViewById(R.id.search_linear22);
        search_word_linear3 = findViewById(R.id.search_linear33);
        search_word_linear4 = findViewById(R.id.search_linear44);
        search_word_linear5 = findViewById(R.id.search_linear55);

        search_wordET1 = findViewById(R.id.search_keyword11);
        search_wordET2 = findViewById(R.id.search_keyword22);
        search_wordET3 = findViewById(R.id.search_keyword33);
        search_wordET4 = findViewById(R.id.search_keyword44);
        search_wordET5 = findViewById(R.id.search_keyword55);

        plus_click1 = findViewById(R.id.plus_click11);
        plus_click2 = findViewById(R.id.plus_click22);
        plus_click3 = findViewById(R.id.plus_click33);
        plus_click4 = findViewById(R.id.plus_click44);

        search_gone2 = findViewById(R.id.search_key_hide_gone22);
        search_gone3 = findViewById(R.id.search_key_hide_gone33);
        search_gone4 = findViewById(R.id.search_key_hide_gone44);
        search_gone5 = findViewById(R.id.search_key_hide_gone55);

        txtBrandName=findViewById(R.id.txtBrandName);
        txtProductName=findViewById(R.id.txtProductName);
        txtSellingPrice=findViewById(R.id.txtSellingPrice);
        txtOriginalPrice=findViewById(R.id.txtOriginalPrice);
        productImage=findViewById(R.id.imageSlider);

        otherCategoryLayout=findViewById(R.id.other_categoryED);
        shirtLayout=findViewById(R.id.layoutShirtED);
        jeansLayout=findViewById(R.id.layoutPantED);
        footWareLayout=findViewById(R.id.layoutFootWareED);

        txtValueOtherCategory=findViewById(R.id.value55);
        txtTotalStockOtherCategory=findViewById(R.id.totalStockOtherCategory);

        txtS=findViewById(R.id.value);
        txtM=findViewById(R.id.value2);
        txtL=findViewById(R.id.value3);
        txtXL=findViewById(R.id.value4);
        txtXXL=findViewById(R.id.value5);
        txtTotalStockShirt=findViewById(R.id.stock_shirt);

        txt28=findViewById(R.id.value6);
        txt30=findViewById(R.id.value7);
        txt32=findViewById(R.id.value8);
        txt34=findViewById(R.id.value9);
        txt36=findViewById(R.id.value10);
        txt38=findViewById(R.id.value11);
        txt40=findViewById(R.id.value12);
        txtTotalStockJeans=findViewById(R.id.stock_pant);

        txt6=findViewById(R.id.value13);
        txt7=findViewById(R.id.value14);
        txt8=findViewById(R.id.value15);
        txt9=findViewById(R.id.value16);
        txt10=findViewById(R.id.value17);
        txtTotalStockFootWare =findViewById(R.id.stock_shoes);

        btnUpdate=findViewById(R.id.btnUpdate);
        btnClearImages=findViewById(R.id.btnClearImages);
        btnAddImage=findViewById(R.id.btnAddImage);
        close_btn=findViewById(R.id.close_btn);




        //Getting Data From Previous Activity And Displaying ProductData.
        productId = getIntent().getStringExtra("productId");
        displayProductDetails(productId);



        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),inventory_product.class));
                finish();
            }
        });


        plus_click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (search_gone2.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(search_word_linear2, new AutoTransition());

                search_gone2.setVisibility(var);
            }
        });
        plus_click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (search_gone3.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(search_word_linear3, new AutoTransition());

                search_gone3.setVisibility(var);
            }
        });
        plus_click3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (search_gone4.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(search_word_linear4, new AutoTransition());

                search_gone4.setVisibility(var);
            }
        });
        plus_click4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (search_gone5.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(search_word_linear5, new AutoTransition());

                search_gone5.setVisibility(var);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new loadingDialog(edit_product.this);
                dialog.show();
                updateData(productId);
            }
        });

        btnClearImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.getProductImages().clear();
                imagesUri.clear();
                productImage.setImageList(new ArrayList<>(), ScaleTypes.FIT);
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//
//                }
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
            }
        });


    }

    public void updateData(String productId){
        ArrayList<String> searchKeyWord=new ArrayList<>();
        ArrayList<String> productSizes=new ArrayList<>();
        searchKeyWord.add(search_wordET1.getText().toString());
        searchKeyWord.add(search_wordET2.getText().toString());
        searchKeyWord.add(search_wordET3.getText().toString());
        searchKeyWord.add(search_wordET4.getText().toString());
        searchKeyWord.add(search_wordET5.getText().toString());


        productRef=FirebaseDatabase.getInstance().getReference("Product").child(productId);
        DatabaseReference productNameRef=productRef.child("productName");
        DatabaseReference brandNameRef=productRef.child("productBrandName");
        DatabaseReference sellingPriceRef=productRef.child("sellingPrice");
        DatabaseReference originalPriceRef=productRef.child("originalPrice");
        DatabaseReference totalStockRef=productRef.child("totalStock");
        DatabaseReference productSizesRef=productRef.child("productSizes");
        DatabaseReference searchKeyWordRef=productRef.child("searchKeyWord");
        DatabaseReference productImageRef=productRef.child("productImages");


        productNameRef.setValue(txtProductName.getText().toString());
        brandNameRef.setValue(txtBrandName.getText().toString());
        sellingPriceRef.setValue(txtSellingPrice.getText().toString());
        originalPriceRef.setValue(txtOriginalPrice.getText().toString());
        searchKeyWordRef.setValue(searchKeyWord);

        if(product.getProductCategory().equals("Men's(Top)") || product.getProductCategory().equals("Women's(Top)")) {
            productSizes.add(txtS.getText().toString());
            productSizes.add(txtM.getText().toString());
            productSizes.add(txtL.getText().toString());
            productSizes.add(txtXL.getText().toString());
            productSizes.add(txtXXL.getText().toString());
            productSizesRef.setValue(productSizes);
            totalStockRef.setValue(txtTotalStockShirt.getText().toString());

        }else if (product.getProductCategory().equals("Men's(Bottom)") || product.getProductCategory().equals("Women's(Bottom)")) {

            productSizes.add(txt28.getText().toString());
            productSizes.add(txt30.getText().toString());
            productSizes.add(txt32.getText().toString());
            productSizes.add(txt34.getText().toString());
            productSizes.add(txt36.getText().toString());
            productSizes.add(txt38.getText().toString());
            productSizes.add(txt40.getText().toString());

            productSizesRef.setValue(productSizes);
            totalStockRef.setValue(txtTotalStockJeans.getText().toString());

        }else if (product.getProductCategory().equals("Footware(Men)") || product.getProductCategory().equals("Footware(Women)")) {

            productSizes.add(txt6.getText().toString());
            productSizes.add(txt7.getText().toString());
            productSizes.add(txt8.getText().toString());
            productSizes.add(txt9.getText().toString());
            productSizes.add(txt10.getText().toString());
            productSizesRef.setValue(productSizes);
            totalStockRef.setValue(txtTotalStockFootWare.getText().toString());

        }else{

            totalStockRef.setValue(txtTotalStockOtherCategory.getText().toString());

        }

        if(imagesUri.size()!=0){
            Log.d("imagetest","images are selected");
            updateMultipleImages(imagesUri,productImageRef);

        }else{
            Log.d("imagetest","images are not selected");
            dialog.cancel();
            StyleableToast.makeText(getApplicationContext(),"Product Details Updated Successfully",R.style.UptrendToast).show();
            startActivity(new Intent(getApplicationContext(),inventory_product.class));
            finish();
        }

    }

    public void updateMultipleImages(ArrayList<Uri> imagesUri, DatabaseReference productImagesRef) {
        ArrayList<String> list = new ArrayList<>(product.getProductImages());
        storageReference = FirebaseStorage.getInstance().getReference();

        AtomicInteger uploadCount = new AtomicInteger(0);
        int totalUploads = imagesUri.size();

        for (Uri imageUri : imagesUri) {
            StorageReference imageRef = storageReference.child("Product Images/images" + UUID.randomUUID().toString());
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            list.add(uri.toString());
                            int count = uploadCount.incrementAndGet();
                            if (count == totalUploads) {
                                productImagesRef.setValue(list);
                                dialog.cancel();
                                StyleableToast.makeText(getApplicationContext(),"Product Details Updated Successfully",R.style.UptrendToast).show();
                                startActivity(new Intent(getApplicationContext(),inventory_product.class));
                                finish();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle upload failure
                    });
        }
    }


    /*
            This Method Will Display The Product Details.
     */
    public void displayProductDetails(String productId) {
        slideModelsArrayList=new ArrayList<>();
        productRef = FirebaseDatabase.getInstance().getReference("Product").child(productId);
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    product = snapshot.getValue(Product.class);
                    txtBrandName.setText(product.getProductBrandName());
                    txtProductName.setText(product.getProductName());
                    txtSellingPrice.setText(product.getSellingPrice());
                    txtOriginalPrice.setText(product.getOriginalPrice());
                    for(int i=0;i<product.getProductImages().size();i++){
                        slideModelsArrayList.add(new SlideModel(product.getProductImages().get(i), ScaleTypes.FIT));
                    }
                    productImage.setImageList(slideModelsArrayList,ScaleTypes.FIT);
                    search_wordET1.setText(product.getSearchKeyWord().get(0));
                    searchLayoutVisible(product.getSearchKeyWord().get(1),search_wordET2,search_gone2);
                    searchLayoutVisible(product.getSearchKeyWord().get(2),search_wordET3,search_gone3);
                    searchLayoutVisible(product.getSearchKeyWord().get(3),search_wordET4,search_gone4);
                    searchLayoutVisible(product.getSearchKeyWord().get(4),search_wordET5,search_gone5);

                    /*
                            Display stock Of product According To Product And
                            Also Visible The Size Layout according to the product
                     */
                    if(product.getProductCategory().equals("Men's(Top)") || product.getProductCategory().equals("Women's(Top)")){
                        shirtLayout.setVisibility(View.VISIBLE);
                        txtS.setText(product.getProductSizes().get(0));
                        txtM.setText(product.getProductSizes().get(1));
                        txtL.setText(product.getProductSizes().get(2));
                        txtXL.setText(product.getProductSizes().get(3));
                        txtXXL.setText(product.getProductSizes().get(4));
                        txtTotalStockShirt.setText(product.getTotalStock());
                    } else if (product.getProductCategory().equals("Men's(Bottom)") || product.getProductCategory().equals("Women's(Bottom)")) {
                        jeansLayout.setVisibility(View.VISIBLE);
                        txt28.setText(product.getProductSizes().get(0));
                        txt30.setText(product.getProductSizes().get(1));
                        txt32.setText(product.getProductSizes().get(2));
                        txt34.setText(product.getProductSizes().get(3));
                        txt36.setText(product.getProductSizes().get(4));
                        txt38.setText(product.getProductSizes().get(5));
                        txt40.setText(product.getProductSizes().get(6));
                        txtTotalStockJeans.setText(product.getTotalStock());
                    } else if (product.getProductCategory().equals("Footware(Men)") || product.getProductCategory().equals("Footware(Women)")) {
                        footWareLayout.setVisibility(View.VISIBLE);
                        txt6.setText(product.getProductSizes().get(0));
                        txt7.setText(product.getProductSizes().get(1));
                        txt8.setText(product.getProductSizes().get(2));
                        txt9.setText(product.getProductSizes().get(3));
                        txt10.setText(product.getProductSizes().get(4));
                        txtTotalStockFootWare.setText(product.getTotalStock());
                    }else{
                        otherCategoryLayout.setVisibility(View.VISIBLE);
                        txtValueOtherCategory.setText(product.getTotalStock());
                        txtTotalStockOtherCategory.setText(product.getTotalStock());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void searchLayoutVisible(String search,EditText editText,LinearLayout layout){
        if(search.length()!=0){
            layout.setVisibility(View.VISIBLE);
            editText.setText(search);
        }
    }

    public void increment(View v){
        switch (v.getId()){
            case R.id.txtPlus1:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count= Integer.parseInt(txtS.getText().toString());
                count++;
                total++;
                txtS.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus2:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count=Integer.parseInt(txtM.getText().toString());
                count++;
                total++;
                txtM.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus3:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count=Integer.parseInt(txtL.getText().toString());
                count++;
                total++;
                txtL.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus4:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count=Integer.parseInt(txtXL.getText().toString());
                count++;
                total++;
                txtXL.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus5:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count=Integer.parseInt(txtXXL.getText().toString());
                count++;
                total++;
                txtXXL.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus6:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt28.getText().toString());
                count++;
                total++;
                txt28.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtPlus7:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt30.getText().toString());
                count++;
                total++;
                txt30.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtPlus8:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt32.getText().toString());
                count++;
                total++;
                txt32.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtPlus9:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt34.getText().toString());
                count++;
                total++;
                txt34.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtPlus10:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt36.getText().toString());
                count++;
                total++;
                txt36.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtPlus11:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt38.getText().toString());
                count++;
                total++;
                txt38.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtPlus12:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt40.getText().toString());
                count++;
                total++;
                txt40.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtPlus13:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt6.getText().toString());
                count++;
                total++;
                txt6.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtPlus14:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt7.getText().toString());
                count++;
                total++;
                txt7.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtPlus15:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt8.getText().toString());
                count++;
                total++;
                txt8.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtPlus16:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt9.getText().toString());
                count++;
                total++;
                txt9.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtPlus17:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt10.getText().toString());
                count++;
                total++;
                txt10.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtPlus55:
                total=Integer.parseInt(txtTotalStockOtherCategory.getText().toString());
                count= Integer.parseInt(txtValueOtherCategory.getText().toString());
                count++;
                total++;
                txtValueOtherCategory.setText(String.valueOf(count));
                txtTotalStockOtherCategory.setText(String.valueOf(total));
                break;
        }
    }
    public void decrement(View v){
        switch (v.getId()){
            case R.id.txtMinus1:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count= Integer.parseInt(txtS.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txtS.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus2:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count= Integer.parseInt(txtM.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txtM.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus3:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count= Integer.parseInt(txtL.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txtL.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus4:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count= Integer.parseInt(txtXL.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txtXL.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus5:
                total=Integer.parseInt(txtTotalStockShirt.getText().toString());
                count= Integer.parseInt(txtXXL.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txtXXL.setText(String.valueOf(count));
                txtTotalStockShirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus6:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt28.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt28.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtMinus7:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt30.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt30.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtMinus8:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt32.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt32.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtMinus9:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt34.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt34.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtMinus10:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt36.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt36.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtMinus11:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt38.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt38.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtMinus12:
                total=Integer.parseInt(txtTotalStockJeans.getText().toString());
                count= Integer.parseInt(txt40.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt40.setText(String.valueOf(count));
                txtTotalStockJeans.setText(String.valueOf(total));
                break;
            case R.id.txtMinus13:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt6.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt6.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtMinus14:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt7.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt7.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtMinus15:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt8.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt8.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtMinus16:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt9.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt9.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtMinus17:
                total=Integer.parseInt(txtTotalStockFootWare.getText().toString());
                count= Integer.parseInt(txt10.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txt10.setText(String.valueOf(count));
                txtTotalStockFootWare.setText(String.valueOf(total));
                break;
            case R.id.txtMinus55:
                total=Integer.parseInt(txtTotalStockOtherCategory.getText().toString());
                count= Integer.parseInt(txtValueOtherCategory.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                txtValueOtherCategory.setText(String.valueOf(count));
                txtTotalStockOtherCategory.setText(String.valueOf(total));
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();

                for (int i = 0; i < x; i++) {
                    imagesUri.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                String imageURL = data.getData().getPath();
                imagesUri.add(Uri.parse(imageURL));
            }
            updateImageSlider();
        }
    }
    public void updateImageSlider(){
        slideModelsArrayList.clear();
        for(Uri uri:imagesUri){
            slideModelsArrayList.add(new SlideModel(uri.toString(),ScaleTypes.FIT));
        }
        productImage.setImageList(slideModelsArrayList,ScaleTypes.FIT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), inventory_product.class));
        finish();
    }
}