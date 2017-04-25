package com.pactera.enterprisesecretary.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.custom.CircleImageview;
import com.pactera.enterprisesecretary.module.ChatMessage;
import com.pactera.enterprisesecretary.util.StaticProperty;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 聊天适配器 2014.12.01
 * 
 * @author Savvy
 */
public class ChatAdapter extends BaseAdapter {

	private String TAG = "chat";

	private Activity context;
	private List<ChatMessage> chatMessageList;
	private boolean voicePlay = true;// 音频是否可以播放
	MediaPlayer mPlayer = null;
	private ImageView chat_message_img_right_send;
	private ImageView chat_message_img_left, chat_message_img_right;

	public ChatAdapter(Activity context, List<ChatMessage> chatMessageList) {
		super();
		this.context = context;
		this.chatMessageList = chatMessageList;
		mPlayer = new MediaPlayer();
	}

	@Override
	public int getCount() {
		return chatMessageList.size();
	}

	@Override
	public Object getItem(int position) {
		return chatMessageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		final ChatMessage chatMessage = chatMessageList.get(position);
		Log.e(TAG, position + "" + chatMessage.isMessageFlag() + "type:"
				+ chatMessage.getType());


		if (chatMessage.isMessageFlag()) {// 发送方信息
			convertView = ChatAdapter.this.context.getLayoutInflater().inflate(
					R.layout.chat_msg_item_right, null);
			chat_message_img_right = (ImageView) convertView
					.findViewById(R.id.chat_message_img);
			chat_message_img_right_send = (ImageView) convertView
					.findViewById(R.id.chat_message_img_right_send);
		} else {// 接收方信息
			convertView = ChatAdapter.this.context.getLayoutInflater().inflate(
					R.layout.chat_msg_item_left, null);
			chat_message_img_left = (ImageView) convertView
					.findViewById(R.id.chat_message_img);
		}

		LinearLayout chat_message_layout = (LinearLayout) convertView
				.findViewById(R.id.chat_message_layout);
		TextView voicetime = (TextView) convertView
				.findViewById(R.id.voicetime);
		TextView message_name = (TextView) convertView
				.findViewById(R.id.chat_message_name);
		TextView message_time = (TextView) convertView
				.findViewById(R.id.chat_message_time);
		TextView message_content = (TextView) convertView
				.findViewById(R.id.chat_message_content);
		RelativeLayout chatContentLayout = (RelativeLayout) convertView
				.findViewById(R.id.chatContentLayout);
		CircleImageview chat_message_icon = (CircleImageview) convertView
				.findViewById(R.id.chat_message_icon);

		message_name.setText(chatMessage.getName());
		message_time.setText(chatMessage.getSentTime());

		// 判断消息类型，分别进行展示
		Log.e("savvy", "类型:" + chatMessage.getType());
		if (chatMessage.getType() != null) {
			if (chatMessage.isMessageFlag()) {
				// 右边，发送方
				if (chatMessage.getType().equals(StaticProperty.CHATINFO)) {// 文本
					message_content.setVisibility(View.VISIBLE);
					chat_message_img_right.setVisibility(View.GONE);
					chat_message_img_right_send.setVisibility(View.GONE);
					message_content.setText(chatMessage.getMessage());
//					chat_message_icon
//							.setImageResource(R.drawable.loading_fuzzy_80x80);
					
				} else if (chatMessage.getType().equals(
						StaticProperty.CHATIMAGE)) {// 图片
					message_content.setVisibility(View.GONE);
					// Log.e(TAG,"图片内容"+
					// "!!!"+chatMessage.getImage().toString().equals(StaticProperty.CHATIP)
					// + "!!!!!!"
					// + chatMessage.getImage().toString().equals("http:")+
					// "!!!!!!"
					// + chatMessage.getImage().split("http://").length);
					Pattern pattern = Pattern.compile("(http://){1}");
					// 空格结束
					Matcher matcher = pattern.matcher(chatMessage.getImage());
					// if (matcher.find()) {
					// Log.e(TAG,"正则表达式："+matcher.group(0));
					// }
					if (matcher.find()) {// 网络图片,从历史记录中获取的图片为网络图片
						chat_message_img_right.setVisibility(View.VISIBLE);
						chat_message_img_right_send.setVisibility(View.GONE);
						// 图片网址转换
						Log.e("savvy", "发送方网络图片:" + chatMessage.getImage());
						message_content.setVisibility(View.GONE);
						chat_message_img_right.setVisibility(View.VISIBLE);
						//测试图片
						 chat_message_img_right.setImageResource(R.drawable.item_icon_test);
						//点击小图显示大图
//						chat_message_img_right
//								.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										Log.e("savvy", "chat_message_img_right");
//										Intent intent = new Intent();
//										Bundle bundle = new Bundle();
//										bundle.putString("imageUrl",
//												chatMessage.getImage());
//										System.out.println("--------------------------------------" + chatMessage.getImage());
//										intent.putExtras(bundle);
//										intent.setClass(context,
//												CommonBigImageActivity.class);
//										context.startActivity(intent);
//									}
//								});
					} else {// IO流文件，当前发送的图片为IO流图片
						chat_message_img_right_send.setVisibility(View.VISIBLE);
						chat_message_img_right.setVisibility(View.GONE);
						// 图片网址转换
						Log.e("savvy", "发送方IO流图片:" + chatMessage.getImage());
						//测试图片
						chat_message_img_right.setImageResource(R.drawable.item_icon_test);

						//根据编码格式，将字符串转换成图片（如base64）
//						Bitmap imageBitmap = contextUtil
//								.stringtoBitmap(chatMessage.getImage());
//						chat_message_img_right_send.setImageBitmap(imageBitmap);
//						chat_message_img_right_send
//								.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										Log.e("savvy", "chat_message_img_right");
//										Intent intent = new Intent();// 不能直接传递大于40k的图片和大于1M的序列化字符串
//										Bundle bundle = new Bundle();
//										bundle.putString("imagePath",
//												chatMessage.getImagePath());
//										intent.putExtras(bundle);
//										intent.setClass(context,
//												CommonBigImageActivity.class);
//										context.startActivity(intent);
//									}
//								});
					}
				} else if (chatMessage.getType().equals(
						StaticProperty.CHATVOICE)) {// 声音
					// 显示时长
					message_content.setVisibility(View.GONE);
					chat_message_img_right_send.setVisibility(View.VISIBLE);
					voicetime.setVisibility(View.VISIBLE);
					voicetime.setText(chatMessage.getVoiceTime() + "'))  ");
					// // 声音点击事件
					message_content.setVisibility(View.GONE);
					chat_message_img_right.setVisibility(View.VISIBLE);
					// 默认背景
					if (chatMessage.isMessageFlag()) {
						chat_message_img_right_send
								.setBackgroundResource(R.drawable.appkefu_chatto_voice_playing);
					} else {
						chat_message_img_right_send
								.setBackgroundResource(R.drawable.appkefu_chatfrom_voice_playing);
					}
					// // 声音点击事件
					chat_message_layout
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Log.e("savvy",
											"播放开始" + String.valueOf(voicePlay));
									try {
										if (chatMessage.getVoicePath() != null
												&& !chatMessage.getVoicePath()
														.equals("")) {
											if (voicePlay) {// 成功，进行播放
												// 播放背景
												if (chatMessage.isMessageFlag()) {
													chat_message_img_right_send
															.setBackgroundResource(R.drawable.framebyframeright);
												} else {
													chat_message_img_right_send
															.setBackgroundResource(R.drawable.framebyframeleft);
												}
												final AnimationDrawable animationDrawable = (AnimationDrawable) chat_message_img_right_send
														.getBackground();
												voicePlay = false;
												mPlayer.reset();
												mPlayer.setDataSource(chatMessage
														.getVoicePath());// 获取绝对路径来播放音频
												mPlayer.prepare();
												mPlayer.start();
												animationDrawable.start();
												mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

													@Override
													public void onCompletion(
															MediaPlayer mPlayer) {
														voicePlay = true;
														Log.e("savvy",
																"播放结束："
																		+ String.valueOf(voicePlay));
														// if (mPlayer != null)
														// {
														// mPlayer.stop();
														// mPlayer.release();
														// mPlayer = null;
														// }
														animationDrawable
																.stop();
														// 播放结束，改为默认背景
														if (chatMessage
																.isMessageFlag()) {
															chat_message_img_right_send
																	.setBackgroundResource(R.drawable.appkefu_chatto_voice_playing);
														} else {
															chat_message_img_right_send
																	.setBackgroundResource(R.drawable.appkefu_chatfrom_voice_playing);
														}
													}
												});
											}
										} else {
											Toast.makeText(context,
													"音频不存在，播放失败！",
													Toast.LENGTH_LONG).show();
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

				}
			} else {// 左侧，接收方

				if (chatMessage.getType().equals(StaticProperty.CHATINFO)) {// 文本
					message_content.setVisibility(View.VISIBLE);
					chat_message_img_left.setVisibility(View.GONE);
					chatContentLayout.setVisibility(View.VISIBLE);
					message_content.setText(chatMessage.getMessage());
					chat_message_icon.setScaleType(ImageView.ScaleType.FIT_XY);
					System.out.println("chatUserHeadImage------------------------>" + chatMessage.getChatUserHeadImage());
				} else if (chatMessage.getType().equals(
						StaticProperty.CHATIMAGE)) {// 图片
					message_content.setVisibility(View.GONE);

					chat_message_icon.setScaleType(ImageView.ScaleType.FIT_XY);
					chat_message_img_left.setVisibility(View.VISIBLE);
					//大图显示
//					chat_message_img_left
//							.setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									Log.e("savvy", "chat_message_img_left");
//									Intent intent = new Intent();
//									Bundle bundle = new Bundle();
//									bundle.putString("imageUrl",
//											chatMessage.getImage());
//									intent.putExtras(bundle);
//									intent.setClass(context,
//											CommonBigImageActivity.class);
//									context.startActivity(intent);
//								}
//							});

				} else if (chatMessage.getType().equals(
						StaticProperty.CHATVOICE)) {// 声音
					// 显示时长

					message_content.setVisibility(View.GONE);
					chat_message_img_left.setVisibility(View.VISIBLE);
					voicetime.setVisibility(View.VISIBLE);

					voicetime.setText(chatMessage.getVoiceTime() + "'))  ");
					// // 声音点击事件
					message_content.setVisibility(View.GONE);
					chat_message_img_left.setVisibility(View.VISIBLE);
					// 默认背景
					if (chatMessage.isMessageFlag()) {
						chat_message_img_left
								.setBackgroundResource(R.drawable.appkefu_chatto_voice_playing);
					} else {
						chat_message_img_left
								.setBackgroundResource(R.drawable.appkefu_chatfrom_voice_playing);
					}
					// // 声音点击事件
					chat_message_layout
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Log.e("savvy",
											"播放开始" + String.valueOf(voicePlay));
									try {
										if (chatMessage.getVoicePath() != null
												&& !chatMessage.getVoicePath()
														.equals("")) {
											if (voicePlay) {// 成功，进行播放
												// 播放背景
												if (chatMessage.isMessageFlag()) {
													chat_message_img_left
															.setBackgroundResource(R.drawable.framebyframeright);
												} else {
													chat_message_img_left
															.setBackgroundResource(R.drawable.framebyframeleft);
												}
												final AnimationDrawable animationDrawable = (AnimationDrawable) chat_message_img_left
														.getBackground();
												voicePlay = false;
												mPlayer.reset();
												mPlayer.setDataSource(chatMessage
														.getVoicePath());// 获取绝对路径来播放音频
												mPlayer.prepare();
												mPlayer.start();
												animationDrawable.start();
												mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

													@Override
													public void onCompletion(
															MediaPlayer mPlayer) {
														voicePlay = true;
														Log.e("savvy",
																"播放结束："
																		+ String.valueOf(voicePlay));
														// if (mPlayer != null)
														// {
														// mPlayer.stop();
														// mPlayer.release();
														// mPlayer = null;
														// }
														animationDrawable
																.stop();
														// 播放结束，改为默认背景
														if (chatMessage
																.isMessageFlag()) {
															chat_message_img_left
																	.setBackgroundResource(R.drawable.appkefu_chatto_voice_playing);
														} else {
															chat_message_img_left
																	.setBackgroundResource(R.drawable.appkefu_chatfrom_voice_playing);
														}
													}
												});
											}
										} else {
											Toast.makeText(context,
													"音频不存在，播放失败！",
													Toast.LENGTH_LONG).show();
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

				}
			}
		} else {
			message_content.setText("未知信息！");
		}

		return convertView;
	}

	// 销毁播放器
	public void onPlayDestroy() {
		// System.out.println(" 销毁播放器"+mPlayer);
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
		// System.out.println(" 销毁播放器"+mPlayer);
	}
}