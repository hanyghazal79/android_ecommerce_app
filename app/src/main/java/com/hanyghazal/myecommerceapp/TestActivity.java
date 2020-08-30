package com.hanyghazal.myecommerceapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TestActivity extends AppCompatActivity {
TextView textView;
DataAdapter adapter;
DatabaseReference dbRef;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView = findViewById(R.id.text_view);
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.CARTS_DB);
        textView.setText("ROOT = "+dbRef.getRoot()+"\n"+"DB ="+dbRef.getDatabase()+"\n"+
                dbRef.getPath());
    }
}
