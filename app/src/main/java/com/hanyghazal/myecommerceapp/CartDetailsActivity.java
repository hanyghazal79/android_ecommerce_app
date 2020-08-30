package com.hanyghazal.myecommerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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
    private RecyclerView recyclerViewCart;
    private TextView txtCartTotal;
    private Button btnProceedToCheckout;
    private DatabaseReference dbRef;
    private ArrayList<Double> totals = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private double price;
    private double initialItemTotal;
    private double updatedItemTotal;
    private double cartTotal;
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
            protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, final int i, @NonNull final Cart cart) {
                cartTotal = 0.0;
                txtCartTotal.setText("");

                cartViewHolder.txtProdName.setText(cart.getProduct().getProductName());
                price = Double.parseDouble(cart.getProduct().getProductPrice());
                cartViewHolder.txtPrice.setText(String.valueOf(price));
                qty = cart.getQuantity();
                cartViewHolder.txtQty.setText(String.valueOf(qty));
                itemTotal = cart.getItemTotal();
                cartViewHolder.txtItemTotal.setText(String.valueOf(itemTotal));
                Picasso.get().load(cart.getProduct().getProductImageDownloadUri()).into(cartViewHolder.imageViewProduct);
                cartTotal += itemTotal;
//                for(int n = 0; n < options.getSnapshots().size(); n++){
//                    cartTotal += options.getSnapshots().get(n).getItemTotal();
//                }
                txtCartTotal.setText(String.valueOf(cartTotal));
                cartViewHolder.btnIncrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatedQty = Integer.parseInt(cartViewHolder.txtQty.getText().toString()) + 1;
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
                cartViewHolder.btnDecrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatedQty = Integer.parseInt(cartViewHolder.txtQty.getText().toString()) - 1;
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
//        txtCartTotal.setText("");
//        dbRef = FirebaseDatabase.getInstance()
//                .getReference(Commons.CARTS_DB);
//        Query query = dbRef.orderByChild("userId").equalTo(Commons.currentUserKey);//.child(Commons.currentUserKey);
//
//        final FirebaseRecyclerOptions<Cart> options =
//                new FirebaseRecyclerOptions.Builder<Cart>()
//                .setQuery(query, Cart.class)
//                .build();
//        final FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
//
//                    @Override
//                    protected void onBindViewHolder(@NonNull final CartViewHolder holder, final int position, @NonNull final Cart model) {
//
//                        holder.txtProdName.setText(model.getProduct().getProductName());
//                        holder.txtPrice.setText(model.getProduct().getProductPrice());
//                        holder.txtQty.setText(String.valueOf(model.getQuantity()));
//                        holder.txtItemTotal.setText(model.getItemTotal() + Commons.currencySymbol);
//                        Picasso.get().load(model.getProduct().getProductImageDownloadUri()).into(holder.imageViewProduct);
//
//                        price = Double.valueOf(model.getProduct().getProductPrice());
//                        qty = model.getQuantity();
//                        //=====================
////                        for(Cart cart : options.getSnapshots()){
////                            cartTotal += cart.getItemTotal();
////                        }
//                        totals.add(model.getItemTotal());
//                        for(int i=0; i<totals.size(); i++){
//                            cartTotal += totals.get(i);
//                        }
//                        txtCartTotal.setText("TOTAL = "+String.valueOf(cartTotal)+Commons.currencySymbol);
//                        //=====================
//                        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                updatedQty = Integer.parseInt(holder.txtQty.getText().toString()) + 1;
//                                itemTotal = updatedQty * Double.parseDouble(model.getProduct().getProductPrice());
//                                Cart cart = new Cart(Commons.currentUserKey, model.getProduct(), updatedQty, itemTotal);
//                                dbRef.child(options.getSnapshots().getSnapshot(position).getKey()).setValue(cart)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//
//
//                                            }
//                                        });
//                            }
//                        });
//                        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                updatedQty = Integer.parseInt(holder.txtQty.getText().toString()) - 1;
//                                itemTotal = updatedQty * Double.parseDouble(model.getProduct().getProductPrice());
//                                Cart cart = new Cart(Commons.currentUserKey, model.getProduct(), updatedQty, itemTotal);
//                                dbRef.child(options.getSnapshots().getSnapshot(position).getKey()).setValue(cart)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//
//                                            }
//                                        });
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
//                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_cart_layout, viewGroup, false);
//                        final CartViewHolder holder = new CartViewHolder(view);
//                        return holder;
//                    }
//
//                };
//
//        recyclerViewCart.setAdapter(adapter);
//        adapter.startListening();

    }

    private void updateCartTotal(int qty) {

    }

    private ArrayList<ArrayList<Double>> calculateCartTotals(FirebaseRecyclerOptions<Cart> options) {
        double cartTotal = 0.0;
        ArrayList<Double> itemTotals = new ArrayList<>();
        ArrayList<Double> cartTotals = new ArrayList<>();

        double[] prices = new double[]{};
        int[] quantities = new int[]{};
        ArrayList<ArrayList<Double>> totals = new ArrayList<>();
        ObservableSnapshotArray<Cart> snapshots = options.getSnapshots();
        for(int i = 0; i<snapshots.size(); i++){
            prices[i] = Double.parseDouble(snapshots.get(i).getProduct().getProductPrice());
            quantities[i] = snapshots.get(i).getQuantity();
            itemTotals.set(i, prices[i] * quantities[i]);
            cartTotal += prices[i] * quantities[i];
            cartTotals.add(cartTotal);
            totals.add(0, itemTotals);
            totals.add(1, cartTotals);
        }
        return totals;
    }

    private double calculateItemTotal(double price, int quantity) {
        double itemTotal = price * quantity;
        DecimalFormat df = new DecimalFormat("####0.00");
        String itemTotalRound = df.format(itemTotal);
        return Double.parseDouble(itemTotalRound);
    }

    private void initUI() {
        cartTotal = 0.0;
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.CARTS_DB);
        recyclerViewCart = findViewById(R.id.cart_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(layoutManager);
        recyclerViewCart.setHasFixedSize(true);

        txtCartTotal = findViewById(R.id.cart_textview_total);
        btnProceedToCheckout = findViewById(R.id.cart_button_to_checkout);
    }
}
