package com.pactera.enterprisesecretary.activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.adapter.ChatAdapter;
import com.pactera.enterprisesecretary.module.ChatMessage;
import com.pactera.enterprisesecretary.util.StaticProperty;

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
                break;
            default:
                break;
        }
    }
}
