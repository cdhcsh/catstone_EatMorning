package com.capstone.catstone_eatmorning;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.capstone.catstone_eatmorning.ui.categories.CategoriesFragment;
import com.capstone.catstone_eatmorning.ui.myinfo.MyInfoFragment;
import com.capstone.catstone_eatmorning.ui.subscribes.SubscribesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Activity_Menu extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_catagories, R.id.nav_myInfo, R.id.nav_subscribes, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                String title = null;
                if(item.getItemId() == R.id.nav_logout){
                    DataManager.Logined_ID = null;
                    startActivity(new Intent(getApplicationContext(),Activity_LoginPage.class));
                    finish();
                }

                else if(item.getItemId() == R.id.nav_catagories){
                    fragment = new CategoriesFragment();
                    title = item.getTitle().toString();
                }
                else if(item.getItemId() == R.id.nav_myInfo){
                    fragment = new MyInfoFragment();
                    title = item.getTitle().toString();
                }
                else if(item.getItemId() == R.id.nav_subscribes){
                    fragment = new SubscribesFragment();
                    title = item.getTitle().toString();
                }
                if(fragment != null){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment,fragment);
                    ft.commit();
                    getSupportActionBar().setTitle(title);
                }
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        // header에 있는 리소스 가져오기
        TextView text = (TextView) header.findViewById(R.id.ID);

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
                        if (d.getKey().equals(Member.NAME)) {
                            String name = String.valueOf(d.getValue());
                            text.setText(name);
                        }
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}