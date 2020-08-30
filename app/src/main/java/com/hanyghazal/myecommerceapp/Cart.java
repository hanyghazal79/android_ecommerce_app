package com.hanyghazal.myecommerceapp;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private String userId;
    private Product product;
    private int quantity;
    private double itemTotal;

    public Cart() {

    }

    public Cart(String userId, Product product, int quantity, double itemTotal) {
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.itemTotal = itemTotal;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object>map = new HashMap<>();
        map.put("userId", userId);
        map.put("product", product);
        map.put("quantity", quantity);
        map.put("itemTotal", itemTotal);
        return map;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }
}
