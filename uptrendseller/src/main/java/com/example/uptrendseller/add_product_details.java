package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import DataModel.Product;
import io.github.muddz.styleabletoast.StyleableToast;

public class add_product_details extends AppCompatActivity {

   private EditText brandName,productName,original_price,selling_price,pack_of_product,search_wordET1,search_wordET2,search_wordET3,search_wordET4,search_wordET5,ram_mobile,
           storage_mobile,processor_mobile,rear_camera_mobile,front_camera_mobile,battery_mobile;

   private AutoCompleteTextView autoCompleteWeight,idealType,fabricType,selectionType,fabric_careType;
   private LinearLayout colour_gone,fabric_Gone,selection_Gone,fabric_care_Gone,search_word_linear1,search_word_linear2,search_word_linear3,
            search_word_linear4, search_word_linear5,search_gone2,search_gone3,search_gone4,search_gone5;
   private CardView cardView;


    private TextView value1,value2,value3,value4,value5,stock_shirt,value6,value7,value8,
            value9,value10,value11,value12,stock_pant,value13,value14,value15,value16,value17,value55,totalStockOtherCategory,stock_shoes,
            click_info,get_color,get_color_name,plus_click1,plus_click2,plus_click3,plus_click4,save_details_btn,clothes_hading_txt;
    private int count = 0,total=0;
    private LinearLayout layoutShirt,layoutPant,layoutFootWare,layoutOtherCategory,mobileDetailLayout;
    private String category,key,SubCategory;
    private RadioGroup colourRadioGroup;
    private DatabaseReference databaseReference;

