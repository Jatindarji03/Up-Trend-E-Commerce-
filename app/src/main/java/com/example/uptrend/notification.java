package com.example.uptrend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.uptrend.Adapter.NotificationAdapter;
import com.example.uptrend.Adapter.NotificationReturnAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.CancelProduct;
import DataModel.Order;
import DataModel.Return;

public class notification extends AppCompatActivity {
    RecyclerView recyclerViewNotification,recyclerViewReturn;
    ArrayList<CancelProduct> cancelArrayList;
ArrayList<Return>  returnArrayList;

    DatabaseReference cancelRef,returnRef;
    FirebaseUser firebaseUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerViewNotification=findViewById(R.id.recyclerViewNotification);
        recyclerViewReturn=findViewById(R.id.recyclerViewReturn);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
       displayProduct(firebaseUser.getUid());
       displayReturnProduct(firebaseUser.getUid());
    }

    public void displayProduct(String userId){
        cancelArrayList =new ArrayList<>();
        cancelRef = FirebaseDatabase.getInstance().getReference("Cancel");
        Query userQuery= cancelRef.orderByChild("userId").equalTo(userId);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cancelArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    CancelProduct cancelProduct=dataSnapshot.getValue(CancelProduct.class);
                        cancelArrayList.add(cancelProduct);
                }
                NotificationAdapter notificationAdapter=new NotificationAdapter(notification.this, cancelArrayList);
                LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                recyclerViewNotification.setLayoutManager(layoutManager);
                recyclerViewNotification.setAdapter(notificationAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayReturnProduct(String userId){
        returnArrayList=new ArrayList<>();
        returnRef=FirebaseDatabase.getInstance().getReference("Return");
        Query userQuery= returnRef.orderByChild("userId").equalTo(userId);
        userQuery.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            returnArrayList.clear();
                            Return returnProduct=dataSnapshot.getValue(Return.class);
                            returnArrayList.add(returnProduct);
                        }
                        NotificationReturnAdapter notificationAdapter=new NotificationReturnAdapter(notification.this, returnArrayList);
                        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        recyclerViewReturn.setLayoutManager(layoutManager);
                        recyclerViewReturn.setAdapter(notificationAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
}