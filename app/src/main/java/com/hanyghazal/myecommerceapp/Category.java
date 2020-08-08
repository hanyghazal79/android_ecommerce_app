package com.hanyghazal.myecommerceapp;

import android.graphics.Bitmap;
import android.net.Uri;

public class Category {

    private String categoryName, categoryImageName;
    private Bitmap categoryImage;
    private String categoryImageDownloadUri;

    public Category() {
    }

    public Category(String categoryName, String categoryImageDownloadUri) {
        this.categoryName = categoryName;
        this.categoryImageDownloadUri = categoryImageDownloadUri;
    }

//    public Category(String categoryName, String categoryImageName) {
//        this.categoryName = categoryName;
//        this.categoryImageName = categoryImageName;
//    }
//
//    public Category(String categoryName, Bitmap bitmap) {
//        this.categoryName = categoryName;
//        this.categoryImage = bitmap;
//    }
//
//    public Category(String categoryName, String categoryImageName, Uri categoryImageDownloadUri) {
//        this.categoryName = categoryName;
//        this.categoryImageName = categoryImageName;
//        this.categoryImageDownloadUri = categoryImageDownloadUri;
//    }

//    public Category(String categoryName, String categoryImageName, Bitmap categoryImage, Uri categoryImageDownloadUri) {
//        this.categoryName = categoryName;
//        this.categoryImageName = categoryImageName;
//        this.categoryImage = categoryImage;
//        this.categoryImageDownloadUri = categoryImageDownloadUri;
//    }

    public String getCategoryImageDownloadUri() {
        return categoryImageDownloadUri;
    }

    public void setCategoryImageDownloadUri(String categoryImageDownloadUri) {
        this.categoryImageDownloadUri = categoryImageDownloadUri;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImageName() {
        return categoryImageName;
    }

    public void setCategoryImageName(String categoryImageName) {
        this.categoryImageName = categoryImageName;
    }

    public Bitmap getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(Bitmap categoryImage) {
        this.categoryImage = categoryImage;
    }
}
