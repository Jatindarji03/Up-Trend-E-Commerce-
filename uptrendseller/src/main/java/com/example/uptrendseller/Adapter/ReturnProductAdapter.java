package com.example.uptrendseller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adapteranddatamodel.DateAndTime;
import com.example.uptrendseller.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.CancelProduct;
import DataModel.Product;
import DataModel.Return;
import DataModel.UserAddress;

public class ReturnProductAdapter extends RecyclerView.Adapter<ReturnProductAdapter.ReturnProductViewHolder> {
    private Context context;
    private ArrayList<Return> returnArrayList;
    private RequestOnClick requestOnClick;

    public ReturnProductAdapter(Context context, ArrayList<Return> returnArrayList, RequestOnClick requestOnClick) {
        this.context = context;
        this.returnArrayList = returnArrayList;
        this.requestOnClick = requestOnClick;
    }

    @NonNull
    @Override
    public ReturnProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cancel_and_return,parent,false);
        return new ReturnProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnProductViewHolder holder, int position) {
        Return returnProduct=returnArrayList.get(position);
        DatabaseReference userAddressRef = FirebaseDatabase.getInstance().getReference("UserAddress");
        Query userQuery = userAddressRef.orderByChild("userId").equalTo(returnProduct.getUserId());

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    UserAddress userAddress = userSnapshot.getValue(UserAddress.class);
                    holder.txtUserName.setText(userAddress.getFullName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(returnProduct.getProductId());
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
                    holder.txtProductName.setText(product.getProductName());
                    Glide.with(context).load(product.getProductImages().get(0)).into(holder.productImage);
                    int qty = Integer.parseInt(returnProduct.getProductQty());
                    long price = Long.parseLong(product.getSellingPrice());
                    holder.txtPrice.setText(String.valueOf(qty * price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.txtQty.setText(returnProduct.getProductQty());
        holder.txtDate.setText(returnProduct.getReturnDate());
        holder.txtStatus.setText("Return");


        updateTrackingStatus(returnProduct.getNodeId(),returnProduct.getReturnDate(),returnProduct.getPickupDate(),returnProduct.getRefundDate());

        holder.cardViewContainerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestOnClick.RequestOnClickListener(
                        returnProduct.getNodeId(),
                        returnProduct.getProductId(),
                        returnProduct.getUserId(),
                        returnProduct.getProductQty(),
                        returnProduct.getProductSize()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return returnArrayList.size();
    }

    public class ReturnProductViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView productImage;
        CardView cardViewContainerCancel;
        TextView txtProductName, txtQty, txtPrice, txtUserName, txtStatus, txtDate;

        public ReturnProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image_CR);
            txtProductName = itemView.findViewById(R.id.product_name_CR);
            txtQty = itemView.findViewById(R.id.product_qty_CR);
            txtPrice = itemView.findViewById(R.id.productTotalPriceCR);
            txtUserName = itemView.findViewById(R.id.txtUserNameCR);
            txtStatus = itemView.findViewById(R.id.productStatusCR);
            txtDate = itemView.findViewById(R.id.orderDateCR);
            cardViewContainerCancel = itemView.findViewById(R.id.cardViewContainerCancel);
        }
    }

    public void updateTrackingStatus(String nodeId,String returnDate,String pickUpDate,String refundDay){
        DatabaseReference returnStatusRef=FirebaseDatabase.getInstance().getReference("Return").child(nodeId).child("returnStatus");
        if(returnDate.equals(DateAndTime.getDate())){
            returnStatusRef.setValue("return");
        } else if (pickUpDate.equals(DateAndTime.getDate())) {
            returnStatusRef.setValue("pickup");
        } else if (refundDay.equals(DateAndTime.getDate())) {
            returnStatusRef.setValue("refund");
        }
    }
}
