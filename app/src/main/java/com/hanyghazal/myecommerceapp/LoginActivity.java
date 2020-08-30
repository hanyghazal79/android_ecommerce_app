package com.hanyghazal.myecommerceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText loginEdtxtNameOrEmail, loginEdtxtPassword;
    Button btnLogin;
    TextView txtAdmin, txtNotAdmin;
    DatabaseReference dbRef;
    private ProgressDialog progressDialog;
    CheckBox checkBoxRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Commons.dbName = Commons.USERS_DB;
        Toast.makeText(this, Commons.dbName, Toast.LENGTH_LONG).show();
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.dbName);
        Toast.makeText(this, dbRef.toString(), Toast.LENGTH_LONG).show();
        loginEdtxtNameOrEmail = findViewById(R.id.login_edtxt_username_or_email);
        loginEdtxtPassword = findViewById(R.id.login_edtxt_password);
        btnLogin = findViewById(R.id.login_btn_login);
        progressDialog = new ProgressDialog(this);
        checkBoxRemember = findViewById(R.id.login_checkbox_remember);
        txtAdmin = findViewById(R.id.login_txtv_admin);
        txtNotAdmin = findViewById(R.id.login_txtv_not_admin);
        btnLogin.setOnClickListener(this);
        txtAdmin.setOnClickListener(this);
        txtNotAdmin.setOnClickListener(this);

        Paper.init(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_btn_login){
            String email = String.valueOf(loginEdtxtNameOrEmail.getText());
            String password = String.valueOf(loginEdtxtPassword.getText());

            login(email, password);
        }
        else if(v.getId() == R.id.login_txtv_admin){
            txtAdmin.setVisibility(View.INVISIBLE);
            txtNotAdmin.setVisibility(View.VISIBLE);
            btnLogin.setText("Admin login");
            Commons.dbName = Commons.ADMINS_DB;
        }
        else if(v.getId() == R.id.login_txtv_not_admin){
            txtAdmin.setVisibility(View.VISIBLE);
            txtNotAdmin.setVisibility(View.INVISIBLE);
            btnLogin.setText("Login");
            Commons.dbName = Commons.USERS_DB;
        }
    }

    private void login(final String eMail, final String password) {
        progressDialog.setTitle("Loging in..");
        progressDialog.setMessage("Please wait while checking your data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String rName = snapshot.child("userName").getValue(String.class);
                        String rEmail = snapshot.child("eMail").getValue(String.class);
                        String rPassword = snapshot.child("password").getValue(String.class);
                        String rPhone= snapshot.child("phone").getValue(String.class);

                        if(eMail.equals(rEmail) && password.equals(rPassword)){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "You are logged in successfully", Toast.LENGTH_LONG).show();
                            Commons.currentUser = snapshot.getValue(User.class);
                            Commons.currentUserKey = snapshot.getKey();
                            if(checkBoxRemember.isChecked()){
                                Paper.book().write(Commons.DB_KEY, Commons.dbName);
                                Paper.book().write(Commons.USER_NAME_KEY, Commons.currentUser.getUserName());
                                Paper.book().write(Commons.E_MAIL_KEY, Commons.currentUser.geteMail());
                                Paper.book().write(Commons.PASS_WORD_KEY, Commons.currentUser.getPassword());
                                Toast.makeText(LoginActivity.this, "Saved login data!..", Toast.LENGTH_SHORT).show();
                            }

                            Intent intent = new Intent(LoginActivity.this, CartDetailsActivity.class);
                            intent.putExtra("Email", rEmail);
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "ERROR: "+databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
