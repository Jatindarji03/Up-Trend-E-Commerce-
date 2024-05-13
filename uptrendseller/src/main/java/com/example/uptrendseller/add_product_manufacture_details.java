package com.example.uptrendseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.github.muddz.styleabletoast.StyleableToast;

public class add_product_manufacture_details extends AppCompatActivity {
    EditText txtGenericName,txtManufactureName,txtPackerDetails;
    TextView txtSaveDetails;
    private DatabaseReference databaseReference;
    private String key;
    private loadingDialog2 loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_manufacture_details);

        //Finding Id of Every Widget
        txtGenericName=findViewById(R.id.generic_name);
        txtManufactureName=findViewById(R.id.manufacture_details);
        txtPackerDetails=findViewById(R.id.Packers_details);
        txtSaveDetails=findViewById(R.id.save_manufacture_txt);
        key=getIntent().getStringExtra("Key");
        databaseReference= FirebaseDatabase.getInstance().getReference("Product").child(key);

        loading=new loadingDialog2(add_product_manufacture_details.this);


        //Saving The Data
        txtSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidInput()){
                    loading.show();
                    HashMap<String,Object> product=new HashMap<>();
                    product.put("productGenericName",txtGenericName.getText().toString());
                    product.put("productManufactureDetails",txtManufactureName.getText().toString());
                    product.put("productPackerDetail",txtPackerDetails.getText().toString());
                    databaseReference.updateChildren(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete()){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        StyleableToast.makeText(getApplicationContext(),"Product Manufacture Details Saved Successfully",R.style.UptrendToast).show();
                                        startActivity(new Intent(getApplicationContext(), inventory_product.class));
                                        finish();

                                    }
                                },2000);
                            }
                        }
                    });
                }
            }
        });

    ChangeColour.changeColour(getApplicationContext(),txtGenericName);
    ChangeColour.changeColour(getApplicationContext(),txtManufactureName);
    ChangeColour.changeColour(getApplicationContext(),txtPackerDetails);
    }

    /*
    `   This Method Will Check The EditText Is Empty or not
     */
    public boolean isValidInput(){
        if(TextUtils.isEmpty(txtGenericName.getText().toString())){
            ChangeColour.errorColour(getApplicationContext(),txtGenericName,"This Filed Is Required");
            return false;
        }
        if(TextUtils.isEmpty(txtManufactureName.getText().toString())){
            ChangeColour.errorColour(getApplicationContext(),txtManufactureName,"This Filed Is Required");
            return false;
        }
        if(TextUtils.isEmpty(txtPackerDetails.getText().toString())){
            ChangeColour.errorColour(getApplicationContext(),txtPackerDetails,"This Filed Is Required");
            return false;
        }
        return true;
    }


}