package com.pactera.enterprisesecretary.module;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.Map;

/**
 * 聊天信息包装类
 * 
 * @author Savvy 2014.11.28
 */
public class ChatMessage implements Serializable {

	private static final long serialVersionUID = -4012977018484775771L;

	private String name;// 身份名称，即用户名
	private int messageFlag;// 信息类型,1为发送，0为接收
	private int contentType;// 内容类型
	private String textContent;// 信息
	private String voice;// 声音
	private String imageString;// 图片内容字符串
	private Bitmap imageBitmap;// 图片内容bitmap
	private String imagePath;// 图片地址（网址或本地地址）
	private Uri imageUri;// 图片资源标志
	private int imageType;// 图片类型：0为拍照获取，1为相册获取，2为网络获取
	private String systemInfo;// 系统信息
	private String sentTime;// 信息发送时间
	private String voicePath;// 声音文件路径
	private int voiceTime;// 音频时间
	private String loginUserHeadImage;// 登录的用户的头像
	private String chatUserHeadImage;// 对方的头像

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}

	public String getImageString() {
		return imageString;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

	public Uri getImageUri() {
		return imageUri;
	}

	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public int getMessageFlag() {
		return messageFlag;
	}

	public void setMessageFlag(int messageFlag) {
		this.messageFlag = messageFlag;
	}

	public String getSentTime() {
		return sentTime;
	}

	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public int getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(int voiceTime) {
		this.voiceTime = voiceTime;
	}

	public String getLoginUserHeadImage() {
		return loginUserHeadImage;
	}

	public void setLoginUserHeadImage(String loginUserHeadImage) {
		this.loginUserHeadImage = loginUserHeadImage;
	}

	public String getChatUserHeadImage() {
		return chatUserHeadImage;
	}

	public void setChatUserHeadImage(String chatUserHeadImage) {
		this.chatUserHeadImage = chatUserHeadImage;
	}

	public String getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(String systemInfo) {
		this.systemInfo = systemInfo;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
