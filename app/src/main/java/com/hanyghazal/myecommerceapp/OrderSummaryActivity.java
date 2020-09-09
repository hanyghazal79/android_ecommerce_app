package com.hanyghazal.myecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class OrderSummaryActivity extends AppCompatActivity {
    RadioButton radioBtnDeliveryAddress, radioBtnNearLocation;
    TextView txtUserDeliveryAddress, txtOrderSubTotal, txtOrderShipping, txtOrderGrandTotal;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button btnContinueToPayment;
    DatabaseReference dbRef;
    FirebaseRecyclerOptions<Cart> options;
    FirebaseRecyclerAdapter<Cart, OrderViewHolder> adapter;
    double orderSubTotal, orderShipping, orderGrandTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        init();

    }

     public void  init(){

        initUI();
        initRef();
        displayOrderSummary();

     }

     public  void initUI(){

        radioBtnDeliveryAddress = findViewById(R.id.radio_btn_delivery_address);
        radioBtnNearLocation = findViewById(R.id.radio_btn_near_location);
        txtUserDeliveryAddress = findViewById(R.id.text_view_delivery_address);
        txtOrderSubTotal = findViewById(R.id.text_view_order_subtotal);
        txtOrderShipping = findViewById(R.id.text_view_order_shipping);
        txtOrderGrandTotal = findViewById(R.id.text_view_order_grand_total);
        recyclerView = findViewById(R.id.recycler_view_order_items);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        btnContinueToPayment = findViewById(R.id.btn_goto_payment);

     }

     public  void initRef(){

        orderSubTotal = 0.0;
        orderShipping = 0.0;
        orderGrandTotal = 0.0;

        String userData =
                Commons.currentUser.getUserName()
                +"\n"+
                Commons.currentUser.getEmail()
                +"\n"+
                Commons.currentUser.getPhone();

        txtUserDeliveryAddress.setText(userData);

     }

     public  void  displayOrderSummary(){
         dbRef = FirebaseDatabase.getInstance().getReference(Commons.CARTS_DB);
         Query query = dbRef.orderByChild("userId").equalTo(Commons.currentUserKey);
         options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(query, Cart.class).build();
         adapter = new FirebaseRecyclerAdapter<Cart, OrderViewHolder>(options) {
             @Override
             protected void onBindViewHolder(@NonNull OrderViewHolder holder, int i, @NonNull Cart cart) {
                 Picasso.get().load(cart.getProduct().getProductImageDownloadUri()).into(holder.imageView);
                 holder.txtProductName.setText(cart.getProduct().getProductName());
                 holder.txtOrderItemPrice.setText(cart.getProduct().getProductPrice());
                 holder.txtOrderItemQty.setText(String.valueOf(cart.getQuantity()));
//                holder.txtOrderItemSeller.setText(cart.getProduct().getProductSellerName());
                 holder.txtOrderItemNum.setText("Item # "+i+1);
                 holder.txtOrderItemTotal.setText(String.valueOf(cart.getItemTotal()) + Commons.currencySymbol);

//                 orderSubTotal += cart.getItemTotal();
//                 orderShipping += cart.getProduct().
             }

             @NonNull
             @Override
             public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order_item, parent, false);
                 OrderViewHolder holder = new OrderViewHolder(view);
                 return holder;
             }
         };
         recyclerView.setAdapter(adapter);
         adapter.startListening();
     }
}