package com.example.uptrendseller.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uptrendseller.R;
import com.example.uptrendseller.edit_product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theophrast.ui.widget.SquareImageView;

import java.util.ArrayList;

import DataModel.Product;
import de.hdodenhof.circleimageview.CircleImageView;

public class InventoryProductAdapter extends RecyclerView.Adapter<InventoryProductAdapter.ProductViewHolder>{
    private Context context;
    private ArrayList<Product> productArrayList;
    private Onclick onclick;

    public InventoryProductAdapter(Context context, ArrayList<Product> productArrayList, Onclick onclick) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.raw_add_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product=productArrayList.get(position);
        Glide.with(context).load(product.getProductImages().get(0)).into(holder.productImage);
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(product.getSellingPrice());
        holder.productStock.setText(product.getTotalStock());

        // deleteIng product

        holder.productDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deleteDialog(product.getProductId());

            }
        });

        holder.productEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.ItemOnClickListener(product.getProductId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView productImage;
        TextView productName,productPrice,productStock,productEdit,productDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image_RV);
            productName=itemView.findViewById(R.id.product_name_RV);
            productPrice=itemView.findViewById(R.id.product_price_RV);
            productStock=itemView.findViewById(R.id.product_stock_RV);
            productEdit=itemView.findViewById(R.id.product_edit_RV);
            productDelete=itemView.findViewById(R.id.product_delete_RV);

        }
    }
    public void deleteDialog(String  productId){
        AlertDialog.Builder deleteDialog=new AlertDialog.Builder(context);
        deleteDialog.setTitle("Delete Product?");
        deleteDialog.setIcon(R.drawable.delete_vector);
        deleteDialog.setMessage("Are you sure you want to delete this item?");
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteProduct(productId);
            }
        });
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        deleteDialog.show();
    }
    private void deleteProduct(String productId){
        DatabaseReference productNode= FirebaseDatabase.getInstance().getReference("Product").child(productId);
        productNode.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                notifyDataSetChanged();
                Toast.makeText(context, "Product Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
