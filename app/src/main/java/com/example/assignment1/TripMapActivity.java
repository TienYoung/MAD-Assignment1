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
        if (this.googleMap != null) {
            setupMap();
        }
    }

    private void setupMap() {
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);
        this.googleMap.getUiSettings().setScrollGesturesEnabled(true);
    }

    // Called MapView lifecycle methods
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle("MapViewBundleKey");
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle("MapViewBundleKey", mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}