package com.hanyghazal.myecommerceapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductViewHolder extends ViewHolder {
    ImageView imageViewProduct;
    TextView txtProductName, txtProductPrice;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewProduct = itemView.findViewById(R.id.single_product_image_view);
        txtProductName = itemView.findViewById(R.id.single_product_text_view_name);
        txtProductPrice = itemView.findViewById(R.id.single_product_text_view_price);

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//       view = parent.getSelectedView();
//
//    }
}
