package com.capstone.catstone_eatmorning;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Member {
    public String userID;
    public String username;
    public String password;

    public Member(String userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public Member(){

    }
    public static void writeNewUser(String userID, String username,String password){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Member user = new Member(userID,username,password);
        mDatabase.child("users").child(userID).setValue(user);
    }
}
