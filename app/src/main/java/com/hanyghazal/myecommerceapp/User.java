package com.hanyghazal.myecommerceapp;

import android.graphics.Bitmap;

public class User {
    private String userName;
    private String eMail;
    private String password;
    private String phone;
    private String userImageUri;

    public User() {
    }

    public User(String userName, String eMail, String password, String phone) {
        this.userName = userName;
        this.eMail = eMail;
        this.password = password;
        this.phone = phone;
    }

    public User(String userName, String eMail, String password, String phone, String userImageUri) {
        this.userName = userName;
        this.eMail = eMail;
        this.password = password;
        this.phone = phone;
        this.userImageUri = userImageUri;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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
