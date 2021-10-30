package com.capstone.catstone_eatmorning.ui.categories;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.capstone.catstone_eatmorning.DataManager;
import com.capstone.catstone_eatmorning.Member;
import com.capstone.catstone_eatmorning.Menu;
import com.capstone.catstone_eatmorning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CategoriesFragment extends Fragment {
    private ListView listView;
    private ListViewAdapter adapter;
    private CategoriesViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(CategoriesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        listView = (ListView)root.findViewById(R.id.listview_menus);
        listView.setAdapter(adapter);

        adapter.addItem("SEX","좋아");
        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserReference;

        UserReference = rootReference.child("menus");
        UserReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("Firebase", String.valueOf(task.getResult().getValue()));
                } else {
                    for(DataSnapshot d : task.getResult().getChildren()){
                        for(DataSnapshot c : d.getChildren()){

                            if(c.getKey().equals(Menu.NAME)){

                            }
                        }
                    }
                }
            }
        });

        return root;
    }
    class ViewHolder{
        public TextView name;
        public TextView description;
    }
    class ListViewAdapter extends BaseAdapter{
        private Context mContext = null;
        public ArrayList<ListData> listData = new ArrayList<ListData>();
        public ListViewAdapter(Context context){
            super();
            this.mContext = context;
        }
        public int getCount(){
            return listData.size();
        }
        public Object getItem(int position){
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public View getView(int posion,View convertView,ViewGroup parent){
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.custom_listview_item,null);

                holder.name = (TextView)convertView.findViewById(R.id.txt_listview_menu_name);
                holder.description = (TextView)convertView.findViewById(R.id.txt_listview_menu_description);

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            ListData data = listData.get(posion);

            holder.name.setText(data.name);
            holder.name.setText(data.description);

            return convertView;
        }
        public void addItem(String name,String description){
            ListData data = null;
            data = new ListData();
            data.name = name;
            data.description = description;

            listData.add(data);
        }
        public void dataChange(){
            adapter.notifyDataSetChanged();
        }
    }
}
class ListData{
    public String name;
    public String description;
    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();
        public int compare(ListData o1,ListData o2) {
            return sCollator.compare(o1.name,o2.name);
        }
    };
}