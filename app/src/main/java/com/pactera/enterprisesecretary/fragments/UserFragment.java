package com.pactera.enterprisesecretary.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pactera.enterprisesecretary.R;


public class UserFragment extends Fragment {


    public UserFragment() {
    }

    public static UserFragment newInstance(Bundle args) {
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取传入信息
        Bundle bundle = getArguments();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //创建页面视图
        View view = inflater.inflate(R.layout.fragment_user, container,
                false);

        return view;
    }

}
