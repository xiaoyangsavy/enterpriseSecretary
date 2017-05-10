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
import com.pactera.enterprisesecretary.activity.ChatActivity;
import com.pactera.enterprisesecretary.activity.CommonBigImageActivity;
import com.pactera.enterprisesecretary.custom.CircleImageview;
import com.pactera.enterprisesecretary.module.ChatMessage;
import com.pactera.enterprisesecretary.util.CommonUtil;
import com.pactera.enterprisesecretary.util.StaticProperty;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 聊天适配器 2014.12.01
 *
 * @author Savvy
 */
public class ChatAdapter extends BaseAdapter {

    public CommonUtil commonUtil = new CommonUtil();//工具类

    private Activity context;
    private List<ChatMessage> chatMessageList;
    private boolean voicePlay = true;// 音频是否可以播放
    MediaPlayer mPlayer = null;

    private ImageView chatImage;
    private View chatAudio;


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
        Log.e("savy", position + ":" + chatMessage.getMessageFlag() + "! type:"
                + chatMessage.getContentType());
        int messageFlag = 1;//发送信息
        messageFlag = chatMessage.getMessageFlag();
//        messageFlag = 0;  //接收信息
        if (messageFlag == 1) {// 发送方信息
            convertView = ChatAdapter.this.context.getLayoutInflater().inflate(
                    R.layout.chat_msg_item_right, null);

        } else {// 接收方信息
            convertView = ChatAdapter.this.context.getLayoutInflater().inflate(
                    R.layout.chat_msg_item_left, null);
        }

        this.chatImage = (ImageView) convertView
                .findViewById(R.id.chatImage);
        this.chatAudio = (View) convertView
                .findViewById(R.id.chatAudio);
        LinearLayout chatMessageLayout = (LinearLayout) convertView
                .findViewById(R.id.chatMessageLayout);
        TextView chatAudioTime = (TextView) convertView
                .findViewById(R.id.chatAudioTime);
        TextView chatMessageName = (TextView) convertView
                .findViewById(R.id.chatMessageName);
        TextView chatMessageTime = (TextView) convertView
                .findViewById(R.id.chatMessageTime);
        TextView chatText = (TextView) convertView
                .findViewById(R.id.chatText);
        RelativeLayout chatContentLayout = (RelativeLayout) convertView
                .findViewById(R.id.chatContentLayout);
        CircleImageview chatMessageIcon = (CircleImageview) convertView
                .findViewById(R.id.chatMessageIcon);

        chatMessageName.setText(chatMessage.getName());
        chatMessageTime.setText(chatMessage.getSentTime());


