package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.domain.interactors.FirstLoadDataInteractor;
import ru.niceaska.gameproject.presentation.presenter.StartAppPresenter;

@Module
public class FLMscreenModule {

    @ScreenScope
    @Provides
    public StartAppPresenter providesFirstLoadPresenter(FirstLoadDataInteractor interactor) {
        return new StartAppPresenter(interactor);
    }
}
