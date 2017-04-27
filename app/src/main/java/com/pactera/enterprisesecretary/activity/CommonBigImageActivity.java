package com.pactera.enterprisesecretary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.custom.DragImageView;
import com.pactera.enterprisesecretary.util.CommonUtil;
import com.pactera.enterprisesecretary.util.StaticProperty;

import java.io.IOException;
import java.io.InputStream;

public class CommonBigImageActivity extends AppCompatActivity {

    CommonUtil obtainInterfaceUtil = new CommonUtil();
    SharedPreferences share;
    private int window_width, window_height;// 控件宽度
    private DragImageView dragImageView;// 自定义控件
    private int state_height;// 状态栏的高度
    private ViewTreeObserver viewTreeObserver;

    private RelativeLayout bigBackRl;
    private String imageUrl = null;// 图片网址
    private Bitmap imageBitmap = null;// 图片文件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_big_image);

        this.getSupportActionBar().hide();//隐藏顶部导航栏

        share = getSharedPreferences(StaticProperty.SAVEINFO,
                Activity.MODE_PRIVATE);
        Log.d("savy", "显示大图活动");
        this.dragImageView = (DragImageView) super.findViewById(R.id.bigImg);
        // 设置图片
        dragImageView.setmActivity(this);// 注入Activity.
        // 获取传入参数
        Intent intent = super.getIntent();
        Bundle bundle = intent.getExtras();
        int type = bundle.getInt("type");
        int degree = 0;
        switch (type){//图片类型
            case 0://拍照
                imageBitmap = obtainInterfaceUtil.getBitmapByPath(bundle.getString("imagePath"), 1000, 1000);
                 degree = obtainInterfaceUtil.getBitmapDegree(intent.getExtras()
                        .getString("imagePath"));
                imageBitmap = obtainInterfaceUtil.rotateBitmapByDegree(imageBitmap,
                        degree);
                dragImageView.setImageBitmap(imageBitmap);
                break;
            case 1://相册
                Uri uri = bundle.getParcelable("imageUri");
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dragImageView.setImageBitmap(imageBitmap);
                break;
            case 2://网络
                break;
        }


        /** 测量状态栏高度 **/
        viewTreeObserver = dragImageView.getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (state_height == 0) {
                            // 获取状况栏高度
                            Rect frame = new Rect();
                            getWindow().getDecorView()
                                    .getWindowVisibleDisplayFrame(frame);
                            state_height = frame.top;
                            dragImageView.setScreen_H(window_height
                                    - state_height);
                            dragImageView.setScreen_W(window_width);
                        }

                    }
                });

        this.bigBackRl = (RelativeLayout) super.findViewById(R.id.bigBackRl);
        this.bigBackRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                CommonBigImageActivity.this.finish();
            }
        });
    }

    /**
     * 读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
