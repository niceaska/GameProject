package ru.niceaska.gameproject.di.components;

import dagger.Component;
import ru.niceaska.gameproject.di.modules.MainActivityModule;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.presentation.presenter.MainActivityPresenter;

@Component(dependencies = AppComponent.class, modules = {MainActivityModule.class})
@ScreenScope
public interface MainActivityComponent {
    MainActivityPresenter getMainActivityPresenter();
}
