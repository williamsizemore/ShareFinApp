package com.example.sharefinapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    public NotificationService() {
    }
    /*
        notification service for Firebase Cloud messaging
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        // todo handle FCM messages here
        Log.d("onMessageReceived","From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0)
        {
            Log.d("onMessageReceived","Message data payload: " + remoteMessage.getData());


        }

    }

}