package com.hanyghazal.myecommerceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import io.paperdb.Paper;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnShowCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Paper.init(this);
        btnShowCategories = findViewById(R.id.admin_button_show_categories);
        btnShowCategories.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.admin_button_show_categories){
            Intent intent = new Intent(AdminActivity.this, CategoriesActivity.class);
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