        //判断是否显示时间
        if (position > 0) {//非首条记录，需要判断，两条记录间隔5分钟
            String currentTimeString = chatMessage.getSentTime();
            String preTimeString = chatMessageList.get(position - 1).getSentTime();

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

            long currentTimeLong = 0l;
            long preTimeLong = 0l;
            try {
                //上一条信息的时间
                Date preDate = sdf.parse(preTimeString);
                preTimeLong = preDate.getTime();
                //本条信息的时间
                Date currentDate = sdf.parse(currentTimeString);
                currentTimeLong = currentDate.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (currentTimeLong - preTimeLong > (5 * 60 * 1000)) {
                chatMessageTime.setVisibility(View.VISIBLE);
            } else {
                chatMessageTime.setVisibility(View.GONE);
            }

        } else {//首条必显示
            chatMessageTime.setVisibility(View.VISIBLE);
        }

        // 判断消息类型，分别进行展示
        Log.e("savvy", "类型:" + chatMessage.getContentType());
        if (messageFlag == 1) {
            // 右边，发送方
            if (chatMessage.getContentType() == StaticProperty.CHATINFO) {// 文本
                chatText.setVisibility(View.VISIBLE);
                chatText.setText(chatMessage.getTextContent());

            } else if (chatMessage.getContentType() ==
                    StaticProperty.CHATIMAGE) {// 图片

                Pattern pattern = Pattern.compile("(http://){1}");
                // 空格结束
                Matcher matcher = null;
                if (chatMessage.getImagePath() != null && !"".equals(chatMessage.getImagePath())) {
                    matcher = pattern.matcher(chatMessage.getImagePath());
                }
                if (matcher.find()) {// 网络图片,从历史记录中获取的图片为网络图片
                    chatImage.setVisibility(View.VISIBLE);
                    // 图片网址转换
                    Log.e("savy", "发送方网络图片:" + chatMessage.getImagePath());
                    //点击小图显示大图
//						chatImageRightv
//								.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										Log.e("savvy", "chatImageRight");
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

                    Log.e("savy", "发送方IO流图片:" + chatMessage.getImageUri());

                    chatImage.setVisibility(View.VISIBLE);

                    Bitmap imageBitmap = null;
                    imageBitmap = chatMessage.getImageBitmap();
//						imageBitmap = commonUtil.getBitmapByPath(chatMessage.getImagePath(),200,200);


//						try {
//						 imageBitmap = commonUtil.getBitmapByPathByUri(context,chatMessage.getImageUri(),1000,1000);//获取图片并压缩
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
                    Log.e("savy", "图片内容:" + imageBitmap);
                    chatImage.setImageBitmap(imageBitmap);

                    chatImage
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e("savvy", "chat_message_img_right");
                                    Intent intent = new Intent();// 不能直接传递大于40k的图片和大于1M的序列化字符串
                                    Bundle bundle = new Bundle();
                                    bundle.putString("imagePath",
                                            chatMessage.getImagePath());
                                    bundle.putInt("type", chatMessage.getImageType());
                                    if (chatMessage.getImageType() == 1) {
                                        bundle.putParcelable("imageUri", chatMessage.getImageUri());
                                    }
//                                        bundle.putParcelable("imageBitmap",
//                                                chatMessage.getImageBitmap());//直接传递bitmap有大小的限制
                                    intent.putExtras(bundle);
                                    intent.setClass(context,
                                            CommonBigImageActivity.class);
                                    context.startActivity(intent);
                                }
                            });

                }
            } else if (chatMessage.getContentType() ==
                    StaticProperty.CHATVOICE) {// 声音
                // 显示时长
                chatAudio.setVisibility(View.VISIBLE);
                chatAudioTime.setVisibility(View.VISIBLE);
                chatAudioTime.setText(chatMessage.getVoiceTime() + "'))  ");
                // // 声音点击事件
                // 默认背景
                chatAudio
                        .setBackgroundResource(R.drawable.appkefu_chatto_voice_playing);
                // // 声音点击事件
                chatMessageLayout
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final View myAudioView = view.findViewById(R.id.chatAudio);
                                Log.e("savy",
                                        "播放开始" + String.valueOf(voicePlay));
                                try {
                                    if (chatMessage.getVoicePath() != null
                                            && !chatMessage.getVoicePath()
                                            .equals("")) {
                                        if (voicePlay) {// 成功，进行播放
                                            // 播放背景
                                            myAudioView
                                                    .setBackgroundResource(R.drawable.framebyframeright);
                                            final AnimationDrawable animationDrawable = (AnimationDrawable) myAudioView
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
                                                    myAudioView
                                                            .setBackgroundResource(R.drawable.appkefu_chatto_voice_playing);
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

            if (chatMessage.getContentType() == StaticProperty.CHATINFO) {// 文本
                chatText.setVisibility(View.VISIBLE);
//					chat_message_img_left.setVisibility(View.GONE);
                chatContentLayout.setVisibility(View.VISIBLE);
                chatText.setText(chatMessage.getTextContent());
                chatMessageIcon.setScaleType(ImageView.ScaleType.FIT_XY);
                System.out.println("chatUserHeadImage------------------------>" + chatMessage.getChatUserHeadImage());
            } else if (chatMessage.getContentType() ==
                    StaticProperty.CHATIMAGE) {// 图片

                Pattern pattern = Pattern.compile("(http://){1}");
                // 空格结束
                Matcher matcher = null;
                if (chatMessage.getImagePath() != null && !"".equals(chatMessage.getImagePath())) {
                    matcher = pattern.matcher(chatMessage.getImagePath());
                }

                if (matcher.find()) {// 网络图片,从历史记录中获取的图片为网络图片
                    chatImage.setVisibility(View.VISIBLE);
                    // 图片网址转换
                    Log.e("savy", "发送方网络图片:" + chatMessage.getImagePath());

                } else {// IO流文件，当前发送的图片为IO流图片

                    Log.e("savy", "发送方IO流图片:" + chatMessage.getImageUri());

                    chatImage.setVisibility(View.VISIBLE);

                    Bitmap imageBitmap = null;
                    imageBitmap = chatMessage.getImageBitmap();
//						imageBitmap = commonUtil.getBitmapByPath(chatMessage.getImagePath(),200,200);


//						try {
//						 imageBitmap = commonUtil.getBitmapByPathByUri(context,chatMessage.getImageUri(),1000,1000);//获取图片并压缩
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
                    Log.e("savy", "图片内容:" + imageBitmap);
                    chatImage.setImageBitmap(imageBitmap);

                    chatImage
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e("savvy", "chat_message_img_right");
                                    Intent intent = new Intent();// 不能直接传递大于40k的图片和大于1M的序列化字符串
                                    Bundle bundle = new Bundle();
                                    bundle.putString("imagePath",
                                            chatMessage.getImagePath());
                                    bundle.putInt("type", chatMessage.getImageType());
                                    if (chatMessage.getImageType() == 1) {
                                        bundle.putParcelable("imageUri", chatMessage.getImageUri());
                                    }
//                                        bundle.putParcelable("imageBitmap",
//                                                chatMessage.getImageBitmap());//直接传递bitmap有大小的限制
                                    intent.putExtras(bundle);
                                    intent.setClass(context,
                                            CommonBigImageActivity.class);
                                    context.startActivity(intent);
                                }
                            });
                }
            } else if (chatMessage.getContentType() ==
                    StaticProperty.CHATVOICE) {// 声音
                // 显示时长
                chatAudio.setVisibility(View.VISIBLE);
                chatAudioTime.setVisibility(View.VISIBLE);
                chatAudioTime.setText(chatMessage.getVoiceTime() + "'))  ");
                // // 声音点击事件
                // 默认背景
                chatAudio
                        .setBackgroundResource(R.drawable.appkefu_chatfrom_voice_playing);
                // // 声音点击事件
                chatMessageLayout
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final View myAudioView = view.findViewById(R.id.chatAudio);
                                Log.e("savy",
                                        "播放开始" + String.valueOf(voicePlay));
                                try {
                                    if (chatMessage.getVoicePath() != null
                                            && !chatMessage.getVoicePath()
                                            .equals("")) {
                                        if (voicePlay) {// 成功，进行播放
                                            // 播放背景
                                            myAudioView
                                                    .setBackgroundResource(R.drawable.framebyframeleft);
                                            final AnimationDrawable animationDrawable = (AnimationDrawable) myAudioView
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
                                                    myAudioView
                                                            .setBackgroundResource(R.drawable.appkefu_chatfrom_voice_playing);
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