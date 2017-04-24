package com.pactera.enterprisesecretary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.pactera.enterprisesecretary.R;

import java.util.List;

//聊天页面
public class ChatActivity extends MyBaseActivity {


    private Button chatMoreButton,chatSendButton = null;
    private ImageButton chatTypeButton= null;
    private EditText chatMessageEditText= null;
    private View chatVoiceButton= null;
    private RelativeLayout chatMoreView= null;
    private ScrollView chatMoreScrollView= null;
    private ImageButton[] moreButtons= null;

    private ListView multiChatList= null;// 信息显示列表
    private List<Object> messageList= null;
//    public ChatAdapter chatAdapter= null;

    // 设计师版隐藏布局中的四个按钮
    private Button button_sendImage, button_phone, button_inviteOrder;// 客户版隐藏布局中的发送图片按钮,拨打电话,邀请查看订单按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



    }
}
