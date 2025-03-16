package com.example.assignment1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

public class TripDetailActivity extends ComponentActivity {
    private TextView destinationTextView, dateTextView, peopleCountTextView, budgetTextView;
    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        db = AppDatabase.getDatabase(this);

        destinationTextView = findViewById(R.id.detailDestinationTextView);
        dateTextView = findViewById(R.id.detailDateTextView);
        peopleCountTextView = findViewById(R.id.detailPeopleCountTextView);
        budgetTextView = findViewById(R.id.detailBudgetTextView);

        int tripId = getIntent().getIntExtra("TRIP_ID", -1);
        if (tripId != -1) {
            loadTripDetails(tripId);
        }
    }

    private void loadTripDetails(int tripId) {
        TripInfo trip = db.tripInfoDao().getAllTripInfo().stream().filter(
                t -> t.getId() == tripId).findFirst().orElse(null);
        if (trip != null) {
            destinationTextView.setText("Destination: " + trip.getDestination());
            dateTextView.setText("Date: " + trip.getDate());
            peopleCountTextView.setText("People Count: " + trip.getPeopleCount());
            budgetTextView.setText("Budget: " + trip.getBudget());
        }
    }
}
