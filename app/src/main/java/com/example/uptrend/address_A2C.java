package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapteranddatamodel.Pattern;
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

import java.util.HashMap;

import DataModel.UserAddress;
import io.github.muddz.styleabletoast.StyleableToast;

public class address_A2C extends AppCompatActivity{


    private TextView second_no, close_btnA;


    private LinearLayout layout_phone;


    private EditText txtAlternatePhoneNo,txtName,txtPhoneNo,txtPincode
            ,txtState,txtCity,txtHouseNo,txtRoadName;
   private RadioGroup radioGroupTypeOfAddress;
    private AppCompatButton btnSaveAddress;
    private DatabaseReference userAddressRef;
    private FirebaseUser user;
    private UserAddress userAddress;
    private String status,activityName,productId;
    private RadioButton radioButtonWork,radioButtonHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_a2_c);
        //FindViewBy Id
        second_no = findViewById(R.id.second_no_txt);
        txtAlternatePhoneNo = findViewById(R.id.second_no_address);
        layout_phone = findViewById(R.id.layout_second_no);
        close_btnA = findViewById(R.id.close_btn_address);
        txtName=findViewById(R.id.name_adress);
        txtPhoneNo=findViewById(R.id.phone_no_address);
        txtPincode=findViewById(R.id.pincode_address);
        txtState=findViewById(R.id.state_address);
        txtCity=findViewById(R.id.city_address);
        txtHouseNo=findViewById(R.id.house_adress);
        txtRoadName=findViewById(R.id.road_adress);
        btnSaveAddress=findViewById(R.id.save_address_btn);
        radioGroupTypeOfAddress=findViewById(R.id.radioGroupTypeOfAddress);
        radioButtonHome=findViewById(R.id.radioButtonHome);
        radioButtonWork=findViewById(R.id.radioButtonWork);

        userAddress=new UserAddress();
        userAddressRef= FirebaseDatabase.getInstance().getReference("UserAddress");
        user= FirebaseAuth.getInstance().getCurrentUser();

        //Getting Data from previous activity

        status=getIntent().getStringExtra("status");
        activityName=getIntent().getStringExtra("activityName");
        productId=getIntent().getStringExtra("productId");

        showUserAddress();



        second_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (layout_phone.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(layout_phone, new AutoTransition());
                layout_phone.setVisibility(var);
            }
        });
        close_btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), add_to_cart_product.class));
                finish();
            }
        });
        radioGroupTypeOfAddress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radioButtonHome){
                    userAddress.setAddressType("Home");
                } else if (i==R.id.radioButtonWork) {
                    userAddress.setAddressType("Work");
                }
            }
        });
        Checkout.preload(address_A2C.this);
        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAddress.setFullName(txtName.getText().toString().trim());
                userAddress.setMobileNo(txtPhoneNo.getText().toString().trim());
                userAddress.setAlternateMobileNo(txtAlternatePhoneNo.getText().toString().trim());
                userAddress.setPincode(txtPincode.getText().toString().trim());
                userAddress.setState(txtState.getText().toString().trim());
                userAddress.setCity(txtCity.getText().toString().trim());
                userAddress.setHouseNo(txtHouseNo.getText().toString().trim());
                userAddress.setRoadName(txtRoadName.getText().toString().trim());
                if(status.equals("update")){
                    updateAddress(userAddress);
                }else {
                    userAddress.setUserId(user.getUid());
                    if (validInput(userAddress)) {
                        userAddressRef.push().setValue(userAddress);
                        Toast.makeText(address_A2C.this, "Now You Can Purchase The Product", Toast.LENGTH_SHORT).show();
                        if(activityName.equals("openProduct")){
                            Intent i=new Intent(address_A2C.this,open_product.class);
                            i.putExtra("productId",productId);
                            startActivity(i);
                            finish();
                        } else if (activityName.equals("cart")) {
                            startActivity(new Intent(address_A2C.this,add_to_cart_product.class));
                            finish();
                        }
                    }
                }

            }
        });

        ChangeColour.changeColour(getApplicationContext(),txtName);
        ChangeColour.changeColour(getApplicationContext(),txtPhoneNo);
        ChangeColour.changeColour(getApplicationContext(),txtPincode);
        ChangeColour.changeColour(getApplicationContext(),txtState);
        ChangeColour.changeColour(getApplicationContext(),txtCity);
        ChangeColour.changeColour(getApplicationContext(),txtHouseNo);
        ChangeColour.changeColour(getApplicationContext(),txtRoadName);
    }

    /*
            This Method Will Update The User Address In The Database.
     */

    public void updateAddress(UserAddress userAddress){
        HashMap<String,Object> hashMapUser=new HashMap<>();
        hashMapUser.put("fullName",userAddress.getFullName());
        hashMapUser.put("mobileNo",userAddress.getMobileNo());
        hashMapUser.put("alternateMobileNo",userAddress.getAlternateMobileNo());
        hashMapUser.put("pincode",userAddress.getPincode());
        hashMapUser.put("state",userAddress.getState());
        hashMapUser.put("city",userAddress.getCity());
        hashMapUser.put("houseNo",userAddress.getHouseNo());
        hashMapUser.put("roadName",userAddress.getRoadName());
        hashMapUser.put("addressType",userAddress.getAddressType());
        hashMapUser.put("userId",userAddress.getUserId());
        Query query=userAddressRef.orderByChild("userId").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("UserAddress").child(userSnapshot.getKey());
                databaseReference.setValue(hashMapUser);
                startActivity(new Intent(getApplicationContext(),add_to_cart_product.class));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /*
         This Method will Show The User Address When User Trying To Update
         The address In AddToCart Activity.

     */

    public void showUserAddress(){
        if(status.equals("update")){
            Query addressQuery=userAddressRef.orderByChild("userId").equalTo(user.getUid());
            addressQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    userAddress = userSnapshot.getValue(UserAddress.class);
                    txtName.setText(userAddress.getFullName());
                    txtPhoneNo.setText(userAddress.getMobileNo());
                    txtAlternatePhoneNo.setText(userAddress.getAlternateMobileNo());
                    txtPincode.setText(userAddress.getPincode());
                    txtState.setText(userAddress.getState());
                    txtCity.setText(userAddress.getCity());
                    txtHouseNo.setText(userAddress.getHouseNo());
                    txtRoadName.setText(userAddress.getRoadName());
                    if(userAddress.getAddressType().equals("Work")){
                        radioButtonWork.setChecked(true);
                    } else if (userAddress.getAddressType().equals("Home")) {
                        radioButtonHome.setChecked(true);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{

        }
    }


    /*
            This Method Validate User Data Is Valid or Not
            if the Data is Valid then It Will Save to Database
            OtherWise It Show Some Error.
     */
    public boolean validInput(UserAddress userAddress){
        if(TextUtils.isEmpty(userAddress.getFullName())){
            ChangeColour.errorColour(getApplicationContext(),txtName,"This Filed Is Required");
            return false;
        }
        if (TextUtils.isEmpty(userAddress.getMobileNo())){
            ChangeColour.errorColour(getApplicationContext(),txtPhoneNo,"This Filed Is Required");
            return false;
        } else if (!Pattern.isValidMobileNumber(userAddress.getMobileNo())) {
            ChangeColour.errorColour(getApplicationContext(),txtPhoneNo,"Invalid Mobile Number");
            return false;
        }
        if (TextUtils.isEmpty(userAddress.getPincode())){
            ChangeColour.errorColour(getApplicationContext(),txtPincode,"This Filed Is Required");
            return false;
        }
        if (TextUtils.isEmpty(userAddress.getState())){
            ChangeColour.errorColour(getApplicationContext(),txtState,"This Filed Is Required");
            return false;
        }
        if (TextUtils.isEmpty(userAddress.getCity())){
            ChangeColour.errorColour(getApplicationContext(),txtCity,"This Filed Is Required");
            return false;
        }
        if (TextUtils.isEmpty(userAddress.getHouseNo())){
            ChangeColour.errorColour(getApplicationContext(),txtHouseNo,"This Filed Is Required");
            return false;
        }
        if (TextUtils.isEmpty(userAddress.getRoadName())) {
            ChangeColour.errorColour(getApplicationContext(), txtRoadName, "This Filed Is Required");
            return false;
        }
        return  true;
    }

}