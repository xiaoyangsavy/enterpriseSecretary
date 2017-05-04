package com.pactera.enterprisesecretary.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
				+ "messageTime 		VARCHAR(50)   		NOT NULL 	,"		//发送或接收时间
				+ "id				INTEGER				PRIMARY KEY "		//主键，自增
				+ ")";

		db.execSQL(sql);
	}

	//更新数据库表
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + StaticProperty.TABLECHATMESSAGE;
		db.execSQL(sql);
		// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!database");
		this.onCreate(db);
		db.close();//关闭数据库
	}

}
