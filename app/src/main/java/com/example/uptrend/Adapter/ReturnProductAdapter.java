package com.example.uptrend.Adapter;

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
import com.example.uptrend.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import DataModel.Product;
import DataModel.Return;

public class ReturnProductAdapter extends RecyclerView.Adapter<ReturnProductAdapter.ReturnProductHolder>{
    private Context context;
    private ArrayList<Return> returnArrayList;
    private ReturnOnClick returnOnClick;

    public ReturnProductAdapter(Context context, ArrayList<Return> returnArrayList, ReturnOnClick returnOnClick) {
        this.context = context;
        this.returnArrayList = returnArrayList;
        this.returnOnClick = returnOnClick;
    }

    @NonNull
    @Override
    public ReturnProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.history_product,parent,false);
        return new ReturnProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnProductHolder holder, int position) {
        Return returnProduct=returnArrayList.get(position);
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference("Product").child(returnProduct.getProductId());
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Product product=snapshot.getValue(Product.class);
                    Glide.with(context).load(product.getProductImages().get(0)).into(holder.productImage);
                    holder.txtBrandName.setText(product.getProductBrandName());
                    holder.txtProductName.setText(product.getProductName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtStatus.setText("Return on ");
        holder.txtReturnDate.setText(DateAndTime.convertDateFormatOrder(returnProduct.getReturnDate()));
        updateTrackingStatus(
                returnProduct.getNodeId(),
                returnProduct.getReturnDate(),
                returnProduct.getPickupDate(),
                returnProduct.getRefundDate()
        );
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnOnClick.ReturnOnClickItem(returnProduct.getNodeId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return returnArrayList.size();
    }

    public class ReturnProductHolder extends RecyclerView.ViewHolder{
        ShapeableImageView productImage;
        TextView txtBrandName,txtProductName,txtReturnDate,txtStatus;
        CardView cardView;
        public ReturnProductHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.historyProductImage);
            txtBrandName=itemView.findViewById(R.id.orderBrandName);
            txtProductName=itemView.findViewById(R.id.productNAme);
            txtReturnDate=itemView.findViewById(R.id.orderDate);
            txtStatus=itemView.findViewById(R.id.txtStatus);
            cardView=itemView.findViewById(R.id.cardViewHistoryContainerHI);
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
//    public void updateTrackingDate(String returnDate,String orderID,String date,String date2,String nodeId){
////        DateTimeFormatter formatter = null;
////        LocalDate parsedDate = null;
////        LocalDate shipingDate = null;
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
////            parsedDate = LocalDate.parse(returnDate, formatter);
////            shipingDate = parsedDate.plusDays(2);
////            LocalDate deliveryDate=parsedDate.plusDays(4);
////            String shipingDay = shipingDate.format(formatter);
////            String deliveryDay=deliveryDate.format(formatter);
////            DatabaseReference shipingRef=FirebaseDatabase.getInstance().getReference("Order").child(orderID).child("shipingDate");
////            DatabaseReference deliveryRef=FirebaseDatabase.getInstance().getReference("Order").child(orderID).child("delliveryDate");
////            shipingRef.setValue(shipingDay);
////            deliveryRef.setValue(deliveryDay);
//            if(date.equals(DateAndTime.getDate())){
//                DatabaseReference returnStatusRef= FirebaseDatabase.getInstance().getReference("Return").child(nodeId).child("returnStatus");
//                returnStatusRef.setValue("pickup");
//            }
//            if(date2.equals(DateAndTime.getDate())){
//                DatabaseReference orderStatusRef= FirebaseDatabase.getInstance().getReference("Return").child(nodeId).child("returnStatus");
//                orderStatusRef.setValue("refund");
//            }
//        //}
//    }

}
