package com.hanyghazal.myecommerceapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    TextView txtProductName, txtOrderItemTotal, txtOrderItemQty, txtOrderItemPrice, txtOrderItemSeller, txtOrderItemNum;
    ImageView imageView;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.text_view_order_item_name);
        txtOrderItemPrice = itemView.findViewById(R.id.text_view_order_item_price);
        txtOrderItemTotal = itemView.findViewById(R.id.text_view_order_item_total);
        txtOrderItemQty = itemView.findViewById(R.id.text_view_order_item_qty);
        txtOrderItemSeller = itemView.findViewById(R.id.text_view_order_item_seller);
        txtOrderItemNum = itemView.findViewById(R.id.text_view_order_item_num);
        imageView = itemView.findViewById(R.id.image_view_order_item);

    }
}
