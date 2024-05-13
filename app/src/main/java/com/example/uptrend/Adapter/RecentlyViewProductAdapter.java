package com.example.uptrend.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import DataModel.Product;
import DataModel.RecentlyViewProduct;

public class RecentlyViewProductAdapter extends RecyclerView.Adapter<RecentlyViewProductAdapter.RecentlyViewProductViewHolder>{
    private Context context;

    private ArrayList<RecentlyViewProduct> recentlyViewProductArrayList;

    private Onclick onclick;

    public RecentlyViewProductAdapter(Context context, ArrayList<RecentlyViewProduct> recentlyViewProductArrayList, Onclick onclick) {
        this.context = context;
        this.recentlyViewProductArrayList = recentlyViewProductArrayList;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public RecentlyViewProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recently_view_product_rv,parent,false);
        return new RecentlyViewProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyViewProductViewHolder holder, int position) {
        String  productId=recentlyViewProductArrayList.get(position).getProductId();
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference("Product").child(productId);
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()) {
                   Product product = snapshot.getValue(Product.class);
                   Glide.with(context).load(product.getProductImages().get(0)).into(holder.productImage);
                   holder.txtProductName.setText(product.getProductName());
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.productContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.ItemOnClickListener(productId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recentlyViewProductArrayList.size();
    }

    public class RecentlyViewProductViewHolder extends RecyclerView.ViewHolder{
        TextView txtProductName;
        ShapeableImageView productImage;
        LinearLayout productContainer;

        public RecentlyViewProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName=itemView.findViewById(R.id.productName);
            productImage=itemView.findViewById(R.id.productImage);
            productContainer=itemView.findViewById(R.id.productContainer);
        }
    }
}
