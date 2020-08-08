package com.hanyghazal.myecommerceapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CartViewHolder extends RecyclerView.ViewHolder{
    ImageView imageViewProduct;
    TextView txtProdName, txtPrice, txtQty, txtItemTotal;
    Button btnIncrease, btnDecrease;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        identifyLayout();
    }

    private void identifyLayout() {
        imageViewProduct = itemView.findViewById(R.id.single_cart_imageview_product);
        txtProdName = itemView.findViewById(R.id.single_cart_textview_prod_name);
        txtPrice = itemView.findViewById(R.id.single_cart_textview_prod_price);
        txtQty = itemView.findViewById(R.id.single_cart_text_view_qty);
        txtItemTotal = itemView.findViewById(R.id.single_cart_textview_item_total);
        btnIncrease = itemView.findViewById(R.id.button_increase);
        btnDecrease = itemView.findViewById(R.id.button_decrease);

    }



}
