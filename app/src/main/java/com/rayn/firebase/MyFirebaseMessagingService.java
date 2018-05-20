package com.rayn.firebase;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rayn.R;
import com.rayn.auth.ParentDashboardActivity;
import com.rayn.school.AnnouncementActivity;
import com.rayn.school.StudentAttendanceListActivity;
import com.rayn.session.SessionParam;
import com.rayn.utility.Config;

import java.util.Map;

/**
 * Created by ravib on 32/05/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // fsfs();
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        /*if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData());
        }*/
        sendNotification(remoteMessage.getData());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


    }


    private void sendNotification(Map<String, String> payload) {

        SessionParam sessionParam = new SessionParam(this);
        Intent intent = null;
        String title = payload.get("title");
        String messageBody =  payload.get("message");
        String notification_type = payload.get("type");
        String student_id = payload.get("id");
        String student_name = payload.get("stname");



        if (!TextUtils.isEmpty(SessionParam.getSessionKey(this))) {

            if (notification_type.equals("homework") ) {
                intent = AnnouncementActivity.getIntent(this, Config.TYPE_HOMEWORK,true,"");
                SessionParam.setNotificationPref(this,1,student_id);
                //     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            else if (notification_type.equals("announcement") ) {
                intent = AnnouncementActivity.getIntent(this, Config.TYPE_ANNOUNCEMENT,true,"");
                SessionParam.setNotificationPref(this,2,student_id);
                //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }else if (notification_type.equals("attendance")) {
                intent = StudentAttendanceListActivity.getIntent(this,true,"");
                SessionParam.setNotificationPref(this,3,student_id);
                //      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
                intent = new Intent(this, ParentDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

        } /*else {
            intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }*/

        if(null!=intent) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                    intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(this)
                    //      .setLargeIcon(largeIcon)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                notificationBuilder.setLights(Color.RED, 3000, 3000);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder.setSound(alarmSound);
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            } else {

                notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                notificationBuilder.setLights(Color.RED, 3000, 3000);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder.setSound(alarmSound);
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            }


            NotificationManager notificationManager =
                    (NotificationManager)
                            getSystemService(Context.NOTIFICATION_SERVICE);
            int notiId = (int) System.currentTimeMillis();
            notificationManager.notify(notiId, notificationBuilder.build());
        }
    }
}
