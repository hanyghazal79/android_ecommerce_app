package com.hanyghazal.myecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ProductsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference dbRef;
    FirebaseRecyclerOptions<Product> options;
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        init();

    }
    public void init(){
        initUI();
        displayProducts();
    }
    public void initUI(){
        recyclerView = findViewById(R.id.recycler_view_products);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }
    public void displayProducts() {
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.PRODUCTS_DB);
        options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(dbRef, Product.class).build();
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
                        Picasso.get().load(Uri.parse(model.getProductImageDownloadUri())).into(holder.imageViewProduct);
                        holder.txtProductName.setText(model.getProductName());
                        holder.txtProductPrice.setText(model.getProductPrice());
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_product_layout, viewGroup, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_products, menu);
        if(Commons.isAdmin){

            menu.getItem(R.id.action_add_product).setVisible(true);
            menu.getItem(R.id.action_edit_product).setVisible(true);
            menu.getItem(R.id.action_remove_product).setVisible(true);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add_product:
                startActivity(new Intent(ProductsActivity.this, NewProductActivity.class););
                return true;
            case R.id.action_edit_product:
                startActivity(new Intent(ProductsActivity.this, EditProductActivity.class));
                return true;
            case R.id.action_remove_product:
                startActivity(new Intent(ProductsActivity.this, DeleteProductActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}