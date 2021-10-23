package com.capstone.catstone_eatmorning;

public class Review {

    public static final String NAME = "review_name";
    public static final String ID = "review_user_id";
    public static final String GRADE = "review_grade";
    public static final String DATE = "review_date";
    public static final String IMAGE = "review_image";
    public static final String CONTENT = "review_content";

    String review_name;
    String review_user_id;
    Integer review_grade;
    String review_date;
    String review_image;
    String review_content;

    public Review(String review_name, String review_user_id, Integer review_grade, String review_date, String review_image, String review_content) {
        this.review_name = review_name;
        this.review_user_id = review_user_id;
        this.review_grade = review_grade;
        this.review_date = review_date;
        this.review_image = review_image;
        this.review_content = review_content;
    }


    public void insert(String menu_name){
        String[] childs = {Menu.MENUS,menu_name,Menu.REVIEWS,review_name}; // (위치,키이름)
        DataManager.insertData(childs,Review.this); //데이터 입력

    }
}
