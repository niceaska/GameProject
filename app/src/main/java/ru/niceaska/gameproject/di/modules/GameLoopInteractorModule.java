package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.GameLoopInteractor;
import ru.niceaska.gameproject.domain.IDataRepository;

@Module
public class GameLoopInteractorModule {

    @Provides
    @GameScope
    public GameLoopInteractor provideGameLoopInteractor(IDataRepository dataRepository) {
        return new GameLoopInteractor(dataRepository);
    }
}
