package com.hanyghazal.myecommerceapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductViewHolder extends ViewHolder {
    ImageView imageViewProduct;
    TextView txtProductName, txtProductPrice;
    ImageButton imgBtnAddToWishList, imgBtnAddToCart;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewProduct = itemView.findViewById(R.id.single_product_image_view);
        txtProductName = itemView.findViewById(R.id.single_product_text_view_name);
        txtProductPrice = itemView.findViewById(R.id.single_product_text_view_price);
        imgBtnAddToWishList = itemView.findViewById(R.id.single_product_wishlist_icon);
        imgBtnAddToCart = itemView.findViewById(R.id.single_product_cart_icon);

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//       view = parent.getSelectedView();
//
//    }
}
