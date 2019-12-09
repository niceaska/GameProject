package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.domain.interactors.GameLoopInteractor;

@Module
public class GameLoopInteractorModule {

    @Provides
    @GameScope
    public GameLoopInteractor provideGameLoopInteractor(IDataRepository dataRepository) {
        return new GameLoopInteractor(dataRepository);
    }
}
