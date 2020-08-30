package com.hanyghazal.myecommerceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    Button btnChooseImage, btnSave;
    ImageView imageView;
    EditText edtxtCategoryName;
    private Uri imgUri;
    String categoryRandomKey, categoryName, categoryImageName;
    StorageReference storageReference;
    DatabaseReference dbRef;
    ProgressDialog progressDialog;
    private String categoryImageDownloadUri;
    private StorageReference filePathRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        this.setTitle("New Category");
        btnChooseImage = findViewById(R.id.new_cat_button_choose_image);
        btnSave = findViewById(R.id.new_cat_button_save);
        imageView = findViewById(R.id.new_category_image_view);
        edtxtCategoryName = findViewById(R.id.new_cat_edtxt_category_name);

        btnSave.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference(Commons.CATEGORY_IMAGES_STORE);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.new_cat_button_choose_image){
            chooseImage();

        }
        else if(v.getId() == R.id.new_cat_button_save){
            saveCategory();
        }
    }

    private void saveCategory() {
        categoryName = edtxtCategoryName.getText().toString();
        Commons.categoryNames.add(categoryName);
        if(imgUri == null){
            Toast.makeText(this, "Please choose an image..", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(categoryName)){
            Toast.makeText(this, "Please provide a category name..", Toast.LENGTH_SHORT).show();
        }
        else {
            uploadImage(imgUri);

        }
    }

    private void uploadImage(Uri uri) {
        progressDialog.setTitle("Saving a new category.");
        progressDialog.setMessage("Please wait until saving the new product category..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        String currentSaveTime = timeFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String currentSaveDate = dateFormat.format(calendar.getTime());
        categoryRandomKey = currentSaveDate + currentSaveTime;
        categoryImageName = categoryName.toLowerCase().trim().replace(' ', '_')+categoryRandomKey+".JPG";
        Commons.categoryImageNames.add(categoryImageName);

        filePathRef = storageReference.child(categoryImageName);
        final UploadTask uploadTask = filePathRef.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if(task.isSuccessful()){
                           categoryImageDownloadUri = filePathRef.getDownloadUrl().toString();
                       }
                       else throw task.getException();
                        return filePathRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        categoryImageDownloadUri = task.getResult().toString();
                        Category category = new Category(categoryName, categoryImageDownloadUri);
                        saveToDatabase(category);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void saveToDatabase(Category category) {
        dbRef = FirebaseDatabase.getInstance().getReference(Commons.CATEGORIES_DB);
        dbRef.child(categoryRandomKey).setValue(category)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Intent intent = new Intent(NewCategoryActivity.this, CategoriesActivity.class);
                            Toast.makeText(NewCategoryActivity.this, "Category is added successfully.", Toast.LENGTH_LONG).show();

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(NewCategoryActivity.this, "Failed category addition.", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void chooseImage() {
        Intent galleryIntent = new Intent();

//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("images/*");
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data !=null){
            imgUri = data.getData();
            imageView.setImageURI(imgUri);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("imageNames", Commons.categoryImageNames);
    }
}
