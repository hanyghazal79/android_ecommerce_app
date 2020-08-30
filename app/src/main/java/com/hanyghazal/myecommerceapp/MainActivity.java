package com.hanyghazal.myecommerceapp;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button mainBtnJoinNow, mainBtnLogin;
    ProgressDialog progressDialog;
    DatabaseReference dbRef;
    String retrievedUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this, CartDetailsActivity.class));
        mainBtnJoinNow = findViewById(R.id.main_btn_join_now);
        mainBtnLogin = findViewById(R.id.main_btn_login);
        mainBtnJoinNow.setOnClickListener(this);
        mainBtnLogin.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        Paper.init(this);
        rememberUser();
//        Paper.book().destroy();
    }

    private void rememberUser() {
        retrievedUserName = Paper.book().read(Commons.USER_NAME_KEY);
        String eMail = Paper.book().read(Commons.E_MAIL_KEY);
        String password = Paper.book().read(Commons.PASS_WORD_KEY);

        Commons.dbName = Paper.book().read(Commons.DB_KEY);

        if(eMail != null && password != null){
            if(!TextUtils.isEmpty(eMail) && !TextUtils.isEmpty(password)){
                login(eMail, password);
            }
        }
    }

    private void login(final String eMail, final String password) {
        progressDialog.setTitle("Welcome back "+retrievedUserName);
        progressDialog.setMessage("Please wait while going to the home page.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        dbRef = FirebaseDatabase.getInstance().getReference(Commons.dbName);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    progressDialog.dismiss();
                } else if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        
                        String retrievedEmail = snapshot.child("eMail").getValue(String.class);
                        String retrievedPassword = snapshot.child("password").getValue(String.class);
                        Commons.currentUserKey = snapshot.getKey();

                        if(eMail.equals(retrievedEmail) && password.equals(retrievedPassword)){
                            Toast.makeText(MainActivity.this, "Successful login.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, CartDetailsActivity.class);
                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.main_btn_join_now){
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.main_btn_login){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
