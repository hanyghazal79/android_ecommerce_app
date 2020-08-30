package com.hanyghazal.myecommerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {
    DatabaseReference dbRef;
    ArrayList<Cart> carts =  new ArrayList<>();
    public FirebaseHelper(DatabaseReference dbRef){
        this.dbRef = dbRef;
    }
    public void fetchData(DataSnapshot snapshot){

    }
    public ArrayList<Cart> getCarts(){
        dbRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        carts.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Cart cart = ds.getValue(Cart.class);
//            if(cart.getUserId().equals(Commons.currentUserKey)){
                            carts.add(cart);
//            }
                        }                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        return carts;
    }
}
