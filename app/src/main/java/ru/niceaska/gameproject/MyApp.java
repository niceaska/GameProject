package ru.niceaska.gameproject;

import android.app.Application;

import androidx.room.Room;

import ru.niceaska.gameproject.data.repository.AppDatabase;

public class MyApp extends Application {
    public static MyApp instance;
    private static final String DATABASE = "database";

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE)
                .fallbackToDestructiveMigration()
                .build();
    }

    public static MyApp getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
