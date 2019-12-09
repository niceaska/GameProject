package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.domain.interactors.GameLoopInteractor;
import ru.niceaska.gameproject.domain.interactors.GameStartInteractor;
import ru.niceaska.gameproject.domain.interactors.SaveGameInteractor;
import ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter;

@Module
public class GameScreenModule {
    @Provides
    @ScreenScope
    ListFragmentPresenter provideListFragmentPresenter(GameStartInteractor gameStartInteractor,
                                                       GameLoopInteractor gameLoopInteractor,
                                                       SaveGameInteractor saveGameInteractor) {
        return new ListFragmentPresenter(gameStartInteractor, gameLoopInteractor, saveGameInteractor);
    }
}
