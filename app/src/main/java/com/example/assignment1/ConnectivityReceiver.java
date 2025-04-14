package com.example.assignment1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class ConnectivityReceiver extends BroadcastReceiver {
    private static final String TAG = ConnectivityReceiver.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d(TAG, "Network State Changed");
            ConnectivityManager manager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network activeNetwork = manager.getActiveNetwork();
            if (activeNetwork == null) {
                Log.d(TAG, "No active network");
                return;
            }

            NetworkCapabilities capabilities = manager.getNetworkCapabilities(activeNetwork);
            boolean isConnected = capabilities != null &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            Log.d(TAG, "Internet connection: " + isConnected);
        }
    }
}
