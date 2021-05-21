package com.capstone.catstone_eatmorning;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class Activity_register extends AppCompatActivity {
    EditText input_address;
    private static final int SEARCH_ADDRESS_ACRIVITY = 10000;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        input_address = (EditText)findViewById(R.id.input_address);
        initInput_address();
    }
    private void initInput_address(){
        input_address = (EditText)findViewById(R.id.input_address);
        Button btn_adress = (Button)findViewById(R.id.btn_address);
        btn_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_register.this,activity_address.class);
                startActivityForResult(i,SEARCH_ADDRESS_ACRIVITY);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
                if(resultCode == RESULT_OK){
                    String data = intent.getExtras().getString("data");
                        input_address.setText(data);
                }
    }
}