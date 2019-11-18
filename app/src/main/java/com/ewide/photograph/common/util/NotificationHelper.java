package com.ewide.photograph.common.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.ewide.photograph.R;

/**
 * author：Taozebi
 * date：2018/10/11 15:20
 * describe：通知帮助类
 */

public class NotificationHelper extends ContextWrapper {
    private NotificationManager mNotificationManager;
    private NotificationChannel mNotificationChannel;

    public static final  String CHANNEL_ID          = "default";
    private static final String CHANNEL_NAME        = "Default Channel";
    private static final String CHANNEL_DESCRIPTION = "this is default channel!";

    public NotificationHelper(Context base){
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mNotificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationChannel.setDescription(CHANNEL_DESCRIPTION);
            getNotificationManager().createNotificationChannel(mNotificationChannel);
        }
    }

    public NotificationCompat.Builder getNotification(String title, String content){
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else{
            builder = new NotificationCompat.Builder(this);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //点击自动删除通知
        builder.setAutoCancel(true);
        return builder;
    }

    public void notify(int id, NotificationCompat.Builder builder){
        if (getNotificationManager() != null){
            getNotificationManager().notify(id, builder.build());
        }
    }

    public void openChannelSetting(String channelId){
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null)
            startActivity(intent);
    }

    public void openNotificationSetting(){
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null)
            startActivity(intent);
    }

    private NotificationManager getNotificationManager(){
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

}
