package com.pactera.enterprisesecretary.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.pactera.enterprisesecretary.R;
import com.pactera.enterprisesecretary.activity.IndexActivity;

public class NotificationService extends Service {

    // 获取消息线程
    private MessageThread messageThread = null;

    // 点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    // 通知栏消息
    private int messageNotificationID = 1000;
    private Notification.Builder notificationBuilder = null;
    private NotificationManager messageNotificatioManager = null;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 初始化

        messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        messageIntent = new Intent(this, IndexActivity.class);
        messagePendingIntent = PendingIntent.getActivity(this, 0,
                messageIntent, 0);

        this.notificationBuilder = new Notification.Builder(this);
        this.notificationBuilder.setContentTitle("新消息");//标题
        this.notificationBuilder.setContentText("您有新消息。");//内容
        this.notificationBuilder.setSmallIcon(R.drawable.ic_launcher);//图标
        this.notificationBuilder .setContentIntent(messagePendingIntent);//点击通知跳转
        this.notificationBuilder.setDefaults(Notification.DEFAULT_ALL);//调用系统默认铃声
        this.notificationBuilder.setAutoCancel(true);//可删除


        // 开启线程
        messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从服务器端获取消息
     *
     */
    class MessageThread extends Thread {
        // 设置是否循环推送
        public boolean isRunning = true;

        public void run() {
             while (isRunning) {
            try {
                // 间隔时间
                Thread.sleep(5000);
                // 获取服务器消息
                String serverMessage = getServerMessage();
                if (serverMessage != null && !"".equals(serverMessage)) {
                    // 更新通知栏
                    NotificationService.this.notificationBuilder.setContentText("您有新消息:"+messageNotificationID);
                    messageNotificatioManager.notify(messageNotificationID,
                            NotificationService.this.notificationBuilder.build());
                    // 每次通知完，通知ID递增一下，避免消息覆盖掉
                    messageNotificationID++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             }
        }
    }

    @Override
    public void onDestroy() {
        // System.exit(0);
//        messageThread.isRunning = false;
        super.onDestroy();
    }

    /**
     * 模拟发送消息
     *
     * @return 返回服务器要推送的消息，否则如果为空的话，不推送
     */
    public String getServerMessage() {
        return "NEWS!";
    }
}
