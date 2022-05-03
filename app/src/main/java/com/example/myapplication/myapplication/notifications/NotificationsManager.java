package com.example.myapplication.myapplication.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.myapplication.R;
import com.example.myapplication.myapplication.models.NotificationModel;
import com.example.myapplication.myapplication.ui.SplashActivity;

import java.util.Random;


public class NotificationsManager {


//    public void showNotification(final Context context, final NotificationModel model) {
//        try {
//            Intent intent = new Intent(context, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
//                    .setSmallIcon(getNotificationIcon()).setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
//                    .setContentTitle(context.getString(R.string.app_name))
//                    .setContentText(model != null && model.getMessage() != null ? model.getMessage() : "Notification").setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentTitle(model != null && model.getTitle() != null ? model.getTitle() : "Notification")
//                    .setContentIntent(pendingIntent);
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            if (notificationManager != null) {
//                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//            }
//            //
//            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//            if (v != null) {
//                v.vibrate(500);
//            }
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//    }

    public void showNotification(Context context, NotificationModel model) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-fbase";
        String channelName = "demoFbase";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent notificationIntent = new Intent(context, SplashActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.logo_ic);
        } else {
            mBuilder.setSmallIcon(getNotificationIcon());
        }
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(model != null && model.getBody() != null ? model.getBody() : "Notification"));

        mBuilder.setContentTitle(model != null && model.getTitle() != null ? model.getTitle() : "Notification");
        mBuilder.setContentText(model != null && model.getBody() != null ? model.getBody() : "Notification").setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setContentIntent(pendingIntent);


        //If you don't want all notifications to overwrite add int m to unique value
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, mBuilder.build());
    }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logo_ic : R.drawable.logo_ic;
    }

}
