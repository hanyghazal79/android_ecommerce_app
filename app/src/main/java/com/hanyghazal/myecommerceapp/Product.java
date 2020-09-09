package com.hanyghazal.myecommerceapp;

public class Product {
    private String productKey;
    private String productName;
    private String productCategory;
    private String productPrice;
    private String productDescription;
    private String productImageDownloadUri;
    private String productSellerName;
    private String productShippingValue;

    public Product() {
    }

    public Product(String productKey, String productName, String productCategory, String productPrice, String productDescription, String productImageDownloadUri) {
        this.productKey = productKey;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productImageDownloadUri = productImageDownloadUri;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImageDownloadUri() {
        return productImageDownloadUri;
    }

    public void setProductImageDownloadUri(String productImageDownloadUri) {
        this.productImageDownloadUri = productImageDownloadUri;
    }
}
