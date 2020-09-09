package com.hanyghazal.myecommerceapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerViewCart;
    private TextView txtCartTotal;
    private Button btnProceedToCheckout;
    private DatabaseReference dbRef;
    private ArrayList<Double> totals = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private double price;
    private double initialItemTotal;
    private double updatedItemTotal;
    private double cartSubTotal;
    private int initialQty;
    private int updatedQty;
    private ArrayList<Cart> carts = new ArrayList<>();
    private double itemTotal;
    private int qty;
    private FirebaseRecyclerOptions<Cart> options;
    private FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);

        init();
    }

    private void init() {
        initUI();
        viewCartItems();

    }



    private void viewCartItems() {

        dbRef = FirebaseDatabase.getInstance().getReference(Commons.CARTS_DB);
        Query query = dbRef.orderByChild("userId").equalTo(Commons.currentUserKey);
        options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(query, Cart.class).build();
        adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, final int i, @NonNull final Cart cart) {

                cartSubTotal = 0.0;
                txtCartTotal.setText("");

                holder.txtProdName.setText(cart.getProduct().getProductName());
                price = Double.parseDouble(cart.getProduct().getProductPrice());
                holder.txtPrice.setText(String.valueOf(price));
                qty = cart.getQuantity();
                holder.txtQty.setText(String.valueOf(qty));
                itemTotal = cart.getItemTotal();
                holder.txtItemTotal.setText(String.valueOf(itemTotal));
                Picasso.get().load(cart.getProduct().getProductImageDownloadUri()).into(holder.imageViewProduct);
                cartSubTotal += itemTotal;
                txtCartTotal.setText(String.valueOf(cartSubTotal));

                holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatedQty = Integer.parseInt(holder.txtQty.getText().toString()) + 1;
                        updatedItemTotal = updatedQty * price;
                        Cart updatedCart = new Cart(Commons.currentUserKey, cart.getProduct(), updatedQty, updatedItemTotal);
                        dbRef.child(options.getSnapshots().getSnapshot(i).getKey()).setValue(updatedCart)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        viewCartItems();

                                    }
                                });
                    }
                });
                holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatedQty = Integer.parseInt(holder.txtQty.getText().toString()) - 1;
                        updatedItemTotal = updatedQty * price;
                        Cart updatedCart = new Cart(Commons.currentUserKey, cart.getProduct(), updatedQty, updatedItemTotal);
                        dbRef.child(options.getSnapshots().getSnapshot(i).getKey()).setValue(updatedCart)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        viewCartItems();

                                    }
                                });
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerViewCart.setAdapter(adapter);
        adapter.startListening();
    }



    private void initUI() {
        cartSubTotal = 0.0;
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.CARTS_DB);
        recyclerViewCart = findViewById(R.id.cart_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(layoutManager);
        recyclerViewCart.setHasFixedSize(true);

        txtCartTotal = findViewById(R.id.cart_textview_total);
        btnProceedToCheckout = findViewById(R.id.cart_button_to_checkout);
        btnProceedToCheckout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cart_button_to_checkout){

            startActivity(new Intent(this, OrderSummaryActivity.class));

        }
    }

}
