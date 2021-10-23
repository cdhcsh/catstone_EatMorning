package com.capstone.catstone_eatmorning;

import android.location.Address;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Vector;

public class Member {
    public static final String ID_CHECK_REG_EXP = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$"; //아이디 형식 정규식
    public static final String PASSWORD_CHECK_REG_EXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,19}$";
    public static final String NAME_CHECK_REG_EXP = "^[가-힣]{2,11}$";

    public static final String USERS = "users";
    public static final String DESTINATIONS = "user_destinations";
    public static final String SUBSCRIBES = "user_subscribes";


    public static final String NAVER = new String("naver");
    public static final String KAKAO = new String("kakao");
    public static final String FACEBOOK = new String("facebook");
    public static final String GOOGLE = new String("google");
    public static final String NONE = new String("none");

    public static final String ID = "user_ID";
    public static final String NAME = "user_name";
    public static final String PASSWORD = "user_password";
    public static final String POINT = "user_point";
    public static final String PNUM = "user_pnum";
    public static final String EMAIL = "user_email";
    public static final String PAYMENT = "user_payment";
    public static final String HEALTH = "user_health";
    public static final String CONNECTED_SOCIAL_TYPE = "user_connected_social_type";
    public static final String CONNECTED_SOCIAL_ID = "user_connected_social_ID";

    public String user_ID;
    public String user_name;
    public String user_password;
    public String user_email = "";
    public int user_point = 0;
    public String user_pnum = "";
    public String user_payment = "";
    public String user_health = "";
    public String user_connected_social_type = "";
    public String user_connected_social_ID = "";
    public HashMap<String,Destination> user_destinations = new HashMap<String,Destination>();
    public HashMap<String,Subscribe> user_subscribes = new HashMap<String,Subscribe>();
    public Member(String user_ID, String user_name, String user_password, String user_email ,String user_pnum,int user_point, String user_payment, String user_health) {
        this.user_ID = user_ID;
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_pnum = user_pnum;
        this.user_email = user_email;
        this.user_point = user_point;
        this.user_payment = user_payment;
        this.user_health = user_health;
    }
    public void addDestination(String destination_name,String destination_address,String destination_detail_address){
        Destination destination = new Destination(destination_name,destination_address,destination_detail_address);
        user_destinations.put(destination_name,destination);
    }
    public void addSubscribe(Subscribe subscribe){
        user_subscribes.put(subscribe.subscribe_date.toString(),subscribe);
    }

    public Member(){

    }
    public void insert(){
        String[] childs = {USERS,user_ID}; // (위치,키이름)
        DataManager.insertData(childs,Member.this); //데이터 입력
    }
}
