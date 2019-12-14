package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.domain.interactors.MainActivityInteractor;
import ru.niceaska.gameproject.presentation.presenter.MainActivityPresenter;
import ru.niceaska.gameproject.rx.IRxSchedulers;

@Module
public class MainActivityModule {

    @Provides
    @ScreenScope
    public MainActivityPresenter providesMainActivityPresenter(MainActivityInteractor interacator,
                                                               IRxSchedulers iRxSchedulers) {
        return new MainActivityPresenter(interacator, iRxSchedulers);
    }

}
