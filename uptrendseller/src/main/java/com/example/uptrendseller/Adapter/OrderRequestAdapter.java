package com.example.uptrendseller.Adapter;

import android.content.Context;
import android.util.Log;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import DataModel.Order;
import DataModel.Product;
import DataModel.UserAddress;

public class OrderRequestAdapter extends RecyclerView.Adapter<OrderRequestAdapter.OrderRequestViewHolder> {
    private Context context;
    private ArrayList<Order> orderArrayList;
    private RequestOnClick requestOnClick;

    public OrderRequestAdapter(Context context, ArrayList<Order> orderArrayList, RequestOnClick requestOnClick) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        this.requestOnClick = requestOnClick;
    }

    @NonNull
    @Override
    public OrderRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inventory_product_rv, parent, false);
        return new OrderRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRequestViewHolder holder, int position) {
        Order order = orderArrayList.get(position);

        DatabaseReference userAddressRef = FirebaseDatabase.getInstance().getReference("UserAddress");
        Query userQuery = userAddressRef.orderByChild("userId").equalTo(order.getUserId());

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
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(order.getProductId());
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
                    holder.txtProductName.setText(product.getProductName());
                    Glide.with(context).load(product.getProductImages().get(0)).into(holder.productImage);
                    int qty = Integer.parseInt(order.getProductQty());
                    long price = Long.parseLong(product.getSellingPrice());
                    holder.txtProductPrice.setText(String.valueOf(qty * price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.txtQuntity.setText(order.getProductQty());
        holder.orderDate.setText(order.getOrderDate());
        if(order.getOrderStatus().equals("new")){
            holder.productStatus.setText("Processing");
            
        } else if (order.getOrderStatus().equals("shiping")) {
            holder.productStatus.setText("Shiping");
        } else if (order.getOrderStatus().equals("delivered")) {
            holder.deliveredIcon.setVisibility(View.VISIBLE);
            holder.productStatus.setText("Delivered");
            
        }
        holder.cardViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestOnClick.RequestOnClickListener(
                        order.getNodeId(),
                        order.getProductId(),
                        order.getUserId(),
                        order.getProductQty(),
                        order.getProductSize()
                );
            }
        });

        updateTrackingDate(order.getOrderDate(),order.getNodeId(),order.getShipingDate(),order.getDelliveryDate(),order.getNodeId());


    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class OrderRequestViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView productImage;
        TextView txtProductName, txtProductPrice, txtUserName, txtQuntity, productStatus, orderDate, deliveredIcon;
        CardView cardViewContainer;

        public OrderRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image_RV);
            txtProductName = itemView.findViewById(R.id.product_name_RV);
            txtProductPrice = itemView.findViewById(R.id.productTotalPrice);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtQuntity = itemView.findViewById(R.id.product_qty);
            cardViewContainer = itemView.findViewById(R.id.cardViewContainer);
            productStatus = itemView.findViewById(R.id.productStatus);
            orderDate = itemView.findViewById(R.id.orderDate);
            deliveredIcon = itemView.findViewById(R.id.deliveredIcon);
        }
    }

    public String getProductSize(String category, String index) {
        String size = "";
        if (category.equals("Men's(Top)") || category.equals("Women's(Top)")) {
            if (index.equals("0")) size = "S";
            else if (index.equals("1")) size = "M";
            else if (index.equals("2")) size = "L";
            else if (index.equals("3")) size = "XL";
            else if (index.equals("4")) size = "XXL";
        } else if (category.equals("Men's(Bottom)") || category.equals("Women's(Bottom)")) {
            if (index.equals("0")) size = "28";
            else if (index.equals("1")) size = "30";
            else if (index.equals("2")) size = "32";
            else if (index.equals("3")) size = "34";
            else if (index.equals("4")) size = "36";
            else if (index.equals("5")) size = "38";
            else if (index.equals("6")) size = "40";

        } else if (category.equals("Footware(Men)") || category.equals("Footware(Women)")) {
            if (index.equals("0")) size = "6";
            else if (index.equals("1")) size = "7";
            else if (index.equals("2")) size = "8";
            else if (index.equals("3")) size = "9";
            else if (index.equals("3")) size = "10";
        } else {
            size = "no";
        }
        return size;
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
