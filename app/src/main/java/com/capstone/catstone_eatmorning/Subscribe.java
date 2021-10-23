package com.capstone.catstone_eatmorning;

public class Subscribe {
    public static final String DATE = "subscribe_date";
    public static final String MENU_NAME = "subscribe_menu_name";
    public static final String INFO = "subscribe_info";

    Integer subscribe_date;
    String subscribe_menu_name;
    String subscribe_info;

    public Subscribe(Integer subscribe_date, String subscribe_menu_name, String subscribe_info) {
        this.subscribe_date = subscribe_date;
        this.subscribe_menu_name = subscribe_menu_name;
        this.subscribe_info = subscribe_info;
    }

    public void insert(){
        insert(DataManager.Logined_ID);

    }

    public void insert(String user_ID){
        String[] childs = {Member.USERS,user_ID,Member.SUBSCRIBES,subscribe_date.toString()}; // (위치,키이름)
        DataManager.insertData(childs,Subscribe.this); //데이터 입력

    }
}
