package com.example.uptrend.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uptrend.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.Order;
import DataModel.Product;
import DataModel.Review;
import io.github.muddz.styleabletoast.StyleableToast;

public class RatingProductAdapter extends RecyclerView.Adapter<RatingProductAdapter.RatingProductViewHolder> {
    private Context context;
    private ArrayList <Order> orderArrayList;

    public RatingProductAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public RatingProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.reviews_product,parent,false);
        return new RatingProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingProductViewHolder holder, int position) {
        Order order=orderArrayList.get(position);
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference("Product").child(order.getProductId());
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Product product=snapshot.getValue(Product.class);
                    holder.txtProductName.setText(product.getProductName());
                    holder.txtBrandName.setText(product.getProductBrandName());
                    Glide.with(context).load(product.getProductImages().get(0)).into(holder.productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Review");
        Query query = reviewRef.orderByChild("userId").equalTo(order.getUserId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean foundReview = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Review dataReview = dataSnapshot.getValue(Review.class);
                    if (dataReview.getProductId().equals(order.getProductId())) {
                        holder.ratingBar.setRating(Float.parseFloat(dataReview.getProductStar()));
                        foundReview = true;

                    }
                }
                if (!foundReview) {
                    holder.ratingBar.setRating(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reviewRef=FirebaseDatabase.getInstance().getReference("Review");
                Query userQuery=reviewRef.orderByChild("userId").equalTo(order.getUserId());
                userQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean reviewFound=false;
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Review review=dataSnapshot.getValue(Review.class);
                            if(review.getProductId().equals(order.getProductId())){
                                DatabaseReference reference=dataSnapshot.getRef().child("productStar");
                                reference.setValue(String.valueOf(holder.ratingBar.getRating()));
                                StyleableToast.makeText(context,"You have suucessfully rating this product",R.style.UptrendToast).show();

                                reviewFound=true;
                            }
                        }
                        if(!reviewFound){
                            Review review = new Review();
                            review.setUserId(order.getUserId());
                            review.setProductId(order.getProductId());
                            review.setProductStar(String.valueOf(holder.ratingBar.getRating()));
                            DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Review");
                            reviewRef.push().setValue(review);
                            StyleableToast.makeText(context,"You have suucessfully rating this product",R.style.UptrendToast).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class RatingProductViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView productImage;
        TextView txtProductName,txtBrandName;
        RatingBar ratingBar;
        AppCompatButton btnSubmit;

        public RatingProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImageA2C);
            txtProductName=itemView.findViewById(R.id.productNameRA);
            txtBrandName=itemView.findViewById(R.id.productBrandNameRA);
            ratingBar=itemView.findViewById(R.id.productReview);
            btnSubmit=itemView.findViewById(R.id.submit_rating_btn);
        }
    }

}
