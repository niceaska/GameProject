package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.presentation.presenter.MainActivityPresenter;

@Module
public class MainActivityModule {

    @Provides
    @ScreenScope
    public MainActivityPresenter providesMainActivityPresenter(IDataRepository dataRepository) {
        return new MainActivityPresenter(dataRepository);
    }

}
