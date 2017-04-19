package com.pactera.enterprisesecretary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pactera.enterprisesecretary.util.StaticProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int height = 0;
    private int width = 0;
    SharedPreferences share = null;
    SharedPreferences.Editor sedit = null;

    private ScrollView mainScrollView = null;

    private List<Map<String, String>> mainList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //测试数据
        this.mainList = new ArrayList<Map<String, String>>();

        for (int i = 0; i < 5; i++) {
            Map<String, String> myMap = new HashMap<String, String>();
            myMap.put("name", "加载中");
            myMap.put("description", "加载中......");
            this.mainList.add(myMap);
        }


        //保存全局信息
        share = MainActivity.this.getSharedPreferences(
                StaticProperty.SAVEINFO, Activity.MODE_PRIVATE);
        //获取屏幕尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        sedit = share.edit();
        sedit.putInt(StaticProperty.SCREENWIDTH, width);//保存屏幕宽度
        sedit.putInt(StaticProperty.SCREENHEIGHT, height);//保存屏幕高度
        sedit.commit();//提交保存信息


        this.mainScrollView = (ScrollView) super.findViewById(R.id.mainScrollView);
        int itemWidth = width/2;
        int itemHeight = width/4;
        RelativeLayout scrollRelativeLayout = new RelativeLayout(this);//相对布局
        for (int i = 0; i < this.mainList.size(); i++) {
            Map<String,String> myMap = this.mainList.get(i);

           View itemView = LayoutInflater.from(this).inflate(
                    R.layout.scrollview_main, null);


//            RelativeLayout itemRelativeLayout = new RelativeLayout(this);//相对布局
//            itemRelativeLayout.setBackgroundColor(Color.RED);
            RelativeLayout.LayoutParams itemLyParam = new RelativeLayout.LayoutParams(
                    itemWidth, itemHeight);
            if(i%2==0) {
                itemLyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }else{
                itemLyParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            itemLyParam.topMargin = (i/2)*itemHeight;
            itemView.setLayoutParams(itemLyParam);

//            ImageView icoImageView = new ImageView(this);//图标
//            TextView titleTextView = new TextView(this);//标题
//            TextView descriptionTextView = new TextView(this);//描述
//
//            icoImageView.setImageResource(R.mipmap.ic_launcher);
//            icoImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            itemRelativeLayout.addView(icoImageView);
//
//            titleTextView.setText(myMap.get("name"));
//            itemRelativeLayout.addView(titleTextView);
//
//            descriptionTextView.setText(myMap.get("description"));
//            itemRelativeLayout.addView(descriptionTextView);
            scrollRelativeLayout.addView(itemView);
        }
        this.mainScrollView.addView(scrollRelativeLayout);

    }
}
