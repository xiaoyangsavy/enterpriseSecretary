package com.pactera.enterprisesecretary.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pactera.enterprisesecretary.module.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库，操作类
 * @author Savvy
 * 
 */
public class MyDatabaseUtil  {

	private SQLiteDatabase db = null;

	public MyDatabaseUtil(SQLiteDatabase db) {
		this.db = db;
	}

	// 关闭数据库
	public void closeDB() {
		this.db.close();
	}



	public void insertAttitudeDesign(ChatMessage chatMessage) {
//		System.out.println(attitude + "attitude**********");
		String sql = "INSERT INTO "
				+ StaticProperty.TABLECHATMESSAGE
				+ "(messageFlag,contentType,name,textContent,voicePath,voiceTime,imagePath,messageTime "
				+ " ) "
				+ "VALUES (?,?,?,?,?,?,?,?)";
		Object args[] = new Object[] { chatMessage.getMessageFlag(), chatMessage.getContentType(),
				chatMessage.getName(), chatMessage.getTextContent(),
				chatMessage.getVoicePath(), chatMessage.getVoiceTime(),
				chatMessage.getImagePath(), chatMessage.getSentTime() };
		this.db.execSQL(sql, args);
	}

	public List<ChatMessage> findAllAttitudeDesign() {
		List<ChatMessage> all = new ArrayList<ChatMessage>();
		String sql = "messageFlag,contentType,name,textContent,voicePath,voiceTime,imagePath,messageTime"
				+ " FROM "
				+ StaticProperty.TABLECHATMESSAGE;
		String args[] = new String[] {};
		Cursor result = this.db.rawQuery(sql, args);
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			ChatMessage chatMessage = new ChatMessage();

			chatMessage.setMessageFlag(result.getInt(0));
			chatMessage.setContentType(result.getInt(1));
			chatMessage.setName(result.getString(2));
			chatMessage.setTextContent(result.getString(3));
			chatMessage.setVoicePath(result.getString(4));
			chatMessage.setVoiceTime(result.getInt(5));
			chatMessage.setImagePath(result.getString(6));
			chatMessage.setSentTime(result.getString(7));
			all.add(chatMessage);
		}
		return all;
	}


}
