package com.hanyghazal.myecommerceapp;

public class User {
    private String userName;
    private String email;
    private String password;
    private String phone;
    private String userImageUri;
    private  String address;

    public User() {
    }

    public User(String userName, String email, String password, String phone, String address) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public User(String userName, String email, String password, String phone, String userImageUri, String address) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userImageUri = userImageUri;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserImageUri() {
        return userImageUri;
    }

    public void setUserImageUri(String userImageUri) {
        this.userImageUri = userImageUri;
    }
}
