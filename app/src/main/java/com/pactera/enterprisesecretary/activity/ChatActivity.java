package com.pactera.enterprisesecretary.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pactera.enterprisesecretary.BuildConfig;
import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.adapter.ChatAdapter;
import com.pactera.enterprisesecretary.custom.AudioRecorderButton;
import com.pactera.enterprisesecretary.module.ChatMessage;
import com.pactera.enterprisesecretary.util.CommonUtil;
import com.pactera.enterprisesecretary.util.MyDatabaseHelper;
import com.pactera.enterprisesecretary.util.MyDatabaseUtil;
import com.pactera.enterprisesecretary.util.StaticProperty;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.message;

//聊天页面
public class ChatActivity extends MyBaseActivity implements View.OnClickListener {

    public CommonUtil obtainInterfaceUtil = new CommonUtil();//通用方法

    //持久化信息
    SharedPreferences share = null;
    SharedPreferences.Editor sedit = null;

    //数据库
    private SQLiteOpenHelper sqLiteOpenHelper = null;
    private MyDatabaseUtil myDatabaseUtil = null;

    private RelativeLayout chatMoreButton, chatSendButton = null;
    private ImageView chatMoreImageView, chatSendImageView = null;
    private ImageButton chatTypeButton = null;
    private AudioRecorderButton chatAudioButton; //语音按钮
    private EditText chatMessageEditText = null;
    private RelativeLayout chatMoreView = null;
    private ScrollView chatMoreScrollView = null;
    private ImageButton[] moreButtons = null;

    private ListView chatListView = null;// 信息显示列表
    private List<ChatMessage> messageList = null;
    public ChatAdapter chatAdapter= null;

    private InputMethodManager inputMethodManager = null;//键盘管理器

    private String photoName;// 随机生成的照片名
    private Uri imageUri;// 图片资源标记，拍照时返回，根据uri获取图片路径

    private int screenHeight = 0;
    private int screenWidth = 0;


    private List<Map<String, Object>> itemList = null;//滚动视图扩展按钮


    //位移动画
    private Animation animationOut, animationIn;

    // 设计师版隐藏布局中的四个按钮
    private Button button_sendImage, button_phone, button_inviteOrder;// 客户版隐藏布局中的发送图片按钮,拨打电话,邀请查看订单按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        super.hiddenGoButton();

