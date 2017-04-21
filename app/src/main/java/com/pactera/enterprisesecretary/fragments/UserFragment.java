package com.pactera.enterprisesecretary.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.adapter.MessageAdapter;
import com.pactera.enterprisesecretary.adapter.UserAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserFragment extends Fragment {

    private List<Map<String, Object>> itemList = null;
    private ListView userListView = null;
    private UserAdapter userAdapter;

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
        this.itemList = (ArrayList) bundle
                .getParcelableArrayList("itemList");
        //初始化选项列表
        this.itemList = new ArrayList<Map<String, Object>>();

        Map<String, Object> myMap = null;
        myMap = new HashMap<String, Object>();
        myMap.put("name", "个人信息");
        myMap.put("description", "输入你相关信息 有所变更及时改");
        myMap.put("image", R.drawable.user_info);
        this.itemList.add(myMap);

        myMap = new HashMap<String, Object>();
        myMap.put("name", "工作中心");
        myMap.put("description", "随时知道职位 牢记工作职责");
        myMap.put("image", R.drawable.user_work);
        this.itemList.add(myMap);

        myMap = new HashMap<String, Object>();
        myMap.put("name", "公司地址");
        myMap.put("description", "告诉企秘公司名 掌握实时路况");
        myMap.put("image", R.drawable.user_location);
        this.itemList.add(myMap);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //创建页面视图
        View view = inflater.inflate(R.layout.fragment_user, container,
                false);

        userListView = (ListView) view.findViewById(R.id.userListView);
        userAdapter = new UserAdapter(getActivity(), this.itemList);
        userListView.setAdapter(userAdapter);

        return view;
    }

}
