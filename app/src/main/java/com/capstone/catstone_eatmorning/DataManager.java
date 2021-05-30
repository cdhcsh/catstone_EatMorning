package com.capstone.catstone_eatmorning;

import android.provider.ContactsContract;
import android.se.omapi.Session;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Vector;

public class DataManager {
    public static String Logined_ID = null;
    static DataSnapshot dataSnapshot;
    public static void insertData(String[] childs,Object data){
        getDataReference(childs).setValue(data);
    }
    public static DatabaseReference getDataReference(String[] childs){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        for (String child : childs){
            databaseReference = databaseReference.child(child);
        }
        return databaseReference;
    }
}
