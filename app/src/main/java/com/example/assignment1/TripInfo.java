package com.example.assignment1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Entity class representing trip information stored in the Room database.
 * Each instance corresponds to a row in the trip_info_table.
 */
@Entity(tableName = "trip_info_table")
public class TripInfo {
    /**
     * Unique identifier for each trip record, automatically generated.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * Name of the trip destination.
     */
    @ColumnInfo(name = "destination")
    private String destination;

    /**
     * URL for the destination image.
     */
    @ColumnInfo(name = "image_url")
    private String imageUrl;

    /**
     * Date of the planned trip.
     */
    @ColumnInfo(name = "date")
    private String date;

    /**
     * Number of people participating in the trip.
     */
    @ColumnInfo(name = "people_count")
    private int peopleCount;

    /**
     * Budget allocated for the trip.
     */
    @ColumnInfo(name = "budget")
    private double budget;

    /**
     * Constructor to create a new TripInfo instance.
     *
     * @param destination Name of the destination
     * @param imageUrl URL of the destination image
     * @param date Trip date as a string
     * @param peopleCount Number of people on the trip
     * @param budget Trip budget amount
     */
    public TripInfo(String destination, String imageUrl, String date, int peopleCount, double budget) {
        this.destination = destination;
        this.imageUrl = imageUrl;
        this.date = date;
        this.peopleCount = peopleCount;
        this.budget = budget;
    }

    /**
     * Gets the trip ID.
     *
     * @return The unique identifier for the trip
     */
    public int getId() { return id; }

    /**
     * Sets the trip ID.
     *
     * @param id The unique identifier to set
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the trip destination.
     *
     * @return The name of the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the trip destination.
     *
     * @param destination The name of the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Gets the image URL for the destination.
     *
     * @return The URL of the destination image
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL for the destination.
     *
     * @param imageUrl The URL of the destination image to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the trip date.
     *
     * @return The date of the trip as a string
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the trip date.
     *
     * @param date The date of the trip to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the number of people on the trip.
     *
     * @return The count of people participating
     */
    public int getPeopleCount() {
        return peopleCount;
    }

    /**
     * Sets the number of people on the trip.
     *
     * @param peopleCount The count of people to set
     */
    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    /**
     * Gets the trip budget.
     *
     * @return The budget amount for the trip
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Sets the trip budget.
     *
     * @param budget The budget amount to set
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }

}