package com.capstone.catstone_eatmorning.ui.subscribes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.capstone.catstone_eatmorning.DataManager;
import com.capstone.catstone_eatmorning.Member;
import com.capstone.catstone_eatmorning.Menu;
import com.capstone.catstone_eatmorning.R;
import com.capstone.catstone_eatmorning.Subscribe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

public class SubscribesFragment extends Fragment {
    private SubscribesViewModel slideshowViewModel;
    private  ListView listView;
    private ListViewAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SubscribesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscribes,container, false);
        listView = (ListView)root.findViewById(R.id.listview_subscribes);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("구독 취소하기").setMessage("구독을 취소 하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListData data = adapter.listData.get(position);
                        Subscribe.delete(data.description);
                        Toast.makeText(getActivity().getApplicationContext(),"구독을 취소하셨습니다 .감사합니다.",Toast.LENGTH_SHORT).show();
                        adapter.listData.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(), "취소하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }
        });
        adapter = new ListViewAdapter(getActivity());
        listView.setAdapter(adapter);

        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserReference;

        UserReference = rootReference.child(Member.USERS).child(DataManager.Logined_ID).child(Member.SUBSCRIBES);
        UserReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("Firebase", String.valueOf(task.getResult().getValue()));
                } else {
                    for(DataSnapshot d : task.getResult().getChildren()){
                        String name =null ;
                        String description = null;
                        String price = null ;
                        for(DataSnapshot c : d.getChildren()){
                            if(c.getKey().equals(Subscribe.MENU_NAME)){
                                name = String.valueOf(c.getValue());
                            }
                            else if(c.getKey().equals(Subscribe.DATE)){
                                description = String.valueOf(c.getValue());
                            }
                            else if(c.getKey().equals(Subscribe.PRICE)){
                                price = String.valueOf(c.getValue()) + "원";
                            }
                        }
                        adapter.addItem(name,description,price);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });

        return root;
    }
    class ViewHolder{
        public TextView name;
        public TextView description;
        public TextView price;
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
                convertView = inflater.inflate(R.layout.custom_listview_item,null);//레이아웃가져온다

                holder.name = (TextView)convertView.findViewById(R.id.txt_listview_menu_name);
                holder.description = (TextView)convertView.findViewById(R.id.txt_listview_menu_description);
                holder.price = (TextView)convertView.findViewById(R.id.txt_listview_menu_price);

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            ListData data = listData.get(posion);

            holder.name.setText(data.name);
            holder.description.setText(data.description);
            holder.price.setText(data.price);

            return convertView;
        }
        public void addItem(String name,String description,String price){
            ListData data = null;
            data = new  ListData();
            data.name = name;
            data.description = description;
            data.price = price;
            listData.add(data);
        }
        public void dataChange(){
            this.notifyDataSetChanged();
        }
    }
}
class ListData {
    public String name;
    public String description;
    public String price;
    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();
        public int compare(ListData o1,ListData o2) {
            return sCollator.compare(o1.name,o2.name);
        }
    };
}