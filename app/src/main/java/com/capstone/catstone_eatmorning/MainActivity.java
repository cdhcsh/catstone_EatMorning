package com.capstone.catstone_eatmorning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhn.android.naverlogin.OAuthLogin;
import com.capstone.catstone_eatmorning.Member;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    OAuthLogin mOAuthLoginModule;
    Context mContext;
    private EditText input_ID;
    private EditText input_password;
    private EditText input_name;
    private Button button;

    private String connected_social_type = null;
    private String connected_social_ID = null;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

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
    protected  void onStart(){
        super.onStart();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Member.writeNewUser(input_ID.getText().toString(),input_name.getText().toString(),SHA256.encode(input_password.getText().toString()));
            }
        });
    }
}