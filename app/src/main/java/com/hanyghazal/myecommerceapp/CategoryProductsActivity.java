package com.hanyghazal.myecommerceapp;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryProductsActivity extends AppCompatActivity {
    TextView txtCategoryName;
    Button btnClose;
    RecyclerView recyclerViewCatProducts;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference dbRef;
    ArrayList<String> productKeys = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        init();
    }

    public void init() {
        initUI();
        displayCategoryProducts();
    }

    public void initUI() {
        txtCategoryName = findViewById(R.id.text_view_category_name);
        btnClose = findViewById(R.id.prod_cat_button_close);
        recyclerViewCatProducts = findViewById(R.id.category_products_recyclerview);
        recyclerViewCatProducts.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewCatProducts.setLayoutManager(layoutManager);
        //===
        txtCategoryName.setText(getIntent().getStringExtra(Commons.selectedCategory));
    }

    public void displayCategoryProducts() {
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.PRODUCTS_DB);
        Query query = dbRef.orderByChild("productCategory").equalTo(Commons.selectedCategory);
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(query, Product.class)
                        .build();
        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Product model) {
                        if(model.getProductCategory().equals(Commons.selectedCategory)){
                            Picasso.get().load(Uri.parse(model.getProductImageDownloadUri())).into(holder.imageViewProduct);
                            holder.txtProductName.setText(model.getProductName());
                            holder.txtProductPrice.setText(model.getProductPrice());
                            //===
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(CategoryProductsActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra(Commons.PRODUCT_KEY, model.getProductKey());
                                    intent.putExtra(Commons.PRODUCT_NAME_KEY, model.getProductName());
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
                            holder.itemView.setVisibility(View.GONE);
                        }

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_product_layout, viewGroup, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerViewCatProducts.setAdapter(adapter);
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
                startActivity(new Intent(CategoryProductsActivity.this, NewProductActivity.class););
                return true;
            case R.id.action_edit_product:
                startActivity(new Intent(CategoryProductsActivity.this, EditProductActivity.class));
                return true;
            case R.id.action_remove_product:
                startActivity(new Intent(CategoryProductsActivity.this, DeleteProductActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
