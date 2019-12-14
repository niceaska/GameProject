package ru.niceaska.gameproject.di.components;

import dagger.Component;
import ru.niceaska.gameproject.di.modules.GameScreenModule;
import ru.niceaska.gameproject.di.modules.UtilsModule;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.presentation.view.fragments.MessageListFragment;

@Component(dependencies = GameComponent.class, modules = {GameScreenModule.class, UtilsModule.class})
@ScreenScope
public interface GameScreenComponent {
    void inject(MessageListFragment messageListFragment);
}
