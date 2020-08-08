package com.hanyghazal.myecommerceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageViewProduct;
    TextView textViewProdName, textViewProdDesc, textViewProdPrice, textViewQty,
            textViewPageTitle;
    Button btnAddToCat;
    //int counter;
    String productKey;
    private String productName;
    private String cartRandomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        init();
    }

    private void init() {
        initUI();
        viewProductDetails();
    }



    private void initUI() {
        imageViewProduct = findViewById(R.id.prod_details_image_view);
        textViewProdName = findViewById(R.id.prod_details_text_view_name);
        textViewProdDesc = findViewById(R.id.prod_details_text_view_desc);
        textViewProdPrice = findViewById(R.id.prod_details_text_view_price);
        textViewQty = findViewById(R.id.text_view_qty);
        textViewPageTitle = findViewById(R.id.prod_details_textview_appbar_name);

        btnAddToCat = findViewById(R.id.btn_add_to_cart);
        btnAddToCat.setOnClickListener(this);

    }
    public void viewProductDetails(){
        productKey = getIntent().getStringExtra(Commons.PRODUCT_KEY);
        productName = getIntent().getStringExtra(Commons.PRODUCT_NAME_KEY);
        textViewPageTitle.setText(productName);
//        counter = 0;
//        textViewQty.setText(String.valueOf(counter));
        //===
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Commons.PRODUCTS_DB)
                .child(productKey);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Commons.currentProduct = dataSnapshot.getValue(Product.class);

                    Picasso.get().load(Commons.currentProduct.getProductImageDownloadUri()).into(imageViewProduct);
                    textViewProdName.setText(Commons.currentProduct.getProductName());
                    textViewProdDesc.setText(Commons.currentProduct.getProductDescription());
                    textViewProdPrice.setText(Commons.currentProduct.getProductPrice());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onClick(View v) {
//       if(v.getId() == R.id.button_increase){
//
//           increaseQty();
//       }
//       else if(v.getId() == R.id.button_decrease){
//
//           decreaseQty();
//       }
        if(v.getId() == R.id.btn_add_to_cart){

           addToCartDB(getCartInfo());

       }
    }

    private void addToCartDB(Cart cart) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Commons.CARTS_DB);
        dbRef.child(dbRef.push().getKey()).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ProductDetailsActivity.this, "This product is added to cart", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProductDetailsActivity.this, CartDetailsActivity.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetailsActivity.this, "Failed product addition to cart", Toast.LENGTH_SHORT).show();
                Log.i("CART===>", "onFailure: "+e.toString());
            }
        });
    }
    public Cart getCartInfo(){
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
//        String currentSaveTime = timeFormat.format(calendar.getTime());
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
//        String currentSaveDate = dateFormat.format(calendar.getTime());
//        cartRandomKey = currentSaveDate+currentSaveTime;
        Cart cart = new Cart(Commons.currentUserKey, Commons.currentProduct, 1);
        return cart;
    }

}
