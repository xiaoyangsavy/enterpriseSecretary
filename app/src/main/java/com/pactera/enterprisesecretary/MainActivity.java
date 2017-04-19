package com.pactera.enterprisesecretary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pactera.enterprisesecretary.util.StaticProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int height = 0;
    private int width = 0;
    SharedPreferences share = null;
    SharedPreferences.Editor sedit = null;

    private ScrollView mainScrollView = null;

    private LinearLayout mainButtonOne = null;
    private ImageView mainButtonIconOne = null;
    private TextView mainButtonTextOne = null;

    private LinearLayout mainButtonTwo = null;
    private ImageView mainButtonIconTwo = null;
    private TextView mainButtonTextTwo = null;

    private LinearLayout mainButtonThree = null;
    private ImageView mainButtonIconThree = null;
    private TextView mainButtonTextThree = null;
    private Toast myToast;


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


        //中部的滚动视图区域
        this.mainScrollView = (ScrollView) super.findViewById(R.id.mainScrollView);
        int itemWidth = width/2;
        int itemHeight = width/4;
        RelativeLayout scrollRelativeLayout = new RelativeLayout(this);//相对布局

        //每个scrollView的子视图
        for (int i = 0; i < this.mainList.size(); i++) {
            Map<String,String> myMap = this.mainList.get(i);

           View itemView = LayoutInflater.from(this).inflate(
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

            scrollRelativeLayout.addView(itemView);
        }
        this.mainScrollView.addView(scrollRelativeLayout);



        //底部按钮区域
        this.mainButtonOne = (LinearLayout)super.findViewById(R.id.mainButtonOne);
        this.mainButtonOne.setOnClickListener(this);

        this.mainButtonTwo = (LinearLayout)super.findViewById(R.id.mainButtonTwo);
        this.mainButtonTwo.setOnClickListener(this);

        this.mainButtonThree = (LinearLayout)super.findViewById(R.id.mainButtonThree);
        this.mainButtonThree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        myToast = Toast.makeText(getApplicationContext(),
                "点击了"+v.getId(), Toast.LENGTH_LONG);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();
        switch (v.getId()) {
            case R.id.mainButtonOne:

                break;
            case R.id.mainButtonTwo:

                break;
            case R.id.mainButtonThree:

                break;
        }
    }
}
