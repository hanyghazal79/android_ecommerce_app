package com.hanyghazal.myecommerceapp;

import java.util.Date;

public class Order {

    private String orderKey;
    private Product[] products;
    private User user;
    private Date date;

    public Order(){

    }

    public Order(String orderKey, Product[] products, User user, Date date) {
        this.orderKey = orderKey;
        this.products = products;
        this.user = user;
        this.date = date;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

