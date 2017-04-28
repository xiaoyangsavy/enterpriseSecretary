package com.pactera.enterprisesecretary.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;

/**
 * 全局静态属性，保存必要信息
 * Created by xiaoyang on 2017/4/18.
 */

public class StaticProperty {

    public static final String SAVEINFO = "saveInfo";//保存信息标志
    public static final String SCREENWIDTH = "screenWidth"; // 屏幕宽度
    public static final String SCREENHEIGHT = "screenHeight"; //屏幕高度
    public static final String ISFIRSTLOGIN = "isFirstLogin"; //首次登陆
    public static final String BRAND = "phoneBrand";//手机品牌

    public static final String CHATINFO = "chat"; // 聊天内容为文本信息
    public static final String CHATIMAGE = "image"; // 聊天内容为图片信息
    public static final String CHATVOICE = "voice"; // 聊天内容为声音信息
    // 文件存储文件夹路径
    public static final String FILEPATH = "Resource";//资源文件路径




    private static final int REQUEST_CODE = 1;//申请码
    private static String[] PERMISSIONS_STORAGE = {//运行时权限
            Manifest.permission.READ_EXTERNAL_STORAGE,//读存储空间
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//取存储空间
            Manifest.permission.RECORD_AUDIO//记录声音
    };



    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_CODE
            );
        }
    }

}
