package ru.niceaska.gameproject.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.niceaska.gameproject.di.scopes.ScreenScope;
import ru.niceaska.gameproject.rx.IRxSchedulers;
import ru.niceaska.gameproject.rx.RxSchedulersUtils;

/**
 * @author Мохнаткина Алина
 */
@Module
public class UtilsModule {
    @Provides
    @ScreenScope
    public IRxSchedulers getSchedulers() {
        return new RxSchedulersUtils();
    }

}
