package com.example.uptrend.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uptrend.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.CancelProduct;
import DataModel.Product;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    private Context context;
    private ArrayList<CancelProduct> cancelProductArrayList;

    public NotificationAdapter(Context context, ArrayList<CancelProduct> cancelProductArrayList) {
        this.context = context;
        this.cancelProductArrayList = cancelProductArrayList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification,parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference("Product").child(cancelProductArrayList.get(position).getProductId());
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Product product=snapshot.getValue(Product.class);
                    Glide.with(context).load(product.getProductImages().get(0)).into(holder.productImageView);
                    holder.txtProductName.setText(product.getProductName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtProductPrice.setText(cancelProductArrayList.get(position).getProductSellingPrice());
        holder.txtOrderDate.setText(cancelProductArrayList.get(position).getCancelDate());
        holder.txtTime.setText(cancelProductArrayList.get(position).getCancelTime());


    }

    @Override
    public int getItemCount() {
        return cancelProductArrayList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView productImageView;
        TextView txtProductName,txtOrderDate,txtProductPrice,txtTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView=itemView.findViewById(R.id.historyProductImageNO);
            txtProductName=itemView.findViewById(R.id.productNAmeNo);
            txtOrderDate=itemView.findViewById(R.id.orderDateNO);
            txtTime=itemView.findViewById(R.id.ordertimeNO);
            txtProductPrice=itemView.findViewById(R.id.productPriceNotification);
        }
    }
}
