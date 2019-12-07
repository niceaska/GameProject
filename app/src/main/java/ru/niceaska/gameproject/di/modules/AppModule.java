package ru.niceaska.gameproject.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@Module
public class AppModule {

    private final String GAME_PREFS = "game_prefs";
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public SharedPreferences getSharedPrefrence() {
        return context.getSharedPreferences(GAME_PREFS, MODE_PRIVATE);
    }
}
