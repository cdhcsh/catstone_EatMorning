package com.capstone.catstone_eatmorning.ui.myinfo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.capstone.catstone_eatmorning.DataManager;
import com.capstone.catstone_eatmorning.Member;
import com.capstone.catstone_eatmorning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyInfoFragment extends Fragment {
    private TextView txt_myinfo_id;
    private TextView txt_myinfo_name;
    private TextView txt_myinfo_point;
    private MyInfoViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(MyInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myinfo, container, false);
        txt_myinfo_id = (TextView)root.findViewById(R.id.txt_myinfo_ID);
        txt_myinfo_name = (TextView)root.findViewById(R.id.txt_myinfo_name);
        txt_myinfo_point = (TextView)root.findViewById(R.id.txt_myinfo_point);

        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserReference;

        UserReference = rootReference.child("users").child(DataManager.Logined_ID);
        UserReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.d("Firebase",String.valueOf(task.getResult().getValue()));
                }
                else{
                    for(DataSnapshot d : task.getResult().getChildren()) {
                        if (d.getKey().equals(Member.ID)) {
                            String string = String.valueOf(d.getValue());
                            txt_myinfo_id.setText(string);
                        }
                        else if(d.getKey().equals(Member.NAME)){
                            txt_myinfo_name.setText(String.valueOf(d.getValue()));
                        }
                        else if(d.getKey().equals(Member.POINT)){
                            txt_myinfo_point.setText(Int.valueOf(d.getValue()));
                    }
                }
            }
        };

            return root;
    }
};