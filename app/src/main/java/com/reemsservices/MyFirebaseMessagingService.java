package com.reemsservices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

/**
 * Created by Sony on 12/14/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
        Log.d(TAG, "Data: " + remoteMessage.getData().toString());
        if (remoteMessage.getData().size() > 0) {

            String msg = remoteMessage.getData().toString();
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (!msg.isEmpty() && remoteMessage.getData().get("message") != null) {
                sendNotification(remoteMessage.getData().get("message").toString());
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    private void sendNotification(String messageBody) {
        String msg = "", title = "", type = "";
        try {
            JSONObject jsonObject = new JSONObject(messageBody);
            msg = jsonObject.optString("msg");
            title = jsonObject.optString("title");
            type = jsonObject.optString("type");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("FROM", type);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher).setStyle(new NotificationCompat.BigTextStyle());
        }
        else
        {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //for oreo
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        //end of code

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(channelId); // Channel ID
        }
        Notification note = notificationBuilder.build();

//        if (type.equalsIgnoreCase("new_request")) {
//            playSound();
//        } else {
            note.defaults |= Notification.DEFAULT_SOUND;
            note.defaults |= Notification.DEFAULT_VIBRATE;
//        }

        notificationManager.notify((int) System.currentTimeMillis() /* ID of notification */, note);

    }

//    public void playSound()
//    {
//        MediaPlayer mMediaPlayer = MediaPlayer.create(this, R.raw.sound);
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mMediaPlayer.setLooping(true);
//        mMediaPlayer.start();
//    }
}
