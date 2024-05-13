package com.example.uptrend;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.adapteranddatamodel.DiscountRange;
import com.example.adapteranddatamodel.PriceRange;

import java.util.ArrayList;

public class filter_product extends AppCompatActivity {


    private AppCompatButton btnReset,btnApply;

    private CheckBox checkBox_color1, checkBox_color2, checkBox_color3, checkBox_color4, checkBox_color5, checkBox_color6, checkBox_color7, checkBox_color8,
            checkBox_color9, checkBox_color10, checkBox_color11, checkBox_color12, checkBox_color13, checkBox_color14, checkBox_color15,
            checkBox_color16, checkBox_color17, checkBox_color18, checkBox_color19, checkBox_color20, checkBox_color21, checkBox_color22,
            checkBox_color23, checkBox_color24, checkBox_color25,checkBox_price1,checkBox_price2,checkBox_price3,checkBox_price4

            ,checkBox_price5,checkBox_price6,checkBox_price7,checkBox_price8,checkBox_price9,checkBox_discount1,checkBox_discount2

            ,checkBox_discount3,checkBox_discount4,checkBox_discount5,checkBox_discount6;
    private RadioGroup genderRG,brandRG;

    private TextView gender_txt, color_txt,brand_txt,price_txt,discount_txt,rating_txt;

    private LinearLayout gender_layout, color_layout,brand_layout,price_layout,discount_layout,rating_layout;
    private ArrayList<String> colour=new ArrayList<>();
    private ArrayList<PriceRange> price=new ArrayList<>();

