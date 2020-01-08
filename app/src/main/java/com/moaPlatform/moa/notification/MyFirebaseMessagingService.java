package com.moaPlatform.moa.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.intro.IntroActivity;
import com.moaPlatform.moa.preference.PushTokenPreference;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String NOTIFICATION_DATA_KEY = "message";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Logger.d("onMessageReceived From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage != null && remoteMessage.getData() != null) {
            Logger.d("onMessageReceived data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage);
        } else {
            Logger.d("onMessageReceived Message data payload: empty");
        }
    }

    @Override
    public void onNewToken(String token) {
        Logger.d("Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        if (ObjectUtil.checkNotNull(token)) {
            PushTokenPreference.getInstance().setUpload(getApplicationContext(), false);
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message body received.
     */
    private void sendNotification(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().containsKey(NOTIFICATION_DATA_KEY)) {

            NotificationData notificationData = new Gson().fromJson(remoteMessage.getData().get(NOTIFICATION_DATA_KEY), NotificationData.class);

            Logger.d("sendNotification >>> " + notificationData.toString());

            if (notificationData != null && ObjectUtil.checkNotNull(notificationData.pushTitleText) && ObjectUtil.checkNotNull(notificationData.pushContentText)) {
                Intent intent = new Intent(this, IntroActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(NOTIFICATION_DATA_KEY, new Gson().toJson(notificationData));
                PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (System.currentTimeMillis() / 1000) /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                String channelId = getString(R.string.default_notification_channel_id);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                .setTicker(notificationData.pushTitleText)           //푸시 수신시 알림영역에 뜨는 한줄
                                .setContentTitle(notificationData.pushTitleText)
                                .setContentText(notificationData.pushContentText)
                                .setGroupSummary(true)
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, getString(R.string.push_name),
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify((int) (System.currentTimeMillis() / 1000) /* ID of notification */, notificationBuilder.build());
            }
        }
    }

}
