package com.example.assignment1;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface TripInfoDao {
    @Insert
    void insert(TripInfo tripInfo);
}
