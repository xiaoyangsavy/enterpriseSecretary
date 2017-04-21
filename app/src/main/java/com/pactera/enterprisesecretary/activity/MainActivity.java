package com.pactera.enterprisesecretary.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
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

public class MainActivity extends MyBaseActivity implements View.OnClickListener {

    private int height = 0;
    private int width = 0;
    SharedPreferences share = null;
    SharedPreferences.Editor sedit = null;


    private View actionBarView = null;

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

        //隐藏标题栏按钮
        super.hiddenButton();

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
        this.mainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动结束
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //子页面切换时
            @Override
            public void onPageSelected(int position) {
                Log.d("savy", "setCurrentItem()被调用后执行，位置为: "+position);


                MainActivity.super.hiddenButton(); //隐藏导航栏按钮
                MainActivity.super.showLimit();//显示导航栏底部分割线

                //重置底部图片
                MainActivity.this.bottomImageViewOne.setImageResource(R.drawable.common_button_message_unselect);
                MainActivity.this.bottomImageViewTwo.setImageResource(R.drawable.common_button_main_unselect);
                MainActivity.this.bottomImageViewThree.setImageResource(R.drawable.common_button_user_unselect);

                switch(position)
                {
                    case 0:
                        MainActivity.super.setTitle("消息");
                        MainActivity.this.bottomImageViewOne.setImageResource(R.drawable.common_button_message_select);
                        break;
                    case 1:
                        MainActivity.super.setTitle("企秘");
                        MainActivity.super.hiddenLimit();
                        MainActivity.this.bottomImageViewTwo.setImageResource(R.drawable.common_button_main_select);
                        break;
                    case 2:
                        MainActivity.super.setTitle("我");
                        MainActivity.super.showGoButton();
                        MainActivity.this.bottomImageViewThree.setImageResource(R.drawable.common_button_user_select);
                        break;
                    default:
                        break;
                }

            }

            //在状态改变的时候调用,1:正在滑动，2：滑动完毕，3：什么都没有做
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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


        //默认显示第二个页面
        //需写在最后的位置，不然onPageSelected()方法里面的内容可能没有实例化完成就被调用
        MainActivity.this.mainViewpager
                .setCurrentItem(1);
    }

    @Override
    public void onClick(View v) {
//        myToast = Toast.makeText(getApplicationContext(),
//                "点击了" + v.getId(), Toast.LENGTH_LONG);
//        myToast.setGravity(Gravity.CENTER, 0, 0);
//        myToast.show();
//        this.bottomImageViewOne.setImageResource(R.drawable.common_button_message_unselect);
//        this.bottomImageViewTwo.setImageResource(R.drawable.common_button_main_unselect);
//        this.bottomImageViewThree.setImageResource(R.drawable.common_button_user_unselect);

        switch (v.getId()) {
            case R.id.bottomButtonOne:
//                this.bottomImageViewOne.setImageResource(R.drawable.common_button_message_select);
                MainActivity.this.mainViewpager
                        .setCurrentItem(0);
                break;
            case R.id.bottomButtonTwo:
//                this.bottomImageViewTwo.setImageResource(R.drawable.common_button_main_select);
                MainActivity.this.mainViewpager
                        .setCurrentItem(1);
                break;
            case R.id.bottomButtonThree:
//                this.bottomImageViewThree.setImageResource(R.drawable.common_button_user_select);
                MainActivity.this.mainViewpager
                        .setCurrentItem(2);
                break;
        }
    }


}
