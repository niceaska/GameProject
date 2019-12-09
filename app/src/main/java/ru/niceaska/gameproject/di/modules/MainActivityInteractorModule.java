package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.domain.interactors.MainActivityInteractor;

@Module
public class MainActivityInteractorModule {

    @Provides
    @GameScope
    public MainActivityInteractor provideMainActivityInteractor(IDataRepository dataRepository) {
        return new MainActivityInteractor(dataRepository);
    }
}
