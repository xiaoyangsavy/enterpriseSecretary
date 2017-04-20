package com.pactera.enterprisesecretary.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageFragment extends Fragment {

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
        messageAdapter = new MessageAdapter(getActivity(), this.itemList);
        messageListView.setAdapter(messageAdapter);
//        messageListView.setOnItemClickListener(this);

        return view;
    }

}
