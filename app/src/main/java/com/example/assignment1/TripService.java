package com.example.assignment1;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        checkUpcomingTrips();
        scheduleNextCheck();
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

    private void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Trip Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Notifications about upcoming trips");
        notificationManager.createNotificationChannel(channel);
    }

    private void registerReceivers() {
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter connFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.registerReceiver(this, connectivityReceiver, connFilter, ContextCompat.RECEIVER_EXPORTED);
        } else {
            registerReceiver(connectivityReceiver, connFilter);
        }
    }

    private void unregisterReceivers() {
        try {
            unregisterReceiver(connectivityReceiver);
        } catch (Exception e) {
            Log.e(TAG, "Error unregistering receivers", e);
        }
    }

    private void checkUpcomingTrips() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            List<TripInfo> trips = db.tripInfoDao().getAllTripInfo();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String today = dateFormat.format(new Date());

            for (TripInfo trip : trips) {
                try {
                    Date tripDate = dateFormat.parse(trip.getDate());
                    Date currentDate = dateFormat.parse(today);

                    if (tripDate != null && currentDate != null) {
                        long diffInMillies = tripDate.getTime() - currentDate.getTime();
                        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

                        if (diffInDays >= 0 && diffInDays <= 7) {
                            sendTripNotification(trip, diffInDays);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error checking trip date", e);
                }
            }
        }).start();
    }


    private void sendTripNotification(TripInfo trip, long daysUntilTrip) {
        String title = "Upcoming Trip: " + trip.getDestination();
        String message;
        if (daysUntilTrip == 0) {
            message = "Your trip to " + trip.getDestination() + " is today!";
        } else if (daysUntilTrip == 1) {
            message = "Your trip to " + trip.getDestination() + " is tomorrow!";
        } else {
            message = "Your trip to " + trip.getDestination() + " is in " + daysUntilTrip + " days.";
        }
        if (notificationManager != null) {
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(trip.getId(), notification);
        }
    }

    private void scheduleNextCheck() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, TripService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        long futureInMillis = SystemClock.elapsedRealtime() + 24 * 60 * 60 * 1000;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
