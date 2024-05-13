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
import com.example.uptrend.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import DataModel.Product;
import DataModel.Review;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.SearchProductViewHolder> {
    private Context context;
    int totalReview = 0;
    float rating, total;
    private ArrayList<Product> searchProductArrayList;
    private Onclick onclick;

    public SearchProductAdapter(Context context, ArrayList<Product> searchProductArrayList, Onclick onclick) {
        this.context = context;
        this.searchProductArrayList = searchProductArrayList;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public SearchProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_product, parent, false);
        return new SearchProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProductViewHolder holder, int position) {
        Product product = searchProductArrayList.get(position);
        Glide.with(context).load(product.getProductImages().get(0)).into(holder.productImage);
        holder.txtBrandName.setText(product.getProductBrandName());
        holder.txtProductName.setText(product.getProductName());
        holder.txtSellingPrice.setText(product.getSellingPrice());
        holder.txtOriginalPrice.setText(product.getOriginalPrice());

        Double discount = calculateDiscountPercentage(Double.parseDouble(product.getOriginalPrice()),
                Double.parseDouble(product.getSellingPrice()));
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedDiscountPercentage = df.format(discount);
        holder.txtDiscountPer.setText(formattedDiscountPercentage);


        DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Review");
        Query reviewQuery = reviewRef.orderByChild("productId").equalTo(product.getProductId());
        reviewQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total = 0;
                rating = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    totalReview = (int) snapshot.getChildrenCount();
                    Review review = dataSnapshot.getValue(Review.class);
                    rating = Float.parseFloat(review.getProductStar());
                    total += rating;
                }
                if (rating == 0) {
                    holder.txtRating.setText("0.0");
                } else {
                    holder.txtRating.setText(String.valueOf(total / totalReview));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.cardViewHistoryContainerSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.ItemOnClickListener(product.getProductId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return searchProductArrayList.size();
    }


    public class SearchProductViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView productImage;
        TextView txtBrandName, txtProductName, txtSellingPrice, txtOriginalPrice, txtDiscountPer, txtRating;
        CardView cardViewHistoryContainerSearch;

        public SearchProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.historyProductImageSearch);
            txtBrandName = itemView.findViewById(R.id.orderBrandNameNO);
            txtProductName = itemView.findViewById(R.id.productNAmeNo);
            txtSellingPrice = itemView.findViewById(R.id.product_selling_price_Search);
            txtOriginalPrice = itemView.findViewById(R.id.product_original_price_Search);
            txtDiscountPer = itemView.findViewById(R.id.txtDiscount01);
            cardViewHistoryContainerSearch = itemView.findViewById(R.id.cardViewHistoryContainerSearch);
            txtRating = itemView.findViewById(R.id.txtTotalRatingSearch);
        }

    }

    public double calculateDiscountPercentage(double originalPrice, double sellingPrice) {
        double discount = originalPrice - sellingPrice;
        return (discount / originalPrice) * 100;
    }
}
