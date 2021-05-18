package com.capstone.catstone_eatmorning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class Activity_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Login();

    }
    private void Login(){
        DatabaseReference rootDatabase;
        DatabaseReference userDatabase;
        rootDatabase = FirebaseDatabase.getInstance().getReference();
        //소셜로그인 체크
        if(getIntent().getStringExtra(Member.CONNECTED_SOCIAL_TYPE).equals(Member.NONE)){
            //일반 로그인
            String userID = getIntent().getStringExtra(Member.USERID);
            String password = getIntent().getStringExtra(Member.PASSWORD);
            userDatabase = rootDatabase.child("users").child(userID);
            userDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(!task.isSuccessful()){
                        Log.d("Firebase",String.valueOf(task.getResult().getValue()));
                    }
                    else{
                        Log.d(task.getResult().getKey(),String.valueOf(task.getResult().getValue()));
                        if(userID.equals(task.getResult().getKey())){
                            //아이디 존재할 시
                            Log.d("로그인 ","아이디 있십니더!");
                            for(DataSnapshot d : task.getResult().getChildren()){
                                if(d.getKey().equals(Member.PASSWORD)){
                                    String mpassword = String.valueOf(d.getValue());
                                    Log.d("비밀번호 확인 :" , password + " : " + mpassword);
                                    if(password.equals(mpassword)){
                                        //비밀번호 일치할시
                                        Log.d("로그인 성공","로그인 성공!");
                                    }
                                    else{
                                        //비밀번호 불일치할 시
                                        Log.d("로그인 실패","비밀번호가 일치하지 않음");
                                    }
                                }
                            }
                        }
                        else{
                            //아이디가 존재하지 않을때
                            Log.d("로그인 실패", "일치하는 아이디가 존재하지 않음");
                        }
                    }
                }
            });
        }
        else{
            Log.d("?","?");
        }
    }

}