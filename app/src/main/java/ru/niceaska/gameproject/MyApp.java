package ru.niceaska.gameproject;

import android.app.Application;

import androidx.room.Room;

import ru.niceaska.gameproject.data.repository.AppDatabase;
import ru.niceaska.gameproject.di.components.AppComponent;
import ru.niceaska.gameproject.di.components.DaggerAppComponent;
import ru.niceaska.gameproject.di.modules.AppModule;
import ru.niceaska.gameproject.di.modules.DataModule;

public class MyApp extends Application {
    public static MyApp instance;
    private static final String DATABASE = "database";

    private AppComponent appComponent;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE)
                .fallbackToDestructiveMigration()
                .build();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .dataModule(new DataModule())
                .build();


    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static MyApp getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
