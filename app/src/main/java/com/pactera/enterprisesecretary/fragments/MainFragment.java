package com.pactera.enterprisesecretary.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.util.StaticProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainFragment extends Fragment {

    private int height = 0;
    private int width = 0;
    SharedPreferences share = null;
    SharedPreferences.Editor sedit = null;

    private ScrollView mainScrollView = null;

    private List<Map<String,Object>> itemList = null;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(Bundle args) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container,
                false);

//保存全局信息
        share = getActivity().getSharedPreferences(
                StaticProperty.SAVEINFO, Activity.MODE_PRIVATE);
        width = share.getInt(StaticProperty.SCREENWIDTH,480);
        height = share.getInt(StaticProperty.SCREENHEIGHT,800);

        //中部的滚动视图区域
        this.mainScrollView = (ScrollView) view.findViewById(R.id.mainScrollView);
        int itemWidth = width/2;
        int itemHeight = width/4;
        RelativeLayout scrollRelativeLayout = new RelativeLayout(getActivity());//相对布局

        //每个scrollView的子视图
        for (int i = 0; i < this.itemList.size(); i++) {
            Map<String,Object> myMap = this.itemList.get(i);

            View itemView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.scrollview_main, null);

            RelativeLayout.LayoutParams itemLyParam = new RelativeLayout.LayoutParams(
                    itemWidth, itemHeight);
            if(i%2==0) {
                itemLyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }else{
                itemLyParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            itemLyParam.topMargin = (i/2)*itemHeight;
            itemView.setLayoutParams(itemLyParam);

            ImageView itemImageView = (ImageView)itemView.findViewById(R.id.itemMainIconImageView);
            itemImageView.setImageResource((int)myMap.get("image"));
            scrollRelativeLayout.addView(itemView);
        }
        this.mainScrollView.addView(scrollRelativeLayout);


        return view;
    }




}
