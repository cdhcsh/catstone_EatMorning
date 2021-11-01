package com.capstone.catstone_eatmorning.ui.subscribes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.capstone.catstone_eatmorning.R;

public class SubscribesFragment extends Fragment {
    private SubscribesViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SubscribesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscribes, container, false);
        return root;
    }
}