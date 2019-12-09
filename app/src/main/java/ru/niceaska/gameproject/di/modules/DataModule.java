package ru.niceaska.gameproject.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.data.repository.AppDatabase;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.domain.IDataRepository;

@Module
public class DataModule {

    @Provides
    @Singleton
    public IDataRepository getRepository(AppDatabase database,
                                         Context context,
                                         SharedPreferences preferences) {
        return new DataRepository(database, context, preferences);
    }
}
