package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.FirstLoadDataInteractor;
import ru.niceaska.gameproject.domain.IDataRepository;

@Module
public class FirstLoadModule {

    @GameScope
    @Provides
    public FirstLoadDataInteractor providesFirstLoadInteractor(IDataRepository dataRepository) {
        return new FirstLoadDataInteractor(dataRepository);
    }
}
