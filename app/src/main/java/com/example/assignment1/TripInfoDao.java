package com.example.assignment1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripInfoDao {
    @Insert
    void insert(TripInfo tripInfo);

    @Query("SELECT * FROM trip_info_table")
    List<TripInfo> getAllTripInfo();
}
