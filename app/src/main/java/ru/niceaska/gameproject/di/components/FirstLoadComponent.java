package ru.niceaska.gameproject.di.components;

import dagger.Component;
import ru.niceaska.gameproject.di.modules.FirstLoadModule;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.interactors.FirstLoadDataInteractor;

/**
 * Компонент интерактора загрузки данных
 */
@Component(dependencies = AppComponent.class, modules = {FirstLoadModule.class})
@GameScope
public interface FirstLoadComponent {
    FirstLoadDataInteractor firstLoadDtatInteractor();
}
