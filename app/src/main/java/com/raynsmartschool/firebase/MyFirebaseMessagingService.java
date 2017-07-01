package com.raynsmartschool.firebase;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.raynsmartschool.R;
import com.raynsmartschool.auth.ParentDashboardActivity;
import com.raynsmartschool.school.AnnouncementActivity;
import com.raynsmartschool.school.StudentAttendanceListActivity;
import com.raynsmartschool.session.SessionParam;
import com.raynsmartschool.utility.Config;

import org.json.JSONException;
import org.json.JSONObject;

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



        if (!TextUtils.isEmpty(SessionParam.getSessionKey(this))) {

            if (notification_type.equals("homework") ) {
                intent = AnnouncementActivity.getIntent(this, Config.TYPE_HOMEWORK,true);
           //     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            else if (notification_type.equals("announcement") ) {
                intent = AnnouncementActivity.getIntent(this, Config.TYPE_ANNOUNCEMENT,true);
            //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }else if (notification_type.equals("attendance")) {
                    intent = StudentAttendanceListActivity.getIntent(this,true);
              //      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
                intent = new Intent(this, ParentDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

        } else {
            intent = new Intent(this, ParentDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }


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
            notificationBuilder.setVibrate(new long[0]);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
        int notiId = (int)System.currentTimeMillis();
        notificationManager.notify(notiId, notificationBuilder.build());
    }
}
