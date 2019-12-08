package ru.niceaska.gameproject.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.data.repository.AppDatabase;

import static android.content.Context.MODE_PRIVATE;

@Module
public class AppModule {

    private static final String GAME_PREFS = "game_prefs";
    private static final String DATABASE = "database";
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public SharedPreferences getSharedPrefrence() {
        return context.getSharedPreferences(GAME_PREFS, MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public Context getAppContext() {
        return context;
    }

    @Provides
    @Singleton
    public AppDatabase getAppDatabase() {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE)
                .fallbackToDestructiveMigration()
                .build();
    }
}
