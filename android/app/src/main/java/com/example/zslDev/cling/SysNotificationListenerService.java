package com.example.zslDev.cling;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.hicling.clingsdk.ClingSdk;

public class SysNotificationListenerService extends NotificationListenerService {
    static private String TAG = SysNotificationListenerService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: SysNotificationListenerService");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "onNotificationPosted: sbn" + sbn);
        if (sbn != null) {
            Log.d(TAG, "got notification from: " + sbn.toString());

            Notification notification = sbn.getNotification();

            if (notification != null && notification.tickerText != null) {

                ClingSdk.sendSmartNotification(notification);

            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "remove notification from: " + sbn.getPackageName());
    }

}
