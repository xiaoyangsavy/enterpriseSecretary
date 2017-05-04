package com.pactera.enterprisesecretary.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.util.MyDatabaseHelper;
import com.pactera.enterprisesecretary.util.MyDatabaseUtil;
import com.pactera.enterprisesecretary.util.StaticProperty;

public class IndexActivity extends AppCompatActivity {

    private SharedPreferences share = null;
    private SharedPreferences.Editor sedit = null;

    //数据库
    private SQLiteOpenHelper sqLiteOpenHelper = null;
    private MyDatabaseUtil myDatabaseUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        share = IndexActivity.this.getSharedPreferences(
                StaticProperty.SAVEINFO, Activity.MODE_PRIVATE);
        // 验证是否为第一次登录
        boolean flag = share.getBoolean(StaticProperty.ISFIRSTLOGIN, true);

//        if(flag) {
            //修改为非第一次登陆
            sedit = share.edit();
            sedit.putBoolean(StaticProperty.ISFIRSTLOGIN, false);
            sedit.commit();

            // 初始化数据库
            this.sqLiteOpenHelper = new MyDatabaseHelper(this);
            this.sqLiteOpenHelper.onUpgrade(this.sqLiteOpenHelper.getWritableDatabase(), 1, 2);// 格式化数据库
//        }
    }
}
