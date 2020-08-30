package com.hanyghazal.myecommerceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rey.material.widget.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewProductActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    FloatingActionButton fabSave, fabSelectImage;
    ImageView imageViewProduct;
    EditText edtxtProdName, edtxtProdPrice, edtxtProdDesc;
    Button btnSave;
    private Uri imageUri;

    AutoCompleteTextView autoTxtvCategories;
    ArrayList<String> catNames;
    private boolean isValidated;
    private Product product;
    String productName;
    String productCategory;
    String productPrice;
    String productDesc;
    private String productImageDownloadUri;
    private ProgressDialog dialog;
    private String productRandomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        init();
    }

    private void init() {
        initUI();
        fillSpinnerCategories();

    }

    private void fillSpinnerCategories() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Commons.CATEGORIES_DB);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String categoryName = snapshot.child("categoryName").getValue(String.class);
                        catNames.add(categoryName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, catNames);
        autoTxtvCategories.setAdapter(adapter);
//        autoTxtvCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void initUI() {
        fabSave = findViewById(R.id.new_product_fab_save);
        fabSelectImage = findViewById(R.id.new_product_fab_select_image);
        edtxtProdName = findViewById(R.id.new_product_edit_text_name);
        autoTxtvCategories = findViewById(R.id.new_product_auto_text_category);
        edtxtProdPrice = findViewById(R.id.new_product_edit_text_price);
        edtxtProdDesc = findViewById(R.id.new_product_edit_text_description);
        imageViewProduct = findViewById(R.id.new_product_image_view);
        btnSave = findViewById(R.id.new_product_button_save);

        catNames = new ArrayList<>();
        //product = new Product();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Saving a new product");
        dialog.setMessage("Please wait while saving the product data and updating the database..");
        dialog.setCanceledOnTouchOutside(false);

        fabSave.setOnClickListener(this);
        fabSelectImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.new_product_fab_select_image){
            selectProductImage();
        }
        else if(v.getId() == R.id.new_product_button_save){
            saveNewProduct();
        }
        else if(v.getId() == R.id.new_product_fab_save){
            saveNewProduct();
        }
    }
    private void saveNewProduct() {
        validateProductInfo();
        if(isValidated){
            continueSaving();
        }
    }
    public void continueSaving() {
        uploadProductImage(imageUri);
//        product = new Product(productName, productCategory, productPrice, productDesc, productImageDownloadUri);
//        updateProductDatabase(product);
    }

    private void updateProductDatabase(Product product) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Commons.PRODUCTS_DB);
        //String productKey = dbRef.push().getKey();
        dbRef.child(product.getProductKey()).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                   dialog.dismiss();
                   Toast.makeText(NewProductActivity.this, "The product has been added successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(NewProductActivity.this, "Failed product insertion", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(NewProductActivity.this, "Error: "+e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadProductImage(Uri uri) {
        dialog.show();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        String currentSaveTime = timeFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String currentSaveDate = dateFormat.format(calendar.getTime());

        productRandomKey = currentSaveDate + currentSaveTime;
        productName = edtxtProdName.getText().toString();
        productCategory = autoTxtvCategories.getText().toString();
        productPrice = edtxtProdPrice.getText().toString();
        productDesc = edtxtProdDesc.getText().toString();
        String productImageName = productName.toLowerCase().trim().replace(' ', '_')+productRandomKey+".JPG";
        final StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference(Commons.PRODUCT_IMAGES_STORE)
                .child(productImageName);
        StorageTask storageTask = storageRef.putFile(uri);
        storageTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText(NewProductActivity.this, "OBJECT TASK EXCEPTION: "+task.getException(), Toast.LENGTH_LONG).show();
                    throw  task.getException();
                }
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewProductActivity.this, "Image uploaded successfully", Toast.LENGTH_LONG).show();
                    productImageDownloadUri = task.getResult().toString();
                    product = new Product(productRandomKey,productName, productCategory, productPrice, productDesc, productImageDownloadUri);
                    updateProductDatabase(product);
                }
                else {
                    Toast.makeText(NewProductActivity.this, "URI TASK EXCEPTION: "+task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewProductActivity.this, "TASK FAILURE EXCEPTION: "+e.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public boolean validateProductInfo(){
        isValidated = false;
        if(TextUtils.isEmpty(edtxtProdName.getText().toString())){
            Toast.makeText(this, "Product name field is empty", Toast.LENGTH_LONG).show();
            isValidated = false;
        }
        else if(TextUtils.isEmpty(autoTxtvCategories.getText().toString())){
            Toast.makeText(this, "Product category field is empty", Toast.LENGTH_LONG).show();
            isValidated = false;
        }
        else if(TextUtils.isEmpty(edtxtProdPrice.getText().toString())){
            Toast.makeText(this, "Product price field is empty", Toast.LENGTH_LONG).show();
            isValidated = false;
        }
        else if(TextUtils.isEmpty(edtxtProdDesc.getText().toString())){
            Toast.makeText(this, "Product description field is empty", Toast.LENGTH_LONG).show();
            isValidated = false;
        }
        else {
            isValidated = true;
        }
        return  isValidated;
    }

    private void selectProductImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("images/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            imageViewProduct.setImageURI(imageUri);
        }
    }
}
