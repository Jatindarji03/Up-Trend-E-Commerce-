package com.example.uptrend.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uptrend.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import DataModel.Cart;
import DataModel.Product;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private ArrayList<Cart> cartArrayList;
    private Onclick onclick;

    public CartAdapter(Context context, ArrayList<Cart> cartArrayList, Onclick onclick) {
        this.context = context;
        this.cartArrayList = cartArrayList;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_to_cart_container, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartArrayList.get(position);
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(cart.getProductId());
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
                    Glide.with(context).load(product.getProductImages().get(0)).into(holder.productImage);
                    holder.txtBrandName.setText(product.getProductBrandName());
                    holder.txtProductName.setText(product.getProductName());
                    holder.txtSellingPrice.setText(product.getSellingPrice());
                    holder.txtOriginalPrice.setText(product.getOriginalPrice());
                    Long discountAmount = (long) (Double.parseDouble(product.getOriginalPrice())
                            - Double.parseDouble(product.getSellingPrice()));
                    holder.txtDiscountPrice.setText(String.valueOf(discountAmount));
                    holder.txtQty.setText(cart.getQty());
                    DatabaseReference qtyRef = FirebaseDatabase.getInstance().getReference("Cart").child(cart.getCartId()).child("qty");
                    holder.txtPlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int qty = Integer.parseInt(holder.txtQty.getText().toString());

                            // Ensure product is not null before accessing its properties
                            if (product != null) {
                                // Check if product sizes are available
                                if (product.getProductSizes() != null && product.getProductSizes().size() != 0) {
                                    // Check if product size index is valid
                                    int productSizeIndex = Integer.parseInt(cart.getProductSize());
                                    if (productSizeIndex >= 0 && productSizeIndex < product.getProductSizes().size()) {
                                        int maxQuantity = Integer.parseInt(product.getProductSizes().get(productSizeIndex));
                                        if (qty < maxQuantity) {
                                            qty++;
                                            holder.txtQty.setText(String.valueOf(qty));
                                            qtyRef.setValue(String.valueOf(qty));
                                        }
                                    }
                                } else {
                                    // Check if total stock is available
                                    if (product.getTotalStock() != null) {
                                        int totalStock = Integer.parseInt(product.getTotalStock());
                                        if (qty < totalStock) {
                                            qty++;
                                            holder.txtQty.setText(String.valueOf(qty));
                                            qtyRef.setValue(String.valueOf(qty));
                                        }
                                    }
                                }
                            }
                        }
                    });

//                    holder.txtPlus.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            int qty = Integer.parseInt(holder.txtQty.getText().toString());
////                            qty++;
////                            holder.txtQty.setText(String.valueOf(qty));
////                            qtyRef.setValue(String.valueOf(qty));
//                            if (product.getProductSizes().size() != 0) {
//                                if (qty < Integer.parseInt(product.getProductSizes().get(Integer.parseInt(cart.getProductSize())))) {
//                                    qty++;
//                                    holder.txtQty.setText(String.valueOf(qty));
//                                    qtyRef.setValue(String.valueOf(qty));
//                                }
//                            } else {
//                                if (qty < Integer.parseInt(product.getTotalStock())) {
//                                    qty++;
//                                    holder.txtQty.setText(String.valueOf(qty));
//                                    qtyRef.setValue(String.valueOf(qty));
//                                }
//                            }
//
//                        }
//                    });
                    holder.txtMinus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int qty = Integer.parseInt(holder.txtQty.getText().toString());
                            if (qty > 1) {
                                qty--;
                                holder.txtQty.setText(String.valueOf(qty));
                                qtyRef.setValue(String.valueOf(qty));
                            }
                        }
                    });
                    holder.txtRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatabaseReference qtyRef = FirebaseDatabase.getInstance().getReference("Cart").child(cart.getCartId());
                            qtyRef.removeValue();
                            notifyDataSetChanged();
                        }
                    });
                    holder.layoutContainer1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onclick.ItemOnClickListener(product.getProductId());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView productImage;

        MaterialTextView txtBrandName, txtProductName, txtOriginalPrice, txtSellingPrice, txtDiscountPrice, txtQty;
        TextView txtPlus, txtMinus, txtRemove;
        LinearLayout layoutContainer1;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImageA2C);
            txtBrandName = itemView.findViewById(R.id.txtBrandNameA2C);
            txtProductName = itemView.findViewById(R.id.txtProductNameA2C);
            txtSellingPrice = itemView.findViewById(R.id.txt_product_selling_price_A2C);
            txtOriginalPrice = itemView.findViewById(R.id.text_product_original_price_A2C);
            txtDiscountPrice = itemView.findViewById(R.id.discount_A2C);
            txtQty = itemView.findViewById(R.id.txtQty);
            txtPlus = itemView.findViewById(R.id.txtPlus);
            txtMinus = itemView.findViewById(R.id.txtMinus);
            txtRemove = itemView.findViewById(R.id.delete_A2C);
            layoutContainer1 = itemView.findViewById(R.id.layoutContainer1);


        }
    }
}


