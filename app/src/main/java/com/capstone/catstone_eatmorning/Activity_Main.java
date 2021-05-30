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


        input_ID = (EditText)findViewById(R.id.input_ID);
        input_name = (EditText)findViewById(R.id.input_name);
        input_password = (EditText)findViewById(R.id.input_password);


        button = (Button) findViewById(R.id.btn_commit);

        connected_social_type = getIntent().getStringExtra(Member.CONNECTED_SOCIAL_TYPE);
        connected_social_ID = getIntent().getStringExtra(Member.CONNECTED_SOCIAL_ID);
        if(connected_social_type == null) connected_social_type = "none";
        if(connected_social_ID == null) connected_social_ID = "none";
        input_ID.setText(connected_social_type);
        input_password.setText(connected_social_ID);

    }
}