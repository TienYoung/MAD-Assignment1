package com.example.assignment1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

/**
 * Content provider for trip information data.
 * Allows other applications to access trip data stored in the app's database.
 */
public class TripContentProvider extends ContentProvider {
    private static final String TAG = TripContentProvider.class.getSimpleName();

    // Authority for this content provider
    public static final String AUTHORITY = "com.example.assignment1.provider";

    // URI paths
    public static final String TRIPS_PATH = "trips";

    // URI matcher codes
    private static final int TRIPS = 1;
    private static final int TRIP_ID = 2;

    // Content URI for trips
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TRIPS_PATH);

    // MIME types
    private static final String TRIPS_MIME_TYPE =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TRIPS_PATH;
    private static final String TRIP_MIME_TYPE =
            "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TRIPS_PATH;

    // URI matcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TRIPS_PATH, TRIPS);
        uriMatcher.addURI(AUTHORITY, TRIPS_PATH + "/#", TRIP_ID);
    }

    // Database reference
    private AppDatabase database;

    @Override
    public boolean onCreate() {
        database = Room.databaseBuilder(getContext(),
                        AppDatabase.class, "app_database")
                .allowMainThreadQueries()
                .build();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TRIPS:
                return TRIPS_MIME_TYPE;
            case TRIP_ID:
                return TRIP_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}