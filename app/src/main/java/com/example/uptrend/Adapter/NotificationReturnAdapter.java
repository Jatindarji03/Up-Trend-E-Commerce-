package com.example.uptrend.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

import DataModel.Product;
import DataModel.Return;

public class NotificationReturnAdapter extends RecyclerView.Adapter<NotificationReturnAdapter.NotificationReturnViewHolder> {

    private Context context;
    private ArrayList<Return> returnArrayList;


    public NotificationReturnAdapter(Context context, ArrayList<Return> returnArrayList) {
        this.context = context;
        this.returnArrayList = returnArrayList;
    }

    @NonNull
    @Override
    public NotificationReturnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_return, parent, false);
        return new NotificationReturnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationReturnViewHolder holder, int position) {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference("Product").child(returnArrayList.get(position).getProductId());
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
        holder.txtProductPrice.setText(returnArrayList.get(position).getProductSellingPrice());
        holder.txtOrderDate.setText(returnArrayList.get(position).getReturnDate());
        holder.txtTime.setText(returnArrayList.get(position).getReturnTime());
    }

    @Override
    public int getItemCount() {
        return returnArrayList.size();
    }


    public class NotificationReturnViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView productImageView;
        TextView txtProductName, txtOrderDate, txtProductPrice, txtTime;

        public NotificationReturnViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.historyProductImageNoReturn);
            txtProductName = itemView.findViewById(R.id.productNAmeNoReturn);
            txtOrderDate = itemView.findViewById(R.id.orderDateNOReturn);
            txtTime = itemView.findViewById(R.id.ordertimeNOREturn);
            txtProductPrice = itemView.findViewById(R.id.productPriceNotificationReturn);
        }
    }
}
