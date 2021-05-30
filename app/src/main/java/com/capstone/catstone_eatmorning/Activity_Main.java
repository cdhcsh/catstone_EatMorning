package com.capstone.catstone_eatmorning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Activity_Main extends AppCompatActivity {

    private EditText input_ID;
    private EditText input_password;
    private EditText input_name;
    private Button button;

    private String connected_social_type = null;
    private String connected_social_ID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}