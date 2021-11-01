package com.capstone.catstone_eatmorning.ui.myinfo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.capstone.catstone_eatmorning.DataManager;
import com.capstone.catstone_eatmorning.Member;
import com.capstone.catstone_eatmorning.R;
import com.capstone.catstone_eatmorning.SHA256;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyInfoFragment extends Fragment {
    private TextView txt_myinfo_id;
    private TextView txt_myinfo_name;
    private TextView txt_myinfo_point;
    private TextView txt_myinfo_pnum;
    private TextView txt_myinfo_email;
    private Button btn_myinfo_insert;

    private EditText edit_myinfo_passwd;
    private EditText edit_myinfo_passwdcheck;
    private MyInfoViewModel galleryViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(MyInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myinfo, container, false);
        txt_myinfo_id = (TextView)root.findViewById(R.id.txt_myinfo_ID);
        txt_myinfo_name = (TextView)root.findViewById(R.id.txt_myinfo_name);
        txt_myinfo_point = (TextView)root.findViewById(R.id.txt_myinfo_point);
        txt_myinfo_pnum = (TextView)root.findViewById(R.id.txt_myinfo_pnum);
        txt_myinfo_email = (TextView)root.findViewById(R.id.txt_myinfo_email);
        btn_myinfo_insert = (Button)root.findViewById(R.id.btn_myinfo_insert);
        edit_myinfo_passwd = (EditText)root.findViewById(R.id.edit_myinfo_passwd);
        edit_myinfo_passwdcheck = (EditText)root.findViewById(R.id.edit_myinfo_passwdcheck);

        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserReference;

        UserReference = rootReference.child("users").child(DataManager.Logined_ID);
        UserReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("Firebase", String.valueOf(task.getResult().getValue()));
                } else {
                    for (DataSnapshot d : task.getResult().getChildren()) {
                        if (d.getKey().equals(Member.ID)) {
                            String string = String.valueOf(d.getValue());
                            txt_myinfo_id.setText(string);
                        } else if (d.getKey().equals(Member.NAME)) {
                            txt_myinfo_name.setText(String.valueOf(d.getValue()));
                        } else if (d.getKey().equals(Member.POINT)) {
                            txt_myinfo_point.setText(String.valueOf(d.getValue()));
                        } else if (d.getKey().equals(Member.PNUM)) {
                            txt_myinfo_pnum.setText(String.valueOf(d.getValue()));
                        }
                        else if (d.getKey().equals(Member.EMAIL)) {
                            txt_myinfo_email.setText(String.valueOf(d.getValue()));
                        }
                    }
                }
            }
        });
        btn_myinfo_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_myinfo_passwd.getText().toString().matches(Member.PASSWORD_CHECK_REG_EXP)){
                    Toast.makeText(getActivity().getApplicationContext(),"올바르지 않은 비밀번호 형식입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edit_myinfo_passwd.getText().toString().equals(edit_myinfo_passwdcheck.getText().toString())){
                    Member.update(DataManager.Logined_ID,Member.PASSWORD, SHA256.encode(edit_myinfo_passwd.getText().toString()));
                    Toast.makeText(getActivity().getApplicationContext(),"비밀번호 변경이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"비밀번호와 비밀번호 확인이 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
            return root;
    }
};