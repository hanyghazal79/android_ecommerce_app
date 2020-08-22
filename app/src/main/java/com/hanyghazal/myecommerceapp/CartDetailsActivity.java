package com.hanyghazal.myecommerceapp;

import android.support.annotation.NonNull;
import android.support.v4.math.MathUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerViewCart;
    TextView txtCartTotal;
    Button btnProceedToCheckout;
    DatabaseReference dbRef;
    private double price;
    private double initialItemTotal;
    private double updatedItemTotal;
    private double cartTotal = 0.0;
    private int initialQty;
    private int updatedQty;


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
        dbRef = FirebaseDatabase.getInstance()
                .getReference(Commons.CARTS_DB);
        Query query = dbRef.orderByChild("userId").equalTo(Commons.currentUserKey);//.child(Commons.currentUserKey);

        final FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(query, Cart.class)
                .build();
        final FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model) {
                        holder.txtProdName.setText(model.getProduct().getProductName());
                        holder.txtPrice.setText(model.getProduct().getProductPrice());
                        holder.txtQty.setText(String.valueOf(model.getQuantity()));
                        Picasso.get().load(model.getProduct().getProductImageDownloadUri()).into(holder.imageViewProduct);
                        price = Double.valueOf(model.getProduct().getProductPrice());
                        initialQty = model.getQuantity();
                        updatedQty = initialQty;
                        initialItemTotal = calculateItemTotal(price, initialQty);
//                        cartTotal = calculateCartTotal(options, 0);
                        cartTotal += initialItemTotal;
                        holder.txtItemTotal.setText(initialItemTotal + Commons.currencySymbol);
                        txtCartTotal.setText("TOTAL = "+String.valueOf(cartTotal)+Commons.currencySymbol);

                        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updatedQty = initialQty + 1;
                                holder.txtQty.setText(String.valueOf(updatedQty));
                                updatedItemTotal = calculateItemTotal(price, updatedQty);
                                cartTotal += updatedItemTotal - initialItemTotal;
                                holder.txtItemTotal.setText(updatedItemTotal + Commons.currencySymbol);

                                txtCartTotal.setText("TOTAL = "+String.valueOf(cartTotal)+Commons.currencySymbol);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_cart_layout, viewGroup, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }

                };

        recyclerViewCart.setAdapter(adapter);
        adapter.startListening();

    }

    private void updateCartTotal(int qty) {

    }

    private double calculateCartTotal(FirebaseRecyclerOptions<Cart> options, int qtyUpdate, double cartTotal) {
//        double cartTotal = 0.0;
        ObservableSnapshotArray<Cart> snapshots = options.getSnapshots();
        for(int i = 0; i<snapshots.size(); i++){
            double price = Double.parseDouble(snapshots.get(i).getProduct().getProductPrice());
            int qty = snapshots.get(i).getQuantity() + qtyUpdate;
            cartTotal += price * qty;
        }
        return cartTotal;
    }

    private double calculateItemTotal(double price, int quantity) {
        double itemTotal = price * quantity;
        DecimalFormat df = new DecimalFormat("####0.00");
        String itemTotalRound = df.format(itemTotal);
        return Double.parseDouble(itemTotalRound);
    }

    private void initUI() {
        recyclerViewCart = findViewById(R.id.cart_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(layoutManager);
        recyclerViewCart.setHasFixedSize(true);

        txtCartTotal = findViewById(R.id.cart_textview_total);
        btnProceedToCheckout = findViewById(R.id.cart_button_to_checkout);
    }
}
