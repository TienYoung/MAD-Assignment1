package com.example.assignment1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class TripService extends Service {
    private static final String TAG = TripService.class.getSimpleName();
    private static final String CHANNEL_ID = "TripNotificationChannel";

    private NotificationManager notificationManager;
    private ConnectivityReceiver connectivityReceiver;

    @Override
    public void onCreate() {
        Log.i(TAG, "Trip Service Created");

        createNotificationChannel();

        registerReceivers();
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
        unregisterReceivers();
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

    /**
     * Registers all broadcast receivers used by the service
     */
    private void registerReceivers() {
        // Connectivity
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter connFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.registerReceiver(this, connectivityReceiver, connFilter, ContextCompat.RECEIVER_EXPORTED);
        } else {
            registerReceiver(connectivityReceiver, connFilter);
        }
    }

    /**
     * Unregisters all broadcast receivers
     */
    private void unregisterReceivers() {
        try {
            unregisterReceiver(connectivityReceiver);
        } catch (Exception e) {
            Log.e(TAG, "Error unregistering receivers", e);
        }
    }
}
