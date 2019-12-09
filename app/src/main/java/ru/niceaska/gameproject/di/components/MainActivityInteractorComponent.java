package ru.niceaska.gameproject.di.components;

import dagger.Component;
import ru.niceaska.gameproject.di.modules.MainActivityInteractorModule;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.interactors.MainActivityInteractor;

@Component(dependencies = AppComponent.class, modules = {MainActivityInteractorModule.class})
@GameScope
public interface MainActivityInteractorComponent {
    MainActivityInteractor getMainActivityInteractor();
}
