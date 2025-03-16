package com.example.assignment1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "trip_info_table")
public class TripInfo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "destination")
    private String destination;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "people_count")
    private int peopleCount;

    @ColumnInfo(name = "budget")
    private double budget;

    public TripInfo(String destination, String date, int peopleCount, double budget) {
        this.destination = destination;
        this.date = date;
        this.peopleCount = peopleCount;
        this.budget = budget;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

//    @Override
//    public String toString() {
//        return "Trip to " + destination + " on " + date +
//                " with " + peopleCount + " people. Budget: $" + budget;
//    }
}