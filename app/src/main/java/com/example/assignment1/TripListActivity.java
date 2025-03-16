package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import java.util.List;

public class TripListActivity extends ComponentActivity {
    private ListView tripListView;
    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        tripListView = findViewById(R.id.tripListView);
        db = AppDatabase.getDatabase(this);

        loadTrips();
    }

    private void loadTrips() {
        List<TripInfo> trips = db.tripInfoDao().getAllTripInfo();
        TripListAdapter adapter = new TripListAdapter(this, trips);
        tripListView.setAdapter(adapter);

        tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TripInfo selectedTrip = (TripInfo) parent.getItemAtPosition(position);
                showTripDetail(selectedTrip);
            }
        });
    }

    private void showTripDetail(TripInfo trip) {
        Intent intent = new Intent(this, TripDetailActivity.class);
        intent.putExtra("TRIP_ID", trip.getId());
        startActivity(intent);
    }
}
