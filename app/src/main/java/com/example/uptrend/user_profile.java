package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import DataModel.User;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

public class user_profile extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private DatabaseReference userRef;
    private Query userQuery;
    private User user;
    private CircleImageView selectImageView, maleImageView, femaleImageView;
    EditText txtUserName, txtMobileNumber;
    TextView txtEmail, txtSelectImage;
    AppCompatButton btnUpdate;
    private String image = null,gender=null;
    RadioGroup radioGroupGender;
    RadioButton radioButtonMale,radioButtonFemale;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //findViewById of All Widget.

        btnUpdate = findViewById(R.id.btnUpdate);
        selectImageView = findViewById(R.id.selectImage);
        maleImageView = findViewById(R.id.maleImage);
        femaleImageView = findViewById(R.id.femaleImage);
        txtEmail = findViewById(R.id.userEmail);
        txtUserName = findViewById(R.id.userName);
        txtMobileNumber = findViewById(R.id.userMobileNo);
        txtSelectImage = findViewById(R.id.txtSelectImage);
        radioGroupGender=findViewById(R.id.radioGroupGender);
        radioButtonMale=findViewById(R.id.radioButtonMale);
        radioButtonFemale=findViewById(R.id.radioButtonFemale);

        //Getting Current User

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = new User();

        displayUserDetails();


        txtSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageView.setImageResource(R.drawable.person2);
                maleImageView.setVisibility(View.VISIBLE);
                femaleImageView.setVisibility(View.VISIBLE);
            }
        });
        maleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleImageView.setVisibility(View.GONE);
                femaleImageView.setVisibility(View.GONE);
                selectImageView.setImageResource(R.drawable.logo_men);
                user.setProfileImage("male");
                //image = "male";
            }
        });
        femaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleImageView.setVisibility(View.GONE);
                maleImageView.setVisibility(View.GONE);
                selectImageView.setImageResource(R.drawable.logo_female);
                user.setProfileImage("female");
                //image = "female";
            }
        });
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id==R.id.radioButtonMale){
                    gender="Male";
                } else if (id==R.id.radioButtonFemale) {
                    gender="Female";
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.setUserId(firebaseUser.getUid());
                user.setUserName(txtUserName.getText().toString().trim());
                user.setUserMobileNumber(txtMobileNumber.getText().toString().trim());
                user.setUserEmail(txtEmail.getText().toString());
                user.setProfileImage(user.getProfileImage());
                user.setUserGender(gender);
                profileUpdate(user);
            }
        });

    }

    public void profileUpdate(User user) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userName", user.getUserName());
        userMap.put("userMobileNumber", user.getUserMobileNumber());
        userMap.put("userId", user.getUserId());
        userMap.put("userEmail", user.getUserEmail());
        userMap.put("profileImage", user.getProfileImage());
        userMap.put("userGender",user.getUserGender());
        userRef = FirebaseDatabase.getInstance().getReference("User");
        userQuery = userRef.orderByChild("userId").equalTo(user.getUserId());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                DatabaseReference updateRef = userSnapshot.getRef();
                updateRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        StyleableToast.makeText(getApplicationContext(),"Profile Updated Successfully",R.style.UptrendToast).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
            It Will Display User Details To the User
     */

    public void displayUserDetails() {
        userRef = FirebaseDatabase.getInstance().getReference("User");
        userQuery = userRef.orderByChild("userId").equalTo(firebaseUser.getUid());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    user = userSnapshot.getValue(User.class);
                    txtUserName.setText(user.getUserName());
                    txtEmail.setText(user.getUserEmail());
                    txtMobileNumber.setText(user.getUserMobileNumber());
                    if (user.getProfileImage() != null) {

                        if (user.getProfileImage().equals("male")) {
                            selectImageView.setImageResource(R.drawable.logo_men);
                        } else if (user.getProfileImage().equals("female")) {
                            selectImageView.setImageResource(R.drawable.logo_female);
                        }
                    }else{
                        selectImageView.setImageResource(R.drawable.person2);

                    }
                    if(user.getUserGender()!=null){
                        if(user.getUserGender().equals("Male")){
                            radioButtonMale.setChecked(true);
                        } else if (user.getUserGender().equals("Female")) {
                            radioButtonFemale.setChecked(true);
                        }
                    }else{
                        radioButtonFemale.setChecked(false);
                        radioButtonMale.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}