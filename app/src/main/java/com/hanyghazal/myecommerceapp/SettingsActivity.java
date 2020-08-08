package com.hanyghazal.myecommerceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    CircularImageView imageViewProfile;
    EditText edtxtName, edtxtEmail, edtxtPassword, edtxtPhone;
    Button btnSave, btnClose;
    FloatingActionButton fabSelectImage;
    boolean isImageSelected, isValidated;
    Uri imageUri;
    private static final int VALID_FACTOR = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    private void init() {
        initUI();
        displayCurrentUserInfo();
    }
    private void initUI() {
        imageViewProfile = findViewById(R.id.settings_profile_image_view);
        edtxtName = findViewById(R.id.settings_edtxt_name);
        edtxtEmail = findViewById(R.id.settings_edtxt_email);
        edtxtPassword = findViewById(R.id.settings_edtxt_password);
        edtxtPhone = findViewById(R.id.settings_edtxt_phone);
        btnSave = findViewById(R.id.settings_button_save);
        btnClose = findViewById(R.id.settings_button_close);
        fabSelectImage = findViewById(R.id.settings_fab_pick_image);
        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        fabSelectImage.setOnClickListener(this);
        isImageSelected = false;
    }
    private void displayCurrentUserInfo() {

        DatabaseReference userDbRef = FirebaseDatabase.getInstance().getReference(Commons.USERS_DB)
                .child(Commons.currentUserKey);

        Log.d("REF", "displayCurrentUserInfo: "+userDbRef.toString());
        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userName = dataSnapshot.child("userName").getValue(String.class);
                    String email = dataSnapshot.child("eMail").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);

                    edtxtEmail.setText(email);
                    edtxtName.setText(userName);
                    edtxtPassword.setText(password);
                    edtxtPhone.setText(phone);
                    if(dataSnapshot.child("userImageUri").exists()){
                        String userImageUri = dataSnapshot.child("userImageUri").getValue(String.class);
                        Picasso.get().load(Uri.parse(userImageUri)).into(imageViewProfile);
                    }else {
                        Toast.makeText(SettingsActivity.this, "NO IMAGE", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR_DISPLAYING_USER", "onCancelled: "+databaseError.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.settings_button_save){
            if(isImageSelected){
                updateAllUserInfo();
            }
            else {
                updateUserWithoutImage();
            }

        }
        else if(v.getId() == R.id.settings_fab_pick_image){
            selectProfileImage();
        }
        else if(v.getId() == R.id.settings_button_close){
            finish();
        }

    }

    private void selectProfileImage() {
        CropImage.activity(imageUri)
                .setAspectRatio(1, 1)
                .start(SettingsActivity.this);
        isImageSelected = true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            imageUri = activityResult.getUri();
            imageViewProfile.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this, "Error selecting the image", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void updateAllUserInfo() {
        validateUserInfo();
        if(isValidated){
            updateStorageAndDatabase();
        }

    }

    private void validateUserInfo() {
        int validFactor = 5;
        int changeValue = 0;
        if(TextUtils.isEmpty(edtxtName.getText().toString())){
            changeValue -= 1;
            Toast.makeText(this, "Please write your name", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(edtxtEmail.getText().toString())){
            changeValue -= 1;
            Toast.makeText(this, "Please write your email", Toast.LENGTH_LONG).show();

        }
        else if(TextUtils.isEmpty(edtxtPassword.getText().toString())){
            changeValue -= 1;
            Toast.makeText(this, "Please write your password", Toast.LENGTH_LONG).show();

        }
        else if(TextUtils.isEmpty(edtxtPhone.getText().toString())){
            changeValue -= 1;
            Toast.makeText(this, "Please write your phone", Toast.LENGTH_LONG).show();

        }
        else if(!isImageSelected){
            changeValue -= 1;
            Toast.makeText(this, "Please select a profile image", Toast.LENGTH_LONG).show();

        }
        validFactor += changeValue;
        Toast.makeText(this, ""+validFactor, Toast.LENGTH_LONG).show();
        if(validFactor == VALID_FACTOR){
            isValidated = true;
        }
        else if(validFactor < VALID_FACTOR){
            isValidated = false;
        }
    }
    private void updateStorageAndDatabase() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Updating profile info..\n=======================");
        dialog.setMessage("Please wait....");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if(imageUri != null){
            final StorageReference profileStorageRef = FirebaseStorage.getInstance()
                    .getReference(Commons.PROFILE_IMAGES_STORE)
                    .child(Commons.currentUser.getUserName().trim()+".JPG");

            StorageTask storageTask = profileStorageRef.putFile(imageUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return profileStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String userImageUri = downloadUri.toString();
                        DatabaseReference userDbRef = FirebaseDatabase.getInstance().
                                getReference(Commons.USERS_DB).child(Commons.currentUserKey);
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("userName", edtxtName.getText().toString());
                        userMap.put("eMail", edtxtEmail.getText().toString());
                        userMap.put("password", edtxtPassword.getText().toString());
                        userMap.put("phone", edtxtPhone.getText().toString());
                        userMap.put("userImageUri", userImageUri);

                        userDbRef.updateChildren(userMap);

                        dialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Your profile has been updated successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        finish();
                    }
                    else {
                        dialog.dismiss();
                        Log.d("task===error===", "onComplete: "+task.getException().toString());
                        Toast.makeText(SettingsActivity.this, "Error updating profile info", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "No image was selected", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUserWithoutImage() {
        DatabaseReference userDbRef = FirebaseDatabase.getInstance()
                .getReference(Commons.USERS_DB).child(Commons.currentUserKey);
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userName", edtxtName.getText().toString());
        userMap.put("eMail", edtxtEmail.getText().toString());
        userMap.put("password", edtxtPassword.getText().toString());
        userMap.put("phone", edtxtPhone.getText().toString());

        userDbRef.updateChildren(userMap);
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(this, "Your data has been updated successfully", Toast.LENGTH_LONG).show();
        finish();
    }
}
