package com.hanyghazal.myecommerceapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnShowCategories, btnShowProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Paper.init(this);
        btnShowCategories = findViewById(R.id.admin_button_show_categories);
        btnShowProducts = findViewById(R.id.admin_button_show_products);

        btnShowCategories.setOnClickListener(this);
        btnShowProducts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.admin_button_show_categories){
            Intent intent = new Intent(AdminActivity.this, CategoriesActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.admin_button_show_products){
            Intent intent = new Intent(AdminActivity.this, ProductsActivity.class);
            startActivity(intent);
        }
    }
    public void logout(){
        Paper.book().destroy();
        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
