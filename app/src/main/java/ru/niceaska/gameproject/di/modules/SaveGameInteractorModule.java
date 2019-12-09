package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.domain.interactors.SaveGameInteractor;

@Module
public class SaveGameInteractorModule {

    @Provides
    @GameScope
    public SaveGameInteractor providesSaveGameInteractor(IDataRepository dataRepository) {
        return new SaveGameInteractor(dataRepository);
    }
}
