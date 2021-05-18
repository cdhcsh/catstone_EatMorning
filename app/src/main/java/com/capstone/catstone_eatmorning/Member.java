package com.capstone.catstone_eatmorning;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Member {
    public static final String NAVER = new String("naver");
    public static final String KAKAO = new String("kakao");
    public static final String FACEBOOK = new String("facebook");
    public static final String GOOGLE = new String("google");
    public static final String NONE = new String("none");
    public static final String CONNECTED_SOCIAL_TYPE = "connected_social_type";
    public static final String CONNECTED_SOCIAL_ID = "connected_social_ID";
    public static final String USERID = "userID";
    public static final String PASSWORD = "password";

    public String userID;
    public String username;
    public String password;
    public String connected_social_type = null;
    public String connected_social_ID = null;

    public Member(String userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public Member(String userID, String username, String password, String connected_social_type, String connected_social_ID) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.connected_social_type = connected_social_type;
        this.connected_social_ID = connected_social_ID;
    }

    public Member(){

    }
    public static void writeNewUser(String userID, String username,String password){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Member user = new Member(userID,username,password,"","");
        mDatabase.child("members").child(userID).setValue(user);
    }
    public static void writeNewUser(String userID, String username,String password,String connected_social_type,String connected_social_ID){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Member user = new Member(userID,username,password,connected_social_type,connected_social_ID);
        mDatabase.child("members").child(userID).setValue(user);
    }
}
