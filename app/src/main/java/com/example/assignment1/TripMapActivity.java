package com.example.assignment1;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

public class TripMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = TripMapActivity.class.getSimpleName();
    private GoogleMap googleMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_map);

        // Initialize UI elements
        mapView = findViewById(R.id.map_view);
        Button backButton = findViewById(R.id.back_button);

        // Initialize map
        Bundle mapViewBundle = savedInstanceState != null ?
                savedInstanceState.getBundle("MapViewBundleKey") : null;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        // Set back button click listener
        backButton.setOnClickListener(view -> finish());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
    }
}