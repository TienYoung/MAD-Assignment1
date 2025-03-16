package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TripListAdapter extends ArrayAdapter<TripInfo> {
    public TripListAdapter(Context context, List<TripInfo> trips) {
        super(context, 0, trips);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TripInfo trip = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.trip_list_item,
                    parent, false);
        }

        TextView destinationTextView = convertView.findViewById(
                R.id.destinationTextView);

        TextView dateTextView = convertView.findViewById(
                R.id.dateTextView);

        destinationTextView.setText(trip.getDestination());
        dateTextView.setText(trip.getDate());

        return convertView;
    }
}
