package com.capstone.catstone_eatmorning;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class Activity_register extends AppCompatActivity {
    private TextView text_password_reg;
    private TextView text_password_check;
    private TextView text_email_reg;
    private TextView text_pnum_reg;
    private TextView text_name_reg;
    private EditText input_user_ID;
    private EditText input_user_name;
    private EditText input_user_password;
    private EditText input_user_passwordcheck;
    private EditText input_user_email;
    private EditText input_user_pnum;
    private EditText input_user_address;
    private EditText input_user_detail_address;
    private CheckBox checkbox_agree;
    Button btn_user_IDcheck;
    Button btn_resister_confirm;

    boolean bool_password_reg = false;
    boolean bool_password_check = false;
    boolean bool_ID_check = false;
    boolean bool_email_reg = false;
    boolean bool_pnum_reg = false;
    boolean bool_name_reg = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();

    }

    private void initComponents(){
        text_password_reg = (TextView)findViewById(R.id.text_password_reg);
        text_password_check = (TextView)findViewById(R.id.text_password_check);
        text_email_reg = (TextView)findViewById(R.id.text_email_reg);
        text_pnum_reg = (TextView)findViewById(R.id.text_pnum_reg);
        text_name_reg = (TextView)findViewById(R.id.text_name_reg);
        input_user_ID = (EditText)findViewById(R.id.input_user_ID);
        input_user_name = (EditText)findViewById(R.id.input_user_name);
        input_user_password = (EditText)findViewById(R.id.input_user_password);
        input_user_passwordcheck = (EditText)findViewById(R.id.input_user_password_check);
        input_user_email = (EditText)findViewById(R.id.input_user_email);
        input_user_pnum = (EditText)findViewById(R.id.input_user_pnum);
        input_user_pnum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        input_user_address = (EditText)findViewById(R.id.input_user_address);
        input_user_detail_address = (EditText)findViewById(R.id.input_user_detail_address);
        checkbox_agree = (CheckBox)findViewById(R.id.checkbox_agree);
        checkbox_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_Register_require();
            }
        });
        btn_user_IDcheck = (Button)findViewById(R.id.btn_user_IDcheck);
        btn_resister_confirm = (Button)findViewById(R.id.btn_register_confirm);

        initInput_user_name_reg();
        initInput_user_password_reg();
        initInput_user_password_check();
        initInput_user_email_reg();
        initInput_user_pnum_reg();
        initBtn_user_IDcheck();
        initBtn_Register_confirm();
        check_Register_require();
    }
    private void check_Register_require(){
        String user_address = input_user_address.getText().toString();
        boolean check_agree = checkbox_agree.isChecked();
        //회원가입 조건을 만족할때
        if(bool_ID_check && bool_name_reg && bool_password_reg && bool_password_check && bool_email_reg && bool_pnum_reg
        && user_address.length() > 0 && check_agree){
                btn_resister_confirm.setText(R.string.btn_register);
                btn_resister_confirm.setTextSize(Dimension.SP, 18);
                btn_resister_confirm.setEnabled(true);
        }
        else{
                btn_resister_confirm.setText("아직 입력하지 않은 내용이 있습니다.");
                btn_resister_confirm.setTextSize(Dimension.SP, 15);
                btn_resister_confirm.setEnabled(false);
        }
    }
    private void initInput_user_name_reg(){
        input_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(input_user_name.getText().toString().matches(Member.NAME_CHECK_REG_EXP)){
                    text_name_reg.setText(R.string.name_reg_success);
                    text_name_reg.setTextColor(Color.parseColor(getString(R.string.color_green)));
                    bool_name_reg = true;
                }
                else{
                    text_name_reg.setText(R.string.name_reg_fail);
                    text_name_reg.setTextColor(Color.parseColor(getString(R.string.color_red)));
                    bool_name_reg = false;
                }
                check_Register_require();
            }
        });
    }
    private void initInput_user_pnum_reg(){
        input_user_pnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Patterns.PHONE.matcher(input_user_pnum.getText().toString()).matches()){
                    text_pnum_reg.setText(R.string.pnum_reg_success);
                    text_pnum_reg.setTextColor(Color.parseColor(getString(R.string.color_green)));
                    bool_pnum_reg = true;
                }
                else{
                    text_pnum_reg.setText(R.string.pnum_reg_fail);
                    text_pnum_reg.setTextColor(Color.parseColor(getString(R.string.color_red)));
                    bool_pnum_reg = false;
                }
                check_Register_require();
            }
        });
    }
    private void initInput_user_email_reg(){
        input_user_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Patterns.EMAIL_ADDRESS.matcher(input_user_email.getText().toString()).matches()){
                    text_email_reg.setText(R.string.email_reg_success);
                    text_email_reg.setTextColor(Color.parseColor(getString(R.string.color_green)));
                    bool_email_reg = true;
                }
                else{
                    text_email_reg.setText(R.string.email_reg_fail);
                    text_email_reg.setTextColor(Color.parseColor(getString(R.string.color_red)));
                    bool_email_reg = false;
                }
                check_Register_require();
            }
        });
    }
    private void initInput_user_password_reg(){
        input_user_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(input_user_password.getText().toString().matches(Member.PASSWORD_CHECK_REG_EXP)){
                    text_password_reg.setText(R.string.password_reg_success);
                    text_password_reg.setTextColor(Color.parseColor(getString(R.string.color_green)));
                    bool_password_reg = true;
                }
                else{
                    text_password_reg.setText(R.string.password_reg_fail);
                    text_password_reg.setTextColor(Color.parseColor(getString(R.string.color_red)));
                    bool_password_reg = false;
                }
                check_Register_require();
            }
        });
    }
    private void initInput_user_password_check(){
        TextWatcher password_check = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(input_user_password.getText().toString().equals(input_user_passwordcheck.getText().toString())){
                    text_password_check.setText(R.string.password_check_success);
                    text_password_check.setTextColor(Color.parseColor(getString(R.string.color_green)));
                    bool_password_check = true;
                }
                else{
                    text_password_check.setText(R.string.password_check_fail);
                    text_password_check.setTextColor(Color.parseColor(getString(R.string.color_red)));
                    bool_password_check = false;
                }
                check_Register_require();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        input_user_password.addTextChangedListener(password_check);
        input_user_passwordcheck.addTextChangedListener(password_check);
    }
    private void initBtn_Register_confirm(){
        btn_resister_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = input_user_ID.getText().toString();
                String name = input_user_name.getText().toString();
                String password = input_user_password.getText().toString();
                String email = input_user_email.getText().toString();
                String pnum = input_user_pnum.getText().toString();
                String address = input_user_address.getText().toString();
                String detail_address = input_user_detail_address.getText().toString();

                Member member = new Member(ID,name,SHA256.encode(password),email,pnum,0,"","");
                member.addDestination("기본 배송지",address,detail_address);

                member.insert();
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_register.this);
                builder.setTitle("회원가입 완료");
                builder.setMessage("회원가입에 성공했습니다.\n로그인 화면으로 이동합니다.");
                AlertDialog alertDialog = builder.create();
                //알림 닫을때 activity 종료
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Activity_register.this.finish();
                    }
                });
                alertDialog.show();
            }
        });
    }
    private void initBtn_user_IDcheck(){
        btn_user_IDcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = input_user_ID.getText().toString();
                if(bool_ID_check){
                    bool_ID_check = false;
                    input_user_ID.setEnabled(true);
                    btn_user_IDcheck.setEnabled(true);
                    btn_user_IDcheck.setText("중복확인");
                    check_Register_require();
                }
                else {
                    if (!IDRegCheck(ID)) {
                        Toast.makeText(getApplicationContext(), "아이디는 영문,숫자,'_' 로 구성된 5~12글자이여야 합니다.", Toast.LENGTH_SHORT).show();
                        Log.d("아이디 중복확인 실패", "올바르지 않은 아이디 형식");
                        check_Register_require();
                        return;
                    }
                    String[] childs = {Member.USERS, ID};
                    DatabaseReference databaseReference = DataManager.getDataReference(childs);
                    databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                //연결실패
                                Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.d("아이디 중복 테스트", String.valueOf(task.getResult().getValue()));
                                if (task.getResult().exists()) {
                                    Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    Log.d("아이디 중복 검사 결과", "아이디 중복됨!");
                                } else {
                                    Toast.makeText(getApplicationContext(), "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    Log.d("아이디 중복 검사 결과", "아이디 중복안됨!");
                                    bool_ID_check = true;
                                    input_user_ID.setEnabled(false);
                                    btn_user_IDcheck.setText("확인완료");

                                }
                                check_Register_require();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean IDRegCheck(String ID){ //아이디 정규식 검사
        return ID.matches(Member.ID_CHECK_REG_EXP);
    }
    private boolean passwordRegCheck(String password){//비밀번호 정규식 검사
        return password.matches(Member.PASSWORD_CHECK_REG_EXP);
    }
}