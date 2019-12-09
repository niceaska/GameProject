package ru.niceaska.gameproject.di.components;

import dagger.Component;
import ru.niceaska.gameproject.di.modules.GameLoopInteractorModule;
import ru.niceaska.gameproject.di.modules.GameStartModule;
import ru.niceaska.gameproject.di.modules.SaveGameInteractorModule;
import ru.niceaska.gameproject.di.scopes.GameScope;
import ru.niceaska.gameproject.domain.interactors.GameLoopInteractor;
import ru.niceaska.gameproject.domain.interactors.GameStartInteractor;
import ru.niceaska.gameproject.domain.interactors.SaveGameInteractor;

@Component(dependencies = AppComponent.class,
        modules = {GameLoopInteractorModule.class,
                GameStartModule.class,
                SaveGameInteractorModule.class})
@GameScope
public interface GameComponent {
    GameLoopInteractor gameLoopInteractor();

    GameStartInteractor gameStartInteractor();

    SaveGameInteractor saveGaneInteractor();
}