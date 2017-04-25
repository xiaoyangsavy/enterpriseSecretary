package com.pactera.enterprisesecretary.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pactera.enterprisesecretary.R;

//activity公共基类
public class MyBaseActivity extends AppCompatActivity {

    protected String TAG = "SAVY";

    protected ActionBar actionBar = null;
    protected View myActionBarView = null;
    protected RelativeLayout backButton = null;
    protected ImageView backImageViiew = null;
    protected RelativeLayout goButton = null;
    protected ImageView goImageViiew = null;
    protected TextView titleTextView = null;
    protected View limitView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setCustomActionBar(); //自定义actionBar
    }

    //自定义actionBar，需要配置文件中主题的自定义样式的支持
    private void setCustomActionBar() {
        //创建导航栏视图
        ActionBar.LayoutParams ActionBarLayout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        this.myActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        this.backButton = (RelativeLayout) this.myActionBarView.findViewById(R.id.actionBarBackButton);
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBaseActivity.this.finish();//返回上一页
            }
        });
        this.backImageViiew = (ImageView) this.myActionBarView.findViewById(R.id.actionBarBackImageView);
        this.titleTextView = (TextView) this.myActionBarView.findViewById(R.id.actionBarTitleTextView);
        this.goButton = (RelativeLayout) this.myActionBarView.findViewById(R.id.actionBarGoButton);
        this.goImageViiew = (ImageView) this.myActionBarView.findViewById(R.id.actionBarGoImageView);
        this.limitView = (View) this.myActionBarView.findViewById(R.id.actionBarLimit);

        //创建导航栏并加载视图
        this.actionBar = getSupportActionBar();
        actionBar.setCustomView(this.myActionBarView, ActionBarLayout);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);//设置为自定义样式
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);



    }

    //隐藏actionBar
    public void hiddenActionBar() {
        this.actionBar.hide();
    }

    //显示actionBar
    public void showActionBar() {
        this.actionBar.show();
    }

    //隐藏返回按钮
    public void hiddenBackButton() {
        this.backButton.setVisibility(View.GONE);
    }

    //隐藏返回按钮
    public void showBackButton() {
        this.backButton.setVisibility(View.VISIBLE);
    }

    //隐藏功能按钮
    public void hiddenGoButton() {
        this.goButton.setVisibility(View.GONE);
    }

    //隐藏功能按钮
    public void showGoButton() {
        this.goButton.setVisibility(View.VISIBLE);
    }

    //隐藏全部按钮
    public void hiddenButton() {
        this.hiddenGoButton();
        this.hiddenBackButton();
    }

    //隐藏全部按钮
    public void showButton() {
        this.showGoButton();
        this.showBackButton();
    }

    //设置标题
    public void setTitle(String title) {
        this.titleTextView.setText(title);
    }

    //隐藏底部分割线
    public void hiddenLimit() {
        this.limitView.setVisibility(View.GONE);
    }

    //显示底部分割线
    public void showLimit() {
        this.limitView.setVisibility(View.VISIBLE);
    }
}