        //测试数据
        //测试数据
        this.itemList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 4; i++) {
            Map<String, Object> myMap = new HashMap<String, Object>();
            myMap.put("name", "加载中");
            myMap.put("description", "加载中......");
            myMap.put("image",R.drawable.test_chat_button01);
            this.itemList.add(myMap);
        }


        //保存全局信息
        share = this.getSharedPreferences(
                StaticProperty.SAVEINFO, Activity.MODE_PRIVATE);
        screenWidth = share.getInt(StaticProperty.SCREENWIDTH,480);
        screenHeight = share.getInt(StaticProperty.SCREENHEIGHT,800);

        // 实例化数据库
        this.sqLiteOpenHelper = new MyDatabaseHelper(this);
        this.myDatabaseUtil = new MyDatabaseUtil(
                 this.sqLiteOpenHelper.getWritableDatabase());


        //获取权限
        StaticProperty.verifyStoragePermissions(this);

        this.chatMoreView = (RelativeLayout) super.findViewById(R.id.chatMoreView);
        this.chatMoreButton = (RelativeLayout) super.findViewById(R.id.chatMoreButton);
        this.chatMoreButton.setOnClickListener(this);
        this.chatSendButton = (RelativeLayout) super.findViewById(R.id.chatSendButton);
        this.chatSendButton.setTag("0");//默认发送图片
        this.chatSendButton.setOnClickListener(this);
        this.chatMoreImageView = (ImageView) super.findViewById(R.id.chatMoreImageView);
        this.chatSendImageView = (ImageView) super.findViewById(R.id.chatSendImageView);

        this.chatMessageEditText = (EditText) super.findViewById(R.id.chatMessageEditText);
        this.chatTypeButton = (ImageButton)super.findViewById(R.id.chatTypeButton);
        this.chatTypeButton.setTag("0");
        this.chatTypeButton.setOnClickListener(this);
        this.chatAudioButton = (AudioRecorderButton) super.findViewById(R.id.chatAudioButton);
        this.chatAudioButton.setFinishRecorderCallBack(new AudioRecorderButton.AudioFinishRecorderCallBack() {

            //录音完成，时间seconds。文件路径filePath
            public void onFinish(float seconds, String filePath) {
                Log.e(TAG,seconds+":"+filePath);

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setName("我");
                chatMessage.setMessageFlag(1);
                Date sentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                chatMessage.setSentTime(sdf.format(sentDate));
                chatMessage.setContentType(StaticProperty.CHATVOICE);// 存入信息类型
                chatMessage
                        .setVoiceTime((int)seconds);//保存整秒
                chatMessage.setVoicePath(filePath);
                messageList.add(chatMessage);

//                ChatActivity.this.chatListView
//                        .setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                ChatActivity.this.chatAdapter.notifyDataSetChanged();
                ChatActivity.this.insertMessaage(chatMessage);//保存信息到数据库
            }
        });

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
                    ChatActivity.this.chatSendButton.setTag("1");//发送文字
                    ChatActivity.this.changeSendButton();
                } else if(edit.length() == 0){
                    ChatActivity.this.chatSendButton.setTag("0");//发送图片
                    ChatActivity.this.changeSendButton();
                }

            }
        });


        //聊天按钮
        this.chatListView = (ListView)super.findViewById(R.id.chatListView);
        this.chatListView
                .setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        this.messageList = new ArrayList<ChatMessage>();
        //获取聊天记录历史数据
        this.messageList = this.myDatabaseUtil.findAllMessage();
        this.chatAdapter = new ChatAdapter(this, messageList);
        this.chatListView.setAdapter(this.chatAdapter);
        //istview随item的增加而向上滚动


        // 点击更多显示动画
        this.animationIn = AnimationUtils.loadAnimation(
                ChatActivity.this, R.anim.activity_movein);
        this.animationOut = AnimationUtils.loadAnimation(
                ChatActivity.this, R.anim.activity_exitout);

        //触摸屏幕隐藏键盘
       this.inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        this.chatListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){

                        ChatActivity.this.inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });



        //扩展按钮,滚动视图区域
        this.chatMoreScrollView = (ScrollView)super.findViewById(R.id.chatMoreScrollView);
        int itemWidth = screenWidth/4;
        int itemHeight = (int)(itemWidth*1.0);//272dx
        RelativeLayout scrollRelativeLayout = new RelativeLayout(this);//相对布局

        //每个scrollView的子视图
        for (int i = 0; i < this.itemList.size(); i++) {
            Map<String,Object> myMap = this.itemList.get(i);

            View itemView = LayoutInflater.from(this).inflate(
                    R.layout.scrollview_chat, null);
            RelativeLayout.LayoutParams itemLyParam = new RelativeLayout.LayoutParams(
                    itemWidth, itemHeight);
            itemLyParam.leftMargin = i*itemWidth;
            itemView.setLayoutParams(itemLyParam);

            ImageView itemImageView = (ImageView)itemView.findViewById(R.id.itemChatIconImageView);
            itemImageView.setImageResource((int)myMap.get("image"));

            TextView itemTextView = (TextView)itemView.findViewById(R.id.itemChatTitleTextView);
            itemTextView.setText( myMap.get("name").toString());

            final int index = i;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),
                            "点击了："+(index+1) , Toast.LENGTH_LONG).show();
                }
            });
            scrollRelativeLayout.addView(itemView);
        }
        this.chatMoreScrollView.addView(scrollRelativeLayout);

    }

    //改变发送
    public void changeSendButton() {
        if (this.chatSendButton.getTag().equals("0")) {
            this.chatSendImageView.setImageResource(R.drawable.chat_send_picture);
        } else {
            this.chatSendImageView.setImageResource(R.drawable.chat_send_text);
        }
    }

    //保存发送信息
    public void insertMessaage(ChatMessage chatMessage){
        this.myDatabaseUtil
                .insertMessage(chatMessage);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chatTypeButton:// 点击发送按钮
                if("0".equals(chatTypeButton.getTag())){//当前为文本，切换到语音
                    ChatActivity.this.chatTypeButton.setTag("1");
                    ChatActivity.this.chatTypeButton.setImageResource(R.drawable.chat_keyboard);
                    this.chatAudioButton.setVisibility(View.VISIBLE);
                    this.chatMessageEditText.setVisibility(View.GONE);
                }else {//切换到文本
                    ChatActivity.this.chatTypeButton.setTag("0");
                    ChatActivity.this.chatTypeButton.setImageResource(R.drawable.chat_audio);
                    this.chatMessageEditText.setVisibility(View.VISIBLE);
                    this.chatAudioButton.setVisibility(View.GONE);
                }
                break;

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
                Log.e(TAG,"点击了发送按钮!!!!");
                if(view.getTag().equals("1")){//发送文本
                    Log.e(TAG,"发送文本!!!!");
                    String message = this.chatMessageEditText.getText().toString();
                    if (message == null || message.equals("")) {
                        Toast.makeText(ChatActivity.this, "内容不能为空",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "msg" + message);
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setName("我");
                        chatMessage.setMessageFlag(1);
                        Date sentDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        chatMessage.setSentTime(sdf.format(sentDate));
                        chatMessage.setContentType(StaticProperty.CHATINFO);// 存入信息类型
                        chatMessage.setTextContent(message);
                        messageList.add(chatMessage);
                        //istview随item的增加而向上滚动
//                        this.chatListView
//                                .setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                        chatAdapter.notifyDataSetChanged();
                        Log.i(TAG, "发送完成" + message);
                        this.chatMessageEditText.setText("");

                        ChatActivity.this.insertMessaage(chatMessage);//保存信息到数据库
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
                    LayoutInflater layoutInflater = LayoutInflater.from(this);
                    View longinDialogView = layoutInflater.inflate(R.layout.dialog_title, null);
                    builder.setCustomTitle(longinDialogView);
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
//
                                                    File imagePath = new File(getFilesDir(), "Resource");
                                                    File newFile = new File(imagePath, ChatActivity.this.photoName);
                                                    //首次时必须创建！！！
                                                    if (!imagePath.exists()) {
                                                        imagePath.mkdirs();
                                                    }

                                                    ChatActivity.this.imageUri = FileProvider.getUriForFile(ChatActivity.this,"com.wow.fileprovider", newFile);//通过FileProvider创建一个content类型的Uri
                                                    Intent intent = new Intent();
                                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, ChatActivity.this.imageUri);//将拍取的照片保存到指定URI
                                                    startActivityForResult(intent,10);




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




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(
//                ChatActivity.this,
//                requestCode+":"+resultCode, Toast.LENGTH_LONG)
//                .show();
        Log.e(TAG, requestCode+"-----单聊跳转返回结果：" + resultCode);
        boolean sendFlag = true;//发送成功
        String photoPath = null;//图片路径
        ChatMessage chatMessage = new ChatMessage();//消息发送vo类
        chatMessage.setName("我");
        // 拍照之后回传的数据
        if (requestCode == 10 && resultCode == RESULT_OK) {
            File imagePath = new File(getFilesDir(), "Resource");
            File newFile = new File(imagePath, ChatActivity.this.photoName);
            Log.e(TAG, "图片路径为：" +  newFile.getPath());
            photoPath = newFile.getPath();
            chatMessage.setImageType(0);//暂时未使用！

//            photoBitmap = BitmapFactory.decodeFile(photoPath);
//            photoBitmap = obtainInterfaceUtil.getBitmapByPath(photoPath, 1000,
//                    1000);
//            Log.e(TAG, "图片为：" +  photoBitmap);
//            int degree = obtainInterfaceUtil
//                    .getBitmapDegree(StaticProperty.FILEPATH + photoName);
//            photoBitmap = obtainInterfaceUtil.rotateBitmapByDegree(photoBitmap, degree);
//            bitmap.recycle();
//            bitmap = null;
//            System.gc();

        } else if (requestCode == 11 && resultCode == RESULT_OK) {// 从相册中获取照片之后回传的数据
            ChatActivity.this.imageUri = data.getData();
            photoPath = obtainInterfaceUtil.getRealFilePathByUri(
                    ChatActivity.this, ChatActivity.this.imageUri);//必须加入运行时权限，才可以根据路径获取图片！
            Log.e(TAG, "图片路径为：" +  photoPath);
            chatMessage.setImageType(1);

        }else { //发送失败
            sendFlag = false;
        }

        //发送成功，保存图片地址
        if (sendFlag && !"".equals(photoPath)) {//图片显示以uri为根据
                try {
                    chatMessage.setMessageFlag(1);
//                    chatMessage.setImageUri(ChatActivity.this.imageUri);//保存图片资源编号
                    Date sentDate = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    chatMessage.setSentTime(sdf.format(sentDate));
                    chatMessage.setContentType(StaticProperty.CHATIMAGE);// 存入信息类型
                    chatMessage.setImagePath(photoPath);
                    messageList.add(chatMessage);
//                    chatListView
//                            .setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    chatAdapter.notifyDataSetChanged();

                    ChatActivity.this.insertMessaage(chatMessage);//保存信息到数据库
                } catch (Exception e) {
                    Log.i(TAG, "发送异常" + e.toString());
                    e.printStackTrace();
                    Toast.makeText(ChatActivity.this,
                            "发送失败，请重试！", Toast.LENGTH_SHORT).show();
                }
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
//        MediaPlayerManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MediaPlayerManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.myDatabaseUtil.closeDatabase();
//        MediaPlayerManager.release();
    }

}
