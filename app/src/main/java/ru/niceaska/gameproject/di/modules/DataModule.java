package ru.niceaska.gameproject.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.domain.IDataRepository;

@Module
public class DataModule {

    @Provides
    @Singleton
    public IDataRepository getRepository() {
        return new DataRepository();
    }
}
