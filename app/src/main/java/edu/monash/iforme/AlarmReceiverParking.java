package edu.monash.iforme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by yashkhandha on 7/06/2018.
 */

public class AlarmReceiverParking extends BroadcastReceiver {

    //Notification id to identify the notification
    private static final int NOTIFICATION_ID = 1;
    //Notification manager to manage the incoming calls
    private NotificationManager notificationManager;

    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

    public AlarmReceiverParking(){

    }

    /**
     * This method is called when the notificaiton is to be triggered
     * @param context current context
     * @param intent Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Start MainActivity on click of the notification
        Intent contentIntent = new Intent(context,MainActivity.class);
        //Create pending intent to capture the current state of notification
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context,NOTIFICATION_ID,contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Check for Build version of current device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //This will create the notification and can be modified in style and text
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_medicine_alert)
                .setContentTitle(context.getString(R.string.notification_title_parking))
                .setContentText(context.getString(R.string.notification_text_parking))
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        //Push the notification
        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }
}
