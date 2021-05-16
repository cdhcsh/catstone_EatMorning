package com.capstone.catstone_eatmorning;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    public String username;
    public String email;

    public User(){

    }
    public User(String username, String email){
        this.username = username;
        this.email = email;
    }
    public static void writeNewUser(String userID, String name,String email){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        User user = new User(name,email);
        mDatabase.child("users").child(userID).setValue(user);
    }
}
