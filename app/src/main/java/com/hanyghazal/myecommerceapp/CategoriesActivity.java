package com.hanyghazal.myecommerceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class CategoriesActivity extends AppCompatActivity {
    ListView categoriesListView;
    StorageReference storageReference;
    DatabaseReference dbRef;
   // private static final long ONE_MEGABYTE = 1024 * 1024;
//    ImageView imageView;
    //private String categoryImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        categoriesListView = findViewById(R.id.categories_list_view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_category:
                Intent intent = new Intent(CategoriesActivity.this, NewCategoryActivity.class);
                startActivity(intent);
                return true;
                default: return super.onOptionsItemSelected(item);
        }
    }
}
