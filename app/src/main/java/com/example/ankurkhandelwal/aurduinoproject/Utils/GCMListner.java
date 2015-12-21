package com.example.ankurkhandelwal.aurduinoproject.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.ankurkhandelwal.aurduinoproject.R;
import com.example.ankurkhandelwal.aurduinoproject.SplashActivity;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMListner extends GcmListenerService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String msg = data.getString("extra");
        String title=data.getString("title");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        notifyUser(getApplicationContext(),msg,title);
    }

    private void notifyUser(Context applicationContext, String msg,String title) {
        if(msg!=null){
            notificationManager=(NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Bitmap icon= BitmapFactory.decodeResource(getResources(), R.drawable.ic_arduino);
            PendingIntent pendingIntent=PendingIntent.getActivity(applicationContext,0,new Intent(getApplicationContext(),SplashActivity.class),0);
            NotificationCompat.Builder builder=new NotificationCompat.Builder(applicationContext).setLargeIcon(icon).setSmallIcon(R.drawable.ic_logo).setContentTitle(title).setContentText(msg).setAutoCancel(true);
            builder.setDefaults(Notification.DEFAULT_SOUND);
            builder.setContentIntent(pendingIntent);
            notificationManager.notify(NOTIFICATION_ID,builder.build());
        }
    }
}
