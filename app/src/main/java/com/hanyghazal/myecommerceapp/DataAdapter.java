package com.hanyghazal.myecommerceapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private Context context;
    private ArrayList<Cart> carts;

    public  DataAdapter(Context context, ArrayList<Cart> carts){
        this.context = context;
        this.carts = carts;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cart_layout, viewGroup, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {
        cartViewHolder.txtProdName.setText(carts.get(i).getProduct().getProductName());
        int qty = carts.get(i).getQuantity();
        double price = Double.parseDouble(carts.get(i).getProduct().getProductPrice());
        cartViewHolder.txtQty.setText(String.valueOf(qty));
        cartViewHolder.txtPrice.setText(String.valueOf(price));
        double itemTotal = price * qty;
        cartViewHolder.txtItemTotal.setText(String.valueOf(itemTotal));
        Picasso.get()
                .load(carts.get(i).getProduct().getProductImageDownloadUri())
                .into(cartViewHolder.imageViewProduct);

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }
}
