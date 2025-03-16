package com.example.assignment1;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TripInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TripInfoDao tripInfoDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
