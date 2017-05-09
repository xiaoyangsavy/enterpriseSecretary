package com.pactera.enterprisesecretary.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库，基类
 * 
 * @author Savvy
 * 
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

	public MyDatabaseHelper(Context context) {
		super(context, StaticProperty.DATABASENAME, null,
				StaticProperty.DATABASERVERSION);
	}

	/**
	 * 这个方法
	 * 1、在第一次打开数据库的时候才会走
	 * 2、在清除数据之后再次运行-->打开数据库，这个方法会走
	 * 3、没有清除数据，不会走这个方法
	 * 4、数据库升级的时候这个方法不会走
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE "
				+ StaticProperty.TABLECHATMESSAGE // 聊天信息表
				+ "("
				+ "messageFlag 		INTEGER 			NOT NULL 	,"		//发送接收标记，发送1；接收0
				+ "contentType 		INTEGER 			NOT NULL 	,"		//消息内容类型，文字1；声音2；图片3
				+ "name 			VARCHAR(50) 		   		 	,"		//姓名
				+ "textContent    	VARCHAR(50)  					,"		//信息
				+ "voicePath  		VARCHAR(50)   					,"		//语音地址
				+ "voiceTime  		INTEGER   						,"		//语音时间
				+ "imagePath 		VARCHAR(50)  					,"		//图片地址
				+ "imageBitmap 		BLOB   							,"		//图片数据
				+ "messageTime 		VARCHAR(50)   		NOT NULL 	,"		//发送或接收时间
				+ "id				INTEGER				PRIMARY KEY "		//主键，自增
				+ ")";

		db.execSQL(sql);
	}


	/**
	  * 1、第一次创建数据库的时候，这个方法不会走
	  * 2、清除数据后再次运行(相当于第一次创建)这个方法不会走
	  * 3、数据库已经存在，而且版本升高的时候，这个方法才会调用
	  */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 Log.e("savy","!!!!!!!!!!!!!!!!!!!!!!!!!升级数据库");
		String sql = "DROP TABLE IF EXISTS " + StaticProperty.TABLECHATMESSAGE;
		db.execSQL(sql);
		this.onCreate(db);
		db.close();//关闭数据库
	}

}
