package com.capstone.catstone_eatmorning;

public class Meal {

    public static final String NAME = "meal_name";
    public static final String INFO = "meal_info";
    public static final String DESCRIPTION = "meal_description";
    public static final String IMAGE = "meal_image";

    String meal_name;
    String meal_info;
    String meal_description;
    String meal_image;

    public Meal(String meal_name, String meal_info, String meal_description, String meal_image) {
        this.meal_name = meal_name;
        this.meal_info = meal_info;
        this.meal_description = meal_description;
        this.meal_image = meal_image;
    }

    public void insert(String menu_name){
        String[] childs = {Menu.MENUS,menu_name,Menu.MEAL,meal_name}; // (위치,키이름)
        DataManager.insertData(childs,Meal.this); //데이터 입력

    }
}
