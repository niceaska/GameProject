package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.domain.interactors.FirstLoadDataInteractor;
import ru.niceaska.gameproject.presentation.presenter.StartAppPresenter;
import ru.niceaska.gameproject.rx.IRxSchedulers;

@Module
public class FLMscreenModule {

    @ScreenScope
    @Provides
    public StartAppPresenter providesFirstLoadPresenter(FirstLoadDataInteractor interactor, IRxSchedulers rxSchedulers) {
        return new StartAppPresenter(interactor, rxSchedulers);
    }
}
