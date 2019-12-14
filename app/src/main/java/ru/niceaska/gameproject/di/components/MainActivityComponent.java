package ru.niceaska.gameproject.di.components;

import dagger.Component;
import ru.niceaska.gameproject.di.modules.MainActivityModule;
import ru.niceaska.gameproject.di.modules.UtilsModule;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.presentation.view.activities.MainActivity;

@Component(dependencies = MainActivityInteractorComponent.class, modules = {MainActivityModule.class, UtilsModule.class})
@ScreenScope
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
