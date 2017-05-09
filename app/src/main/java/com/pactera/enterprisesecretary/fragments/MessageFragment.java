package com.pactera.enterprisesecretary.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.activity.WebActivity;
import com.pactera.enterprisesecretary.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageFragment extends Fragment implements AdapterView.OnItemClickListener{

    private List<Map<String,Object>> itemList = null;
    private ListView messageListView = null;
    private MessageAdapter messageAdapter;

    public MessageFragment() {

    }


    public static MessageFragment newInstance(Bundle args) {
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取传入信息
        Bundle bundle = getArguments();
        this.itemList =  (ArrayList)bundle
                .getParcelableArrayList("itemList");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //创建页面视图
        View view = inflater.inflate(R.layout.fragment_message, container,
                false);
        messageListView = (ListView)view.findViewById(R.id.messageListView);
        messageListView.setOnItemClickListener(this);
        messageAdapter = new MessageAdapter(getActivity(), this.itemList);
        messageListView.setAdapter(messageAdapter);


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("savy","跳转到网页!");
        Intent intent = new Intent(this.getActivity(), WebActivity.class);
        MessageFragment.this.getActivity().startActivity(intent);
    }
}
