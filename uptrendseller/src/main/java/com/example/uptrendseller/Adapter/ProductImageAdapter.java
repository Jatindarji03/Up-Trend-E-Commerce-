package com.example.uptrendseller.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.uptrendseller.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder> {
    private ArrayList<Uri> uris;
    private ViewPager2 viewPager2;

    public ProductImageAdapter(ArrayList<Uri> uris, ViewPager2 viewPager2) {
        this.uris = uris;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_container,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.roundedImageView.setImageURI(uris.get(position));
//        if(position==uris.size()-2){
//            viewPager2.post(runnable);
//        }

    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView roundedImageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            roundedImageView=itemView.findViewById(R.id.imageView);
        }
    }
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            uris.addAll(uris);
            notifyDataSetChanged();
        }
    };
}
