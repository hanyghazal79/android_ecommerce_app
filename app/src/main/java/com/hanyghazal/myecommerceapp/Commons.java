package com.hanyghazal.myecommerceapp;

import android.net.Uri;

import java.util.ArrayList;

public class Commons {

    public static final String PRODUCT_IMAGES_STORE = "PRODUCT IMAGES";
    public static final String PRODUCTS_DB = "PRODUCTS";
    public static final String PRODUCT_NAME_KEY = "PRODUCT_NAME_KEY";
    public static final String PRODUCT_KEY = "PRODUCT_KEY";

    public static String dbName;
    public static User currentUser;
    public static Product currentProduct;
    public static String currentUserKey;

    public static ArrayList<String> categoryNames = new ArrayList<>();
    public static ArrayList<String> categoryImageNames = new ArrayList<>();
    public static final String ADMINS_DB = "ADMINS";
    public static final String USERS_DB = "USERS";
    public static final String CARTS_DB = "CARTS";

    public static final String CATEGORIES_DB = "CATEGORIES";
    public static final String DB_KEY = "DB";
    public static final String USER_NAME_KEY = "USERNAME";
    public static final String E_MAIL_KEY = "EMAIL";
    public static final String PASS_WORD_KEY = "PASSWORD";
    public static final String CATEGORY_IMAGES_STORE = "CATEGORY IMAGES";
    public static final String PROFILE_IMAGES_STORE = "PROFILE IMAGES";


    public static String selectedCategory;
    public static String currencySymbol = "EGP";
}