    private loadingDialog2 loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_details);

        // FindView by Id of all Widget.
        value1 = findViewById(R.id.value);
        value2 = findViewById(R.id.value2);
        value3= findViewById(R.id.value3);
        value4= findViewById(R.id.value4);
        value5= findViewById(R.id.value5);
        stock_shirt=findViewById(R.id.stock_shirt);
        value6=findViewById(R.id.value6);
        value7=findViewById(R.id.value7);
        value8=findViewById(R.id.value8);
        value9=findViewById(R.id.value9);
        value10=findViewById(R.id.value10);
        value11=findViewById(R.id.value11);
        value12=findViewById(R.id.value12);
        stock_pant=findViewById(R.id.stock_pant);
        stock_shoes=findViewById(R.id.stock_shoes);
        value13=findViewById(R.id.value13);
        value14=findViewById(R.id.value14);
        value15=findViewById(R.id.value15);
        value16=findViewById(R.id.value16);
        value17=findViewById(R.id.value17);
        value55=findViewById(R.id.value55);
        totalStockOtherCategory=findViewById(R.id.totalStockOtherCategory);
        clothes_hading_txt=findViewById(R.id.clothes_hading_txt);

        layoutShirt=findViewById(R.id.layoutShirt);
        layoutPant=findViewById(R.id.layoutPant);
        layoutFootWare=findViewById(R.id.layoutFootWare);
        layoutOtherCategory=findViewById(R.id.other_category);

        brandName = findViewById(R.id.brand_name);
        productName = findViewById(R.id.product_name);
        original_price = findViewById(R.id.original_price);
        selling_price = findViewById(R.id.selling_price);
        pack_of_product = findViewById(R.id.pack_of);

        //
        autoCompleteWeight = findViewById(R.id.weight_product);
        idealType=findViewById(R.id.idealType);
        fabricType=findViewById(R.id.fabricType);
        selectionType=findViewById(R.id.selectionType);
        fabric_careType=findViewById(R.id.fabric_careType);

        //get colour
        get_color_name = findViewById(R.id.get_color_name);
        get_color = findViewById(R.id.get_color);


        //gone
        fabric_Gone = findViewById(R.id.fabric_gone);
        selection_Gone = findViewById(R.id.selection_gone);
        fabric_care_Gone = findViewById(R.id.fabric_care_gone);

        //search linear layout hide gone
        search_word_linear1 = findViewById(R.id.search_linear1);
        search_word_linear2 = findViewById(R.id.search_linear2);
        search_word_linear3 = findViewById(R.id.search_linear3);
        search_word_linear4 = findViewById(R.id.search_linear4);
        search_word_linear5 = findViewById(R.id.search_linear5);

        search_wordET1 = findViewById(R.id.search_keyword1);
        search_wordET2 = findViewById(R.id.search_keyword2);
        search_wordET3 = findViewById(R.id.search_keyword3);
        search_wordET4 = findViewById(R.id.search_keyword4);
        search_wordET5 = findViewById(R.id.search_keyword5);

        plus_click1 = findViewById(R.id.plus_click1);
        plus_click2 = findViewById(R.id.plus_click2);
        plus_click3 = findViewById(R.id.plus_click3);
        plus_click4 = findViewById(R.id.plus_click4);

        search_gone2 = findViewById(R.id.search_key_hide_gone2);
        search_gone3 = findViewById(R.id.search_key_hide_gone3);
        search_gone4 = findViewById(R.id.search_key_hide_gone4);
        search_gone5 = findViewById(R.id.search_key_hide_gone5);


        colourRadioGroup=findViewById(R.id.colourRadioGroup);
        mobileDetailLayout=findViewById(R.id.mobileDetailLayout);




        //Initialization for instruction effect
        click_info = findViewById(R.id.instruction_click);
        colour_gone = findViewById(R.id.linearLayout);
        cardView = findViewById(R.id.cardView);

        //save btn
        save_details_btn = findViewById(R.id.save_details_txt);

        //Mobile
        ram_mobile=findViewById(R.id.ram_mobile);
        storage_mobile=findViewById(R.id.storage_mobile);
        processor_mobile=findViewById(R.id.processor_mobile);
        rear_camera_mobile=findViewById(R.id.rear_camera_mobile);
        front_camera_mobile=findViewById(R.id.front_camera_mobile);
        battery_mobile=findViewById(R.id.battery_mobile);

        //AutoCompleteTextview
        String[] weightArray = getResources().getStringArray(R.array.net_quantity);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(add_product_details.this, android.R.layout.simple_list_item_1,weightArray);
        autoCompleteWeight.setAdapter(arrayAdapter);


        click_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (cardView.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(colour_gone, new AutoTransition());

                cardView.setVisibility(var);
            }
        });

        colourRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton radioButton=radioGroup.findViewById(id);
                get_color_name.setText(radioButton.getText().toString());
                int colorResId=getColorResourceId(radioButton.getText().toString());

                //This Method Will Change The Textview BackgroundTint According which Colour is Selected.
                setTextViewBackgroundTint(get_color,colorResId);
                cardView.setVisibility(View.GONE);


            }
        });
        plus_click1 .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (search_gone2.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(search_word_linear2, new AutoTransition());

                search_gone2.setVisibility(var);
            }
        });
        plus_click2 .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (search_gone3.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(search_word_linear3, new AutoTransition());

                search_gone3.setVisibility(var);
            }
        });
        plus_click3 .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (search_gone4.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(search_word_linear4, new AutoTransition());

                search_gone4.setVisibility(var);
            }
        });
        plus_click4 .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (search_gone5.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(search_word_linear5, new AutoTransition());

                search_gone5.setVisibility(var);
            }
        });


        category=getIntent().getStringExtra("Category");
        key=getIntent().getStringExtra("Key");
        SubCategory=getIntent().getStringExtra("SubCategory");

        //getting databaseReference of our Database;
        databaseReference= FirebaseDatabase.getInstance().getReference("Product").child(key);

        //This method Will visible size layout of specific category.
        visibleLayoutSize(category);
        loading=new loadingDialog2(add_product_details.this);
        save_details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loading.show();
                if(validInput()){
                    ArrayList<String> searchKeyWord=new ArrayList<>();
                    ArrayList<String> productSizes=new ArrayList<>();
                    HashMap<String,Object> product=new HashMap<>();
                    product.put("productBrandName",brandName.getText().toString());
                    product.put("productName",productName.getText().toString());
                    product.put("originalPrice",original_price.getText().toString());
                    product.put("sellingPrice",selling_price.getText().toString());
                    product.put("productPacking",pack_of_product.getText().toString());
                    product.put("productColour",get_color_name.getText().toString());
                    product.put("productSuitFor",idealType.getText().toString());
                    searchKeyWord.add(search_wordET1.getText().toString());
                    searchKeyWord.add(search_wordET2.getText().toString());
                    searchKeyWord.add(search_wordET3.getText().toString());
                    searchKeyWord.add(search_wordET4.getText().toString());
                    searchKeyWord.add(search_wordET5.getText().toString());
                    product.put("searchKeyWord",searchKeyWord);
                    product.put("productWeight",autoCompleteWeight.getText().toString());

                    if(category.equals("Men's(Top)") || category.equals("Women's(Top)")){
                        productSizes.add(value1.getText().toString());
                        productSizes.add(value2.getText().toString());
                        productSizes.add(value3.getText().toString());
                        productSizes.add(value4.getText().toString());
                        productSizes.add(value5.getText().toString());
                        product.put("productSizes",productSizes);
                        product.put("totalStock",stock_shirt.getText().toString());
                        product.put("productFabric",fabricType.getText().toString());
                        product.put("productOccasion",selectionType.getText().toString());
                        product.put("productWashcare",fabric_careType.getText().toString());
                        databaseReference.updateChildren(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                   new Handler().postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           loading.cancel();
                                           Intent i=new Intent(getApplicationContext(), add_product_manufacture_details.class);
                                           i.putExtra("Key",key);
                                           startActivity(i);
                                           Toast.makeText(add_product_details.this, "Done", Toast.LENGTH_SHORT).show();
                                       }
                                   },2000);

                                }
                            }
                        });
                    } else if (category.equals("Men's(Bottom)") || category.equals("Women's(Bottom)")) {

                        productSizes.add(value6.getText().toString());
                        productSizes.add(value7.getText().toString());
                        productSizes.add(value8.getText().toString());
                        productSizes.add(value9.getText().toString());
                        productSizes.add(value10.getText().toString());
                        productSizes.add(value11.getText().toString());
                        productSizes.add(value12.getText().toString());
                        product.put("productSizes",productSizes);
                        product.put("totalStock",stock_pant.getText().toString());
                        product.put("productFabric",fabricType.getText().toString());
                        product.put("productOccasion",selectionType.getText().toString());
                        product.put("productWashcare",fabric_careType.getText().toString());
                        databaseReference.updateChildren(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loading.cancel();
                                            Intent i=new Intent(getApplicationContext(), add_product_manufacture_details.class);
                                            i.putExtra("Key",key);
                                            startActivity(i);
                                            Toast.makeText(add_product_details.this, "Done", Toast.LENGTH_SHORT).show();
                                        }
                                    },2000);

                                }
                            }
                        });

                    } else if (category.equals("Footware(Men)") || category.equals("Footware(Women)")) {

                        productSizes.add(value13.getText().toString());
                        productSizes.add(value14.getText().toString());
                        productSizes.add(value15.getText().toString());
                        productSizes.add(value16.getText().toString());
                        productSizes.add(value17.getText().toString());
                        product.put("productSizes",productSizes);
                        product.put("totalStock",stock_shoes.getText().toString());
                        databaseReference.updateChildren(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                   loading.cancel();
                                   new Handler().postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           Intent i=new Intent(getApplicationContext(), add_product_manufacture_details.class);
                                           i.putExtra("Key",key);
                                           startActivity(i);
                                           Toast.makeText(add_product_details.this, "Done", Toast.LENGTH_SHORT).show();
                                       }
                                   },2000);
                                }
                            }
                        });
                    } else if (SubCategory.equals("Smartphones")) {
                        product.put("ram",ram_mobile.getText().toString().trim());
                        product.put("storage",storage_mobile.getText().toString().trim());
                        product.put("processor",processor_mobile.getText().toString().trim());
                        product.put("rearCamera",rear_camera_mobile.getText().toString().trim());
                        product.put("frontCamera",front_camera_mobile.getText().toString().trim());
                        product.put("battery",battery_mobile.getText().toString().trim());
                        product.put("totalStock",totalStockOtherCategory.getText().toString());
                        databaseReference.updateChildren(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                    loading.cancel();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i=new Intent(getApplicationContext(), add_product_manufacture_details.class);
                                            i.putExtra("Key",key);
                                            startActivity(i);
                                            Toast.makeText(add_product_details.this, "Done", Toast.LENGTH_SHORT).show();
                                        }
                                    },2000);
                                }
                            }
                        });

                    } else{

                        product.put("totalStock",totalStockOtherCategory.getText().toString());
                        databaseReference.updateChildren(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                   loading.cancel();
                                   new Handler().postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           StyleableToast.makeText(getApplicationContext(),"Product Details Saved Successfully",R.style.UptrendToast).show();
                                           Intent i=new Intent(getApplicationContext(), add_product_manufacture_details.class);
                                           i.putExtra("Key",key);
                                           startActivity(i);
                                           finish();

                                       }
                                   },2000);
                                }
                            }
                        });
                    }
                    //startActivity(new Intent(getApplicationContext(), add_product_manufacture_details.class));

                }
            }
        });
        ChangeColour.changeColour(getApplicationContext(),brandName);
        ChangeColour.changeColour(getApplicationContext(),productName);
        ChangeColour.changeColour(getApplicationContext(),original_price);
        ChangeColour.changeColour(getApplicationContext(),selling_price);
        ChangeColour.changeColour(getApplicationContext(),pack_of_product);

    }


    //This Method Will Check the Input Data is Valid or Not.

    public boolean validInput(){
        if(TextUtils.isEmpty(brandName.getText().toString())){
            ChangeColour.errorColour(getApplicationContext(),brandName,"This Filed Is Required");
            return false;
        }
        if(TextUtils.isEmpty(productName.getText().toString())){
            ChangeColour.errorColour(getApplicationContext(),productName,"This Filed Is Required");
            return false;
        }
        if(TextUtils.isEmpty(original_price.getText().toString())){
            ChangeColour.errorColour(getApplicationContext(),original_price,"This Filed Is Required");
            return false;
        }
        if(TextUtils.isEmpty(selling_price.getText().toString())){
            ChangeColour.errorColour(getApplicationContext(),selling_price,"This Filed Is Required");
            return false;
        }
        if(TextUtils.isEmpty(pack_of_product.getText().toString())){
            ChangeColour.errorColour(getApplicationContext(),pack_of_product,"This Filed Is Required");
            return false;
        }
        return true;
    }
    //This method Will visible size layout of specific category.
    public void visibleLayoutSize(String category){
        if(category.equals("Men's(Top)") || category.equals("Women's(Top)")){
            layoutShirt.setVisibility(View.VISIBLE);
            clothes_hading_txt.setVisibility(View.VISIBLE);
            mobileDetailLayout.setVisibility(View.GONE);
        } else if (category.equals("Men's(Bottom)") || category.equals("Women's(Bottom)")) {
            layoutPant.setVisibility(View.VISIBLE);
            clothes_hading_txt.setVisibility(View.VISIBLE);
            mobileDetailLayout.setVisibility(View.GONE);
        } else if (category.equals("Footware(Men)") || category.equals("Footware(Women)")) {
            layoutFootWare.setVisibility(View.VISIBLE);
            fabric_care_Gone.setVisibility(View.GONE);
            fabric_Gone.setVisibility(View.GONE);
            selection_Gone.setVisibility(View.GONE);
            mobileDetailLayout.setVisibility(View.GONE);
        } else if (SubCategory.equals("Smartphones")) {
            mobileDetailLayout.setVisibility(View.VISIBLE);
            fabric_care_Gone.setVisibility(View.GONE);
            fabric_Gone.setVisibility(View.GONE);
            selection_Gone.setVisibility(View.GONE);
            layoutOtherCategory.setVisibility(View.VISIBLE);
        } else{
            layoutOtherCategory.setVisibility(View.VISIBLE);
            fabric_care_Gone.setVisibility(View.GONE);
            fabric_Gone.setVisibility(View.GONE);
            selection_Gone.setVisibility(View.GONE);
            mobileDetailLayout.setVisibility(View.GONE);

        }
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

    public void increment(View v){
        switch (v.getId()){
            case R.id.txtPlus1:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count= Integer.parseInt(value1.getText().toString());
                count++;
                total++;
                value1.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus2:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count=Integer.parseInt(value2.getText().toString());
                count++;
                total++;
                value2.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus3:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count=Integer.parseInt(value3.getText().toString());
                count++;
                total++;
                value3.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus4:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count=Integer.parseInt(value4.getText().toString());
                count++;
                total++;
                value4.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus5:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count=Integer.parseInt(value5.getText().toString());
                count++;
                total++;
                value5.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtPlus6:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value6.getText().toString());
                count++;
                total++;
                value6.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtPlus7:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value7.getText().toString());
                count++;
                total++;
                value7.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtPlus8:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value8.getText().toString());
                count++;
                total++;
                value8.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtPlus9:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value9.getText().toString());
                count++;
                total++;
                value9.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtPlus10:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value10.getText().toString());
                count++;
                total++;
                value10.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtPlus11:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value11.getText().toString());
                count++;
                total++;
                value11.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtPlus12:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value12.getText().toString());
                count++;
                total++;
                value12.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtPlus13:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value13.getText().toString());
                count++;
                total++;
                value13.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtPlus14:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value14.getText().toString());
                count++;
                total++;
                value14.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtPlus15:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value15.getText().toString());
                count++;
                total++;
                value15.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtPlus16:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value16.getText().toString());
                count++;
                total++;
                value16.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtPlus17:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value17.getText().toString());
                count++;
                total++;
                value17.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtPlus55:
                total=Integer.parseInt(totalStockOtherCategory.getText().toString());
                count= Integer.parseInt(value55.getText().toString());
                count++;
                total++;
                value55.setText(String.valueOf(count));
                totalStockOtherCategory.setText(String.valueOf(total));
                break;
        }
    }
    public void decrement(View v){
        switch (v.getId()){
            case R.id.txtMinus1:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count= Integer.parseInt(value1.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value1.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus2:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count= Integer.parseInt(value2.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value2.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus3:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count= Integer.parseInt(value3.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value3.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus4:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count= Integer.parseInt(value4.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value4.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus5:
                total=Integer.parseInt(stock_shirt.getText().toString());
                count= Integer.parseInt(value5.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value5.setText(String.valueOf(count));
                stock_shirt.setText(String.valueOf(total));
                break;
            case R.id.txtMinus6:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value6.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value6.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtMinus7:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value7.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value7.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtMinus8:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value8.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value8.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtMinus9:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value9.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value9.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtMinus10:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value10.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value10.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtMinus11:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value11.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value11.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtMinus12:
                total=Integer.parseInt(stock_pant.getText().toString());
                count= Integer.parseInt(value12.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value12.setText(String.valueOf(count));
                stock_pant.setText(String.valueOf(total));
                break;
            case R.id.txtMinus13:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value13.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value13.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtMinus14:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value14.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value14.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtMinus15:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value15.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value15.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtMinus16:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value16.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value16.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtMinus17:
                total=Integer.parseInt(stock_shoes.getText().toString());
                count= Integer.parseInt(value17.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value17.setText(String.valueOf(count));
                stock_shoes.setText(String.valueOf(total));
                break;
            case R.id.txtMinus55:
                total=Integer.parseInt(totalStockOtherCategory.getText().toString());
                count= Integer.parseInt(value55.getText().toString());
                if(count<=0) count=0;
                else if (total<=0) total=0;
                else {
                    count--;
                    total--;
                }
                value55.setText(String.valueOf(count));
                totalStockOtherCategory.setText(String.valueOf(total));
                break;
        }

    }
}