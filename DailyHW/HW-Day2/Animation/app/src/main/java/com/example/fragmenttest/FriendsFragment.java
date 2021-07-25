package com.example.fragmenttest;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsFragment extends Fragment {
    private TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String[] flowerList = new Datasource(getActivity()).getFlowerList();
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new ProjectsAdapter(flowerList));

        textView = getView().findViewById(R.id.textview3);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2);
        ObjectAnimator.ofPropertyValuesHolder(textView, pvhX, pvhY).setDuration(3000).start();
    }
}
