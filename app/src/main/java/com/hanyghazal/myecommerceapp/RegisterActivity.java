package com.hanyghazal.myecommerceapp;

import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText regEdtxtName, regEdtxtEmail, regEdtxtPassword, regEdtxtConfirmPassword, regEdtxtPhone, regEdtxtAddress;
    Button regBtnRegister;
    DatabaseReference dbRef;
    ProgressDialog progressDialog;
    TextView txtvRegisterAsAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Commons.dbName = Commons.USERS_DB;

        regEdtxtName = findViewById(R.id.register_edtxt_username);
        regEdtxtEmail = findViewById(R.id.register_edtxt_email);
        regEdtxtPassword = findViewById(R.id.register_edtxt_password);
        regEdtxtConfirmPassword = findViewById(R.id.register_edtxt_confirm_password);
        regEdtxtPhone = findViewById(R.id.register_edtxt_phone);
        regEdtxtAddress = findViewById(R.id.register_edtxt_address);
        regBtnRegister = findViewById(R.id.register_btn_register);
        progressDialog = new ProgressDialog(this);
        txtvRegisterAsAdmin = findViewById(R.id.register_txtv_as_admin);

        regBtnRegister.setOnClickListener(this);
        txtvRegisterAsAdmin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.register_btn_register){
            register();
        }
        else if(v.getId() == R.id.register_txtv_as_admin){
            Commons.dbName = Commons.ADMINS_DB;
            regBtnRegister.setText("Admin register");
        }
    }

    private void register() {
        validateUser();
    }
    public void validateUser() {
        String name = String.valueOf(regEdtxtName.getText());
        final String email = String.valueOf(regEdtxtEmail.getText());
        String password = String.valueOf(regEdtxtPassword.getText());
        String phone = String.valueOf(regEdtxtPhone.getText());
        String address = String.valueOf(regEdtxtAddress.getText());
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a user name", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();

        } else {
            progressDialog.setTitle("Creating an account..");
            progressDialog.setMessage("Please wait for while we are checking the credentials.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            final User user = new User(name, email, password, phone, address);

            dbRef = FirebaseDatabase.getInstance().getReference(Commons.dbName);
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        insertUser(user);
                    }
                    else {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String emailInfo = snapshot.child("email").getValue(String.class);
                            if (email.equals(emailInfo)) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "This Email already exists.", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Inserting user data.", Toast.LENGTH_SHORT).show();
                                //===
                                insertUser(user);
                                //===
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void insertUser(User user){
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.dbName);
        String userID = dbRef.push().getKey();
        dbRef.child(userID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Your registration has been completed successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Registration has failed due to an error, please try again.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