    private String gender;
    private ArrayList<DiscountRange> discount=new ArrayList<>();
    private String brand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_product);


        gender_txt = findViewById(R.id.gender_click);
        gender_layout = findViewById(R.id.gender_layout);
        color_txt = findViewById(R.id.colour_click);
        color_layout = findViewById(R.id.color_layout);
        brand_txt = findViewById(R.id.brand_click);
        brand_layout = findViewById(R.id.brand_layout);
        price_txt = findViewById(R.id.price_click);
        price_layout = findViewById(R.id.price_layout);
        discount_txt = findViewById(R.id.discount_click);
        discount_layout = findViewById(R.id.discount_layout);



        //RadioGroup
        genderRG = findViewById(R.id.genderRadioGroup);
        brandRG = findViewById(R.id.brandRadioGroup);

        //CheckBox
        checkBox_color1 = findViewById(R.id.checkbox_color1);
        checkBox_color2 = findViewById(R.id.checkbox_color2);
        checkBox_color3 = findViewById(R.id.checkbox_color3);
        checkBox_color4 = findViewById(R.id.checkbox_color4);
        checkBox_color5 = findViewById(R.id.checkbox_color5);
        checkBox_color6 = findViewById(R.id.checkbox_color6);
        checkBox_color7 = findViewById(R.id.checkbox_color7);
        checkBox_color8 = findViewById(R.id.checkbox_color8);
        checkBox_color9 = findViewById(R.id.checkbox_color9);
        checkBox_color10 = findViewById(R.id.checkbox_color10);
        checkBox_color11 = findViewById(R.id.checkbox_color11);
        checkBox_color12 = findViewById(R.id.checkbox_color12);
        checkBox_color13 = findViewById(R.id.checkbox_color13);
        checkBox_color14 = findViewById(R.id.checkbox_color14);
        checkBox_color15 = findViewById(R.id.checkbox_color15);
        checkBox_color16 = findViewById(R.id.checkbox_color16);
        checkBox_color17 = findViewById(R.id.checkbox_color17);
        checkBox_color18 = findViewById(R.id.checkbox_color18);
        checkBox_color19 = findViewById(R.id.checkbox_color19);
        checkBox_color20 = findViewById(R.id.checkbox_color20);
        checkBox_color21 = findViewById(R.id.checkbox_color21);
        checkBox_color22 = findViewById(R.id.checkbox_color22);
        checkBox_color23 = findViewById(R.id.checkbox_color23);
        checkBox_color24 = findViewById(R.id.checkbox_color24);
        checkBox_color25 = findViewById(R.id.checkbox_color25);

        checkBox_price1 = findViewById(R.id.checkbox_brand1);
        checkBox_price2 = findViewById(R.id.checkbox_brand2);
        checkBox_price3 = findViewById(R.id.checkbox_brand3);
        checkBox_price4 = findViewById(R.id.checkbox_brand4);
        checkBox_price5 = findViewById(R.id.checkbox_brand5);
        checkBox_price6 = findViewById(R.id.checkbox_brand6);
        checkBox_price7 = findViewById(R.id.checkbox_brand7);
        checkBox_price8 = findViewById(R.id.checkbox_brand8);
        checkBox_price9 = findViewById(R.id.checkbox_brand9);

        checkBox_discount1 = findViewById(R.id.checkbox_discount1);
        checkBox_discount2 = findViewById(R.id.checkbox_discount2);
        checkBox_discount3 = findViewById(R.id.checkbox_discount3);
        checkBox_discount4 = findViewById(R.id.checkbox_discount4);
        checkBox_discount5 = findViewById(R.id.checkbox_discount5);
        checkBox_discount6 = findViewById(R.id.checkbox_discount6);


        //Button
        btnReset = findViewById(R.id.btnReset);
        btnApply=findViewById(R.id.btnApply);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //clear all RadioButton click
                genderRG.clearCheck();
                brandRG.clearCheck();



                //clear all CheckBox click

                if (checkBox_color1.isChecked()) {
                    checkBox_color1.setChecked(false);
                }
                if (checkBox_color2.isChecked()) {
                    checkBox_color2.setChecked(false);
                }
                if (checkBox_color3.isChecked()) {
                    checkBox_color3.setChecked(false);
                }
                if (checkBox_color4.isChecked()) {
                    checkBox_color4.setChecked(false);
                }
                if (checkBox_color5.isChecked()) {
                    checkBox_color5.setChecked(false);
                }
                if (checkBox_color6.isChecked()) {
                    checkBox_color6.setChecked(false);
                }
                if (checkBox_color7.isChecked()) {
                    checkBox_color7.setChecked(false);
                }
                if (checkBox_color8.isChecked()) {
                    checkBox_color8.setChecked(false);
                }
                if (checkBox_color9.isChecked()) {
                    checkBox_color9.setChecked(false);
                }
                if (checkBox_color10.isChecked()) {
                    checkBox_color10.setChecked(false);
                }
                if (checkBox_color11.isChecked()) {
                    checkBox_color11.setChecked(false);
                }
                if (checkBox_color12.isChecked()) {
                    checkBox_color12.setChecked(false);
                }
                if (checkBox_color13.isChecked()) {
                    checkBox_color13.setChecked(false);
                }
                if (checkBox_color14.isChecked()) {
                    checkBox_color14.setChecked(false);
                }
                if (checkBox_color15.isChecked()) {
                    checkBox_color15.setChecked(false);
                }
                if (checkBox_color16.isChecked()) {
                    checkBox_color16.setChecked(false);
                }
                if (checkBox_color17.isChecked()) {
                    checkBox_color17.setChecked(false);
                }
                if (checkBox_color18.isChecked()) {
                    checkBox_color18.setChecked(false);
                }
                if (checkBox_color19.isChecked()) {
                    checkBox_color19.setChecked(false);
                }
                if (checkBox_color20.isChecked()) {
                    checkBox_color20.setChecked(false);
                }
                if (checkBox_color21.isChecked()) {
                    checkBox_color21.setChecked(false);
                }
                if (checkBox_color22.isChecked()) {
                    checkBox_color22.setChecked(false);
                }
                if (checkBox_color23.isChecked()) {
                    checkBox_color23.setChecked(false);
                }
                if (checkBox_color24.isChecked()) {
                    checkBox_color24.setChecked(false);
                }
                if (checkBox_color25.isChecked()) {
                    checkBox_color25.setChecked(false);
                }

                if (checkBox_price1.isChecked()) {
                    checkBox_price1.setChecked(false);
                }
                if (checkBox_price2.isChecked()) {
                    checkBox_price2.setChecked(false);
                }
                if (checkBox_price3.isChecked()) {
                    checkBox_price3.setChecked(false);
                }
                if (checkBox_price4.isChecked()) {
                    checkBox_price4.setChecked(false);
                }
                if (checkBox_price5.isChecked()) {
                    checkBox_price5.setChecked(false);
                }
                if (checkBox_price6.isChecked()) {
                    checkBox_price6.setChecked(false);
                }
                if (checkBox_price7.isChecked()) {
                    checkBox_price7.setChecked(false);
                }
                if (checkBox_price8.isChecked()) {
                    checkBox_price8.setChecked(false);
                }
                if (checkBox_price9.isChecked()) {
                    checkBox_price9.setChecked(false);
                }

                if (checkBox_discount1.isChecked()) {
                    checkBox_discount1.setChecked(false);
                }
                if (checkBox_discount2.isChecked()) {
                    checkBox_discount2.setChecked(false);
                }
                if (checkBox_discount3.isChecked()) {
                    checkBox_discount3.setChecked(false);
                }
                if (checkBox_discount4.isChecked()) {
                    checkBox_discount4.setChecked(false);
                }
                if (checkBox_discount5.isChecked()) {
                    checkBox_discount5.setChecked(false);
                }
                if (checkBox_discount6.isChecked()) {
                    checkBox_discount6.setChecked(false);
                }





            }
        });



        gender_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (gender_layout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(gender_layout, new AutoTransition());

                gender_layout.setVisibility(var);
            }
        });

        color_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (color_layout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(color_layout, new AutoTransition());

                color_layout.setVisibility(var);
            }
        });

        brand_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (brand_layout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(brand_layout, new AutoTransition());

                brand_layout.setVisibility(var);
            }
        });

        price_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (price_layout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(price_layout, new AutoTransition());

                price_layout.setVisibility(var);
            }
        });


        discount_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (discount_layout.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(discount_layout, new AutoTransition());

                discount_layout.setVisibility(var);
            }
        });


        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.radioButtonMale){
                    gender="male";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.radioButtonFemale) {
                    gender="female";
                }
            }
        });
        brandRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.Nike){
                    brand="Nike";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Apple) {
                    brand="Apple";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Zara) {
                    brand="Zara";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Rolex) {
                    brand="Rolex";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Puma) {
                    brand="Puma";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Loreal) {
                    brand="Loreal";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Cadbury) {
                    brand="Cadbury";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Levis) {
                    brand="Levi's";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Cartier) {
                    brand="Cartier";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Biba) {
                    brand="Biba";
                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Charlie_Bear) {
                    brand="Charlie Bear";

                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Doms) {
                    brand="Doms";

                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Lee) {
                    brand="Lee";

                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Lenskart) {
                    brand="Lenskart";


                } else if (radioGroup.getCheckedRadioButtonId()==R.id.Manish_Malhotra) {
                    brand="Manish Malhotra";
                }
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox_color1.isChecked()) colour.add("Aquamarine");
                if(checkBox_color2.isChecked()) colour.add("Azure");
                if(checkBox_color3.isChecked()) colour.add("Black");
                if(checkBox_color4.isChecked()) colour.add("Brown");
                if(checkBox_color5.isChecked()) colour.add("Coral");
                if(checkBox_color6.isChecked()) colour.add("Crimson");
                if(checkBox_color7.isChecked()) colour.add("Cyan");
                if(checkBox_color8.isChecked()) colour.add("Golden");
                if(checkBox_color9.isChecked()) colour.add("Gray");
                if(checkBox_color10.isChecked()) colour.add("Green");
                if(checkBox_color11.isChecked()) colour.add("Hot Pink");
                if(checkBox_color12.isChecked()) colour.add("Lime");
                if(checkBox_color13.isChecked()) colour.add("Magent");
                if(checkBox_color14.isChecked()) colour.add("Maroon");
                if(checkBox_color15.isChecked()) colour.add("Navy Blue");
                if(checkBox_color16.isChecked()) colour.add("Olive");
                if(checkBox_color17.isChecked()) colour.add("Orange");
                if(checkBox_color18.isChecked()) colour.add("Purple");
                if(checkBox_color19.isChecked()) colour.add("Red");
                if(checkBox_color20.isChecked()) colour.add("Royal Blue");
                if(checkBox_color21.isChecked()) colour.add("Silver");
                if(checkBox_color22.isChecked()) colour.add("Teal");
                if(checkBox_color23.isChecked()) colour.add("Wheat");
                if(checkBox_color24.isChecked()) colour.add("White");
                if(checkBox_color25.isChecked()) colour.add("Yellow");

                if(checkBox_price1.isChecked()) price.add(new PriceRange("0","500"));
                if(checkBox_price2.isChecked()) price.add(new PriceRange("501","1000"));
                if(checkBox_price3.isChecked()) price.add(new PriceRange("1001","1500"));
                if(checkBox_price4.isChecked()) price.add(new PriceRange("1501","2000"));
                if(checkBox_price5.isChecked()) price.add(new PriceRange("2001","2500"));
                if(checkBox_price6.isChecked()) price.add(new PriceRange("2501","5000"));
                if(checkBox_price7.isChecked()) price.add(new PriceRange("5001","10000"));
                if(checkBox_price8.isChecked()) price.add(new PriceRange("10001","50000"));
                if(checkBox_price9.isChecked()) price.add(new PriceRange("50001",String.valueOf(Long.MAX_VALUE)));

                if(checkBox_discount1.isChecked()) discount.add(new DiscountRange("0.00","20.99"));
                if(checkBox_discount2.isChecked()) discount.add(new DiscountRange("21.00","30.99"));
                if(checkBox_discount3.isChecked()) discount.add(new DiscountRange("31.00","40.99"));
                if(checkBox_discount4.isChecked()) discount.add(new DiscountRange("41.00","50.99"));
                if(checkBox_discount5.isChecked()) discount.add(new DiscountRange("51.00","80.99"));
                if(checkBox_discount6.isChecked()) discount.add(new DiscountRange("81.00","99.99"));

                Intent intent=new Intent(filter_product.this,search_product.class);
                intent.putExtra("activityName","filterActivity");
                intent.putExtra("colour",colour);
                intent.putExtra("gender",gender);
                intent.putExtra("brand",brand);
                intent.putExtra("price",price);
                intent.putExtra("discount",discount);
                startActivity(intent);
                finish();
            }
        });

    }
}