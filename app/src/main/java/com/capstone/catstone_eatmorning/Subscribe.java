package com.capstone.catstone_eatmorning;

public class Subscribe {
    public static final String DATE = "subscribe_date";
    public static final String MENU_NAME = "subscribe_menu_name";
    public static final String PRICE = "subscribe_price";

    public Integer subscribe_date;
    public String subscribe_menu_name;
    public String subscribe_price;

    public Subscribe(Integer subscribe_date, String subscribe_menu_name, String subscribe_price) {
        this.subscribe_date = subscribe_date;
        this.subscribe_menu_name = subscribe_menu_name;
        this.subscribe_price = subscribe_price;
    }
    public String toString(){
        return subscribe_date+"";
    }
    public void insert(){
        insert(DataManager.Logined_ID);

    }
    public static void delete(String date){
        String[] childs = {Member.USERS,DataManager.Logined_ID,Member.SUBSCRIBES,date}; // (위치,키이름)
        DataManager.insertData(childs,null); //데이터 입력
    }
    public void insert(String user_ID){
        String[] childs = {Member.USERS,user_ID,Member.SUBSCRIBES,subscribe_date.toString()}; // (위치,키이름)
        DataManager.insertData(childs,Subscribe.this); //데이터 입력

    }
}
