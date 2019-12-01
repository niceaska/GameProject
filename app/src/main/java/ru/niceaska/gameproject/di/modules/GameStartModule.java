package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.GameStartInteractor;
import ru.niceaska.gameproject.domain.IDataRepository;

@Module
public class GameStartModule {

    @Provides
    @GameScope
    public GameStartInteractor provideGameStartInteractor(IDataRepository dataRepository) {
        return new GameStartInteractor(dataRepository);
    }
}
