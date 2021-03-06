package com.hanyghazal.myecommerceapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.opengl.Visibility;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FloatingActionButton fab;
    DatabaseReference dbRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CircularImageView imageViewProfile;
    View navAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        displayCategories();

    }

    private void initUI() {
        Paper.init(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle("Home");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);

        navigationView = findViewById(R.id.nav_view_home);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);

        imageViewProfile = navHeader.findViewById(R.id.nav_header_user_image);
        displayProfileImage();

        TextView textViewEmail = navHeader.findViewById(R.id.nav_header_textv_email);
        textViewEmail.setText(getIntent().getStringExtra("Email"));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Hello I am FAB", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.CATEGORIES_DB);
        recyclerView = findViewById(R.id.home_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // ===

        navAdmin = findViewById(R.id.nav_admin);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
                case R.id.action_settings:
                //code settings here
                return true;
                default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.nav_admin){
            startActivity(new Intent(HomeActivity.this, AdminActivity.class));
        }
        if(menuItem.getItemId() == R.id.nav_cart){
              displayCart();
        }
        else if(menuItem.getItemId() == R.id.nav_orders){

        }
        else if (menuItem.getItemId() == R.id.nav_categories) {
//            displayCategories();
            startActivity(new Intent(HomeActivity.this, CategoriesActivity.class));

        }
        else if (menuItem.getItemId() == R.id.nav_products) {
            startActivity(new Intent(HomeActivity.this, CategoryProductsActivity.class));
        }
        else if (menuItem.getItemId() == R.id.nav_settings) {
            displaySettings();

        }
        else if (menuItem.getItemId() == R.id.nav_logout) {

          Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayCart() {
        startActivity(new Intent(HomeActivity.this, CartDetailsActivity.class));
    }
    //============================

    private void displaySettings() {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    //========================================

    @Override
    protected void onStart() {
        super.onStart();
        //displayCategories(); // invoked here if we want to display on starting activity

    }
    public void displayCategories(){
//        startActivity(new Intent(HomeActivity.this, CategoriesActivity.class));
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(dbRef, Category.class)
                        .build();
        FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position, @NonNull final Category model) {
                        holder.textViewCategoryName.setText(model.getCategoryName());
                        Picasso.get().load(Uri.parse(model.getCategoryImageDownloadUri())).into(holder.imageViewCategoryImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Commons.selectedCategory = holder.textViewCategoryName.getText().toString();
                                Commons.selectedCategory = model.getCategoryName();
                                Intent intent = new Intent(HomeActivity.this, CategoryProductsActivity.class);
                                intent.putExtra(Commons.selectedCategory, Commons.selectedCategory);
                                startActivity(intent);

                            }
                        });
                    }
                    @NonNull
                    @Override
                    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_category_layout, viewGroup, false);
                        CategoryViewHolder holder = new CategoryViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public void displayProfileImage(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Commons.USERS_DB).child(Commons.currentUserKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("userImageUri").exists()){
                    String imageUri = dataSnapshot.child("userImageUri").getValue(String.class);
                    Picasso.get().load(Uri.parse(imageUri)).into(imageViewProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //StorageReference storageReference = FirebaseStorage.getInstance().getReference()
    }
}
