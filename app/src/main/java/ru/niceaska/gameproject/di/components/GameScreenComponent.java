package ru.niceaska.gameproject.di.components;

import dagger.Component;
import ru.niceaska.gameproject.di.modules.GameScreenModule;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.presentation.view.MessageListFragment;

@Component(dependencies = GameComponent.class, modules = {GameScreenModule.class})
@ScreenScope
public interface GameScreenComponent {
    void inject(MessageListFragment messageListFragment);
}
