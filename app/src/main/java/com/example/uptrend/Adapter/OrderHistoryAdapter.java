package com.example.uptrend.Adapter;

import android.content.Context;
import android.content.res.Configuration;
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

import DataModel.Order;
import DataModel.Product;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{
    private Context context;
    private ArrayList<Order> orderArrayList;
    private OrderOnClick onclick;

    public OrderHistoryAdapter(Context context, ArrayList<Order> orderArrayList, OrderOnClick onclick) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.history_product,parent,false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order=orderArrayList.get(position);
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference("Product").child(order.getProductId());
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

        updateTrackingDate(order.getOrderDate(),order.getNodeId(),order.getShipingDate(),order.getDelliveryDate(),order.getNodeId());


        holder.txtOrderDate.setText(DateAndTime.convertDateFormatOrder(order.getOrderDate()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.onClickItem(order.getNodeId());
            }
        });



    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }


    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView productImage;
        TextView txtBrandName,txtProductName,txtOrderDate;
        CardView cardView;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.historyProductImage);
            txtBrandName=itemView.findViewById(R.id.orderBrandName);
            txtProductName=itemView.findViewById(R.id.productNAme);
            txtOrderDate=itemView.findViewById(R.id.orderDate);
            cardView=itemView.findViewById(R.id.cardViewHistoryContainerHI);
        }
    }



    public void updateTrackingDate(String orderDate,String orderID,String date,String date2,String nodeId){
        DateTimeFormatter formatter = null;
        LocalDate parsedDate = null;
        LocalDate shipingDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            parsedDate = LocalDate.parse(orderDate, formatter);
            shipingDate = parsedDate.plusDays(2);
            LocalDate deliveryDate=parsedDate.plusDays(4);
            String shipingDay = shipingDate.format(formatter);
            String deliveryDay=deliveryDate.format(formatter);
            DatabaseReference shipingRef=FirebaseDatabase.getInstance().getReference("Order").child(orderID).child("shipingDate");
            DatabaseReference deliveryRef=FirebaseDatabase.getInstance().getReference("Order").child(orderID).child("delliveryDate");
            shipingRef.setValue(shipingDay);
            deliveryRef.setValue(deliveryDay);
            if(orderDate.equals(DateAndTime.getDate())){
                DatabaseReference orderStatusRef= FirebaseDatabase.getInstance().getReference("Order").child(nodeId).child("orderStatus");
                orderStatusRef.setValue("new");
            }
            if(date.equals(DateAndTime.getDate())){
                DatabaseReference orderStatusRef= FirebaseDatabase.getInstance().getReference("Order").child(nodeId).child("orderStatus");
                orderStatusRef.setValue("shiping");

            }
            if(date2.equals(DateAndTime.getDate())){
                DatabaseReference orderStatusRef= FirebaseDatabase.getInstance().getReference("Order").child(nodeId).child("orderStatus");
                orderStatusRef.setValue("delivered");
            }
        }
    }

}

