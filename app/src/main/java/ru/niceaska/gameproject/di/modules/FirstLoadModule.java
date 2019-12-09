package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.domain.interactors.FirstLoadDataInteractor;

@Module
public class FirstLoadModule {

    @GameScope
    @Provides
    public FirstLoadDataInteractor providesFirstLoadInteractor(IDataRepository dataRepository) {
        return new FirstLoadDataInteractor(dataRepository);
    }
}
