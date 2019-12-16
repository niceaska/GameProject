package ru.niceaska.gameproject.di.components;

import dagger.Component;
import ru.niceaska.gameproject.di.modules.FLMscreenModule;
import ru.niceaska.gameproject.di.modules.UtilsModule;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.presentation.view.fragments.GameStartFragment;

/**
 * Компонент старта приложения
 */
@Component(dependencies = FirstLoadComponent.class, modules = {FLMscreenModule.class, UtilsModule.class})
@ScreenScope
public interface FLMscreenComponent {
    void inject(GameStartFragment gameStartFragment);
}
