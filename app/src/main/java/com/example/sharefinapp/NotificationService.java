package com.example.sharefinapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
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
    @Override
    public void onNewToken(String token)
    {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token)
    {
        DBManager.getInstance().getDb().collection("users").whereEqualTo("userID",DBManager.getInstance().getCurrentUserID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentReference docRef = queryDocumentSnapshots.getDocuments().get(0).getReference();

                DBManager.getInstance().updateDataField("users",docRef,"token",token);
            }
        });
    }

    public String getToken()
    {   StringBuilder token = new StringBuilder();
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                 token.append(s);
            }
        });
        return token.toString();
    }

    public void sendNotification(String message, ArrayList<String> recipientTokens)
    {
//        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("962093176245@fcm.googleapis.com").addData("message",message).build());
    }

}