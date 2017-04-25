package com.pactera.enterprisesecretary.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.pactera.enterprisesecretary.BuildConfig;
import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.adapter.ChatAdapter;
import com.pactera.enterprisesecretary.module.ChatMessage;
import com.pactera.enterprisesecretary.util.StaticProperty;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//聊天页面
public class ChatActivity extends MyBaseActivity implements View.OnClickListener {


    private Button chatMoreButton, chatSendButton = null;
    private ImageButton chatTypeButton = null;
    private EditText chatMessageEditText = null;
    private View chatVoiceButton = null;
    private RelativeLayout chatMoreView = null;
    private ScrollView chatMoreScrollView = null;
    private ImageButton[] moreButtons = null;

    private ListView chatListView = null;// 信息显示列表
    private List<ChatMessage> messageList = null;
    public ChatAdapter chatAdapter= null;

    private String photoName;// 随机生成的照片名


    //位移动画
    private Animation animationOut, animationIn;

    // 设计师版隐藏布局中的四个按钮
    private Button button_sendImage, button_phone, button_inviteOrder;// 客户版隐藏布局中的发送图片按钮,拨打电话,邀请查看订单按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.chatMoreView = (RelativeLayout) super.findViewById(R.id.chatMoreView);
        this.chatMoreButton = (Button) super.findViewById(R.id.chatMoreButton);
        this.chatMoreButton.setOnClickListener(this);
        this.chatSendButton = (Button) super.findViewById(R.id.chatSendButton);
        this.chatSendButton.setTag("0");//默认状态
        this.chatSendButton.setText("拍照");
        this.chatSendButton.setOnClickListener(this);
        this.chatMessageEditText = (EditText) super.findViewById(R.id.chatMessageEditText);
        this.chatMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                //text  输入框中改变前的字符串信息
                //start 输入框中改变前的字符串的起始位置
                //count 输入框中改变前后的字符串改变数量一般为0
                //after 输入框中改变后的字符串与起始位置的偏移量
//                System.out.println("输入前字符串 [ " + text.toString() + " ]起始光标 [ " + start + " ]结束偏移量  [" + after + " ]");
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                //text  输入框中改变后的字符串信息
                //start 输入框中改变后的字符串的起始位置
                //before 输入框中改变前的字符串的位置 默认为0
                //count 输入框中改变后的一共输入字符串的数量
//                System.out.println("输入后字符串 [ " + text.toString() + " ] 起始光标 [ " + start + " ] 输入数量 [ " + count+" ]");
            }

            @Override
            public void afterTextChanged(Editable edit) {
                //edit  输入结束呈现在输入框中的信息
//                System.out.println("输入结束后的内容为 [" + edit.toString()+" ] 即将显示在屏幕上");
                if (edit.length() > 0&&ChatActivity.this.chatSendButton.getTag().equals("0")) {
                    ChatActivity.this.chatSendButton.setTag("1");//待发送状态
                    ChatActivity.this.changeSendButton();
                } else if(edit.length() == 0){
                    ChatActivity.this.chatSendButton.setTag("0");//默认状态
                    ChatActivity.this.changeSendButton();
                }

            }
        });


        this.chatListView = (ListView)super.findViewById(R.id.chatListView);
        this.messageList = new ArrayList<ChatMessage>();
        this.chatAdapter = new ChatAdapter(this, messageList);
        this.chatListView.setAdapter(this.chatAdapter);

        // 点击更多显示动画
        this.animationIn = AnimationUtils.loadAnimation(
                ChatActivity.this, R.anim.activity_movein);
        this.animationOut = AnimationUtils.loadAnimation(
                ChatActivity.this, R.anim.activity_exitout);

    }

    //改变发送
    public void changeSendButton() {
        if (this.chatSendButton.getTag().equals("0")) {
            this.chatSendButton.setText("拍照");
        } else {
            this.chatSendButton.setText("发送");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chatMoreButton:// 点击更多按钮
                if (!this.chatMoreView.isShown()) {
                    this.chatMoreView.setVisibility(View.VISIBLE);
                    this.chatMoreView.startAnimation(animationIn);
                } else {
                    this.chatMoreView.setVisibility(View.GONE);//如果在动画结束之后消失，则速度过慢，造成体验感差！
                    this.chatMoreView.startAnimation(animationOut);
                }
                break;
            case R.id.chatSendButton:// 点击发送按钮
                if(view.getTag().equals("1")){//发送文本
                    String message = this.chatMessageEditText.getText().toString();
                    if (message == null || message.equals("")) {
                        Toast.makeText(ChatActivity.this, "内容不能为空",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "msg" + message);
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setMessageFlag(true);
                        Date sentDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        chatMessage.setSentTime(sdf.format(sentDate));
                        chatMessage.setType(StaticProperty.CHATINFO);// 存入信息类型
                        chatMessage.setMessage(message);
                        messageList.add(chatMessage);
                        //istview随item的增加而向上滚动
                        this.chatListView
                                .setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                        chatAdapter.notifyDataSetChanged();
                        Log.i(TAG, "发送完成" + message);
                        this.chatMessageEditText.setText("");
                    }
                }else{//发送图片
                    String[] choices = new String[2];
                    choices[0] = "拍照";
                    choices[1] = "从相册中获取";
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            ChatActivity.this,
                            android.R.layout.simple_expandable_list_item_1, choices);
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            ChatActivity.this);
                    builder.setTitle("请选择图片方式");
                    builder.setSingleChoiceItems(adapter, -1,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    switch (which) {
                                        // 点击拍照按钮
                                        case 0:
                                            // 先验证手机是否有sdcard
                                            String status = Environment
                                                    .getExternalStorageState();
                                            if (status.equals(Environment.MEDIA_MOUNTED)) {
                                                try {
                                                    ChatActivity.this.photoName = "coolatin_"
                                                            + System.currentTimeMillis()
                                                            + ".jpg";
//                                                    String fileName = StaticProperty.FILEPATH
//                                                            + photoName;
//                                                    File file = new File(fileName);
//                                                    if (!file.getParentFile().exists()) {
//                                                        file.getParentFile().mkdirs();
//                                                    }
//
//                                                    if (!file.getParentFile().exists())file.getParentFile().mkdirs();


                                                    File imagePath = new File(getFilesDir(), "Videos");
                                                    File newFile = new File(imagePath, photoName);


                                                    Uri imageUri = FileProvider.getUriForFile(ChatActivity.this,"com.wow.fileprovider", newFile);//通过FileProvider创建一个content类型的Uri
                                                    Intent intent = new Intent();
                                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
                                                    startActivityForResult(intent,1006);




                                                } catch (ActivityNotFoundException e) {
                                                    // TODO Auto-generated catch block
                                                    Toast.makeText(
                                                            ChatActivity.this,
                                                            "没有找到储存目录", Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            } else {
                                                Toast.makeText(
                                                        ChatActivity.this,
                                                        "没有储存卡", Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                        // 点击从相册中选择照片
                                        case 1:
                                            Intent intent2 = new Intent(
                                                    Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(intent2, 11);
                                            // Intent intent2=new
                                            // Intent(Intent.ACTION_GET_CONTENT);
                                            // intent2.addCategory(Intent.CATEGORY_OPENABLE);
                                            // intent2.setType("image/*");
                                            // intent2.putExtra("crop", "true");
                                            // intent2.putExtra("aspectX", 1);
                                            // intent2.putExtra("aspectY", 1);
                                            // intent2.putExtra("outputX", 80);
                                            // intent2.putExtra("outputY", 80);
                                            // intent2.putExtra("return-data", true);
                                            // startActivityForResult(intent2, 11);
                                            break;
                                    }
                                }
                            });
                    builder.create().show();

                }
                break;
            default:
                break;
        }
    }
}
