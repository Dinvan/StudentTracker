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
import com.raynsmartschool.session.SessionParam;

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
        String notification_type = "";
        String messageBody = "";
        String contest_id = "";
        String contest_name = "";
        String contest_unique_id = "";
        String content = "";

        for (Map.Entry<String, String> entry : payload.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("KEY : " + key);
            System.out.println("VALUE : " + value);
            if (key.equalsIgnoreCase("notification_type")) {
                notification_type = value;
            }
            if (key.equalsIgnoreCase("body")) {
                messageBody = value;
            }

            if (key.equalsIgnoreCase("content")) {
                content = value;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        if (!TextUtils.isEmpty(SessionParam.getSessionKey(this))) {

            if (notification_type.equals("1") || notification_type.equals("6") || notification_type.equals("4") ||
                    notification_type.equals("7") || notification_type.equals("5")
                    ) {

            } else if (notification_type.equals("3")
                    || notification_type.equals("8")
                    || notification_type.equals("10")
                    || notification_type.equals("12")
                    || notification_type.equals("13")

                    ) {
                    intent = new Intent(this, ParentDashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            } else {
                intent = new Intent();
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
                .setContentText("You have one new message")
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

        notificationManager.notify(1410, notificationBuilder.build());
    }
}
