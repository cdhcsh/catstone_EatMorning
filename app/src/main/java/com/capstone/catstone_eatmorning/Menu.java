package com.capstone.catstone_eatmorning;

import java.util.HashMap;

public class Menu {

    public static final String MENUS = "menus";
    public static final String REVIEWS = "menu_reviews";
    public static final String MEAL = "menu_meals";

    public static final String NAME = "menu_name";
    public static final String PRICE = "menu_price";
    public static final String DESCRIPTION = "menu_description";
    public static final String IMAGE = "menu_image";


    String menu_name;
    Integer menu_price;
    String menu_description;
    String menu_image;
    //Menu_reviews;
    //Menu_meals;

    HashMap<String,Review> menu_reviews = new HashMap<String,Review>();
    HashMap<String,Meal> menu_meals = new HashMap<String,Meal>();

    public Menu(String menu_name, Integer menu_price, String menu_description, String menu_image) {
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_description = menu_description;
        this.menu_image = menu_image;
    }


    public void insert(){
        String[] childs = {MENUS,menu_name}; // (위치,키이름)
        DataManager.insertData(childs,Menu.this); //데이터 입력

    }

    public void addReview(Review review){
        menu_reviews.put(review.review_date.toString(),review);
    }
    public void addMeal(Meal meal){
        menu_meals.put(meal.meal_name.toString(),meal);
    }

}
