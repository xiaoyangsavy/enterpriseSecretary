package com.pactera.enterprisesecretary.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.adapter.ViewPagerAdapter;
import com.pactera.enterprisesecretary.fragments.MainFragment;
import com.pactera.enterprisesecretary.fragments.MessageFragment;
import com.pactera.enterprisesecretary.fragments.UserFragment;
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


    private LinearLayout  bottomButtonOne = null;
    private ImageView bottomImageViewOne = null;
    private TextView bottomTextViewOne = null;

    private LinearLayout  bottomButtonTwo = null;
    private ImageView bottomImageViewTwo = null;
    private TextView bottomTextViewTwo = null;

    private LinearLayout  bottomButtonThree = null;
    private ImageView bottomImageViewThree = null;
    private TextView bottomTextViewThree = null;
    private Toast myToast;


    private ViewPager mainViewpager = null;
    private ViewPagerAdapter mainAdapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    private ArrayList itemList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //测试数据
        this.itemList = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < 5; i++) {
            Map<String, Object> myMap = new HashMap<String, Object>();
            myMap.put("name", "加载中");
            myMap.put("description", "加载中......");
            myMap.put("image",R.drawable.item_icon_test);
            this.itemList.add(myMap);
        }


        //获取屏幕尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        //保存全局信息
        share = MainActivity.this.getSharedPreferences(
                StaticProperty.SAVEINFO, Activity.MODE_PRIVATE);
        sedit = share.edit();
        sedit.putInt(StaticProperty.SCREENWIDTH, width);//保存屏幕宽度
        sedit.putInt(StaticProperty.SCREENHEIGHT, height);//保存屏幕高度
        sedit.commit();//提交保存信息


        //创建切换视图
        this.mainViewpager = (ViewPager) super.findViewById(R.id.mainViewpager);
        this.mainAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        this.mainViewpager.setAdapter(this.mainAdapter);

        //创建切换页面
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("itemList", this.itemList);
        MessageFragment messageFragment = MessageFragment.newInstance(bundle);
        fragments.add(messageFragment);
        MainFragment mainFragment = MainFragment.newInstance(bundle);
        fragments.add(mainFragment);
       UserFragment userFragment = UserFragment.newInstance(bundle);
        fragments.add(userFragment);
        this.mainAdapter.notifyDataSetChanged();

        //默认显示第二个页面
        MainActivity.this.mainViewpager
                .setCurrentItem(1);

        //底部按钮区域
        this.bottomButtonOne = (LinearLayout) super.findViewById(R.id.bottomButtonOne);
        this.bottomButtonOne.setOnClickListener(this);
        this.bottomImageViewOne = (ImageView) super.findViewById(R.id.bottomImageViewOne);

        this.bottomButtonTwo = (LinearLayout) super.findViewById(R.id.bottomButtonTwo);
        this.bottomButtonTwo.setOnClickListener(this);
        this.bottomImageViewTwo = (ImageView) super.findViewById(R.id.bottomImageViewTwo);

        this.bottomButtonThree = (LinearLayout) super.findViewById(R.id.bottomButtonThree);
        this.bottomButtonThree.setOnClickListener(this);
        this.bottomImageViewThree = (ImageView) super.findViewById(R.id.bottomImageViewThree);
    }

    @Override
    public void onClick(View v) {
//        myToast = Toast.makeText(getApplicationContext(),
//                "点击了" + v.getId(), Toast.LENGTH_LONG);
//        myToast.setGravity(Gravity.CENTER, 0, 0);
//        myToast.show();
        this.bottomImageViewOne.setImageResource(R.drawable.common_button_message_unselect);
        this.bottomImageViewTwo.setImageResource(R.drawable.common_button_main_unselect);
        this.bottomImageViewThree.setImageResource(R.drawable.common_button_user_unselect);

        switch (v.getId()) {
            case R.id.bottomButtonOne:
                this.bottomImageViewOne.setImageResource(R.drawable.common_button_message_select);
                MainActivity.this.mainViewpager
                        .setCurrentItem(0);
                break;
            case R.id.bottomButtonTwo:
                this.bottomImageViewTwo.setImageResource(R.drawable.common_button_main_select);
                MainActivity.this.mainViewpager
                        .setCurrentItem(1);
                break;
            case R.id.bottomButtonThree:
                this.bottomImageViewThree.setImageResource(R.drawable.common_button_user_select);
                MainActivity.this.mainViewpager
                        .setCurrentItem(2);
                break;
        }
    }
}