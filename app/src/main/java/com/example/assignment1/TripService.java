package com.example.assignment1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TripService extends Service {
    private static final String TAG = TripService.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.i(TAG, "Trip Service Created");
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
}
