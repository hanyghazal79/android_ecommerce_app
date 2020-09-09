package com.hanyghazal.myecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class CategoriesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference dbRef;
    private Query query;
    private FirebaseRecyclerOptions<Category> options;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
//    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        init();

    }

    public void init() {

        initUI();
        initRef();
        displayCategories();

    }


    public void initUI(){

        recyclerView = findViewById(R.id.recycler_view_categories);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }

    public  void initRef(){
        Paper.init(this);
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.CATEGORIES_DB);
        query = dbRef.orderByKey();

    }

    public void displayCategories() {

        options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(query, Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int i, @NonNull final Category category) {

                holder.textViewCategoryName.setText(category.getCategoryName());
                Picasso.get().load(Uri.parse(category.getCategoryImageDownloadUri())).into(holder.imageViewCategoryImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Commons.selectedCategory = category.getCategoryName();
                        Intent intent = new Intent(CategoriesActivity.this, CategoryProductsActivity.class);
                        intent.putExtra(Commons.selectedCategory, Commons.selectedCategory);
                        startActivity(intent);



                    }
                });



            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category_layout, parent, false);
                CategoryViewHolder holder = new CategoryViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_categories, menu);

        if(Commons.isAdmin){

            menu.getItem(R.id.action_add_category).setVisible(true);
            menu.getItem(R.id.action_edit_category).setVisible(true);
            menu.getItem(R.id.action_remove_category).setVisible(true);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add_category:
                startActivity(new Intent(CategoriesActivity.this, NewCategoryActivity.class););
                return true;
            case R.id.action_edit_category:
                startActivity(new Intent(CategoriesActivity.this, EditCategoryActivity.class));
                return true;
            case R.id.action_remove_category:
                startActivity(new Intent(CategoriesActivity.this, DeleteCategoryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}