package com.example.assignment1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class TripDetailActivity extends ComponentActivity {
    private TextView destinationTextView, dateTextView, peopleCountTextView, budgetTextView;
    private ImageView destinationImageView;
    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        db = AppDatabase.getDatabase(this);

        destinationTextView = findViewById(R.id.detailDestinationTextView);
        destinationImageView = findViewById(R.id.detailDestinationImageView);
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
            // Set other views with image view by url
            Glide.with(this).load(trip.getImageUrl()).into(destinationImageView);
            dateTextView.setText("Date: " + trip.getDate());
            peopleCountTextView.setText("People Count: " + trip.getPeopleCount());
            budgetTextView.setText("Budget: " + trip.getBudget());
        }
    }
}
