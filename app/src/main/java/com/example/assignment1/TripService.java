package com.example.assignment1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TripService extends Service {
    private static final String TAG = TripService.class.getSimpleName();
    private static final String CHANNEL_ID = "TripNotificationChannel";

    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        Log.i(TAG, "Trip Service Created");
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Trip Service Started with id=" + startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Trip Service Destroyed");
        super.onDestroy();
    }

    /**
     * Creates a notification channel for Android O and above
     */
    private void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Trip Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications about upcoming trips");
            notificationManager.createNotificationChannel(channel);
        }
    }
}
