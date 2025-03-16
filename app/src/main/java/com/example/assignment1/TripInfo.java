package com.example.assignment1;

import java.io.Serializable;

public class TripInfo implements Serializable {
    private String destination;
    private String date;
    private int peopleCount;
    private double budget;

    public TripInfo(String destination, String date, int peopleCount, double budget) {
        this.destination = destination;
        this.date = date;
        this.peopleCount = peopleCount;
        this.budget = budget;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public double getBudget() {
        return budget;
    }

    @Override
    public String toString() {
        return "Trip to " + destination + " on " + date +
                " with " + peopleCount + " people. Budget: $" + budget;
    }
}