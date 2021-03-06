package ru.niceaska.gameproject.di.components;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import ru.niceaska.gameproject.di.modules.AppModule;
import ru.niceaska.gameproject.di.modules.DataModule;
import ru.niceaska.gameproject.domain.IDataRepository;

/**
 * Основной компонент приложения
 */
@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {

    SharedPreferences getSharedPreferences();

    IDataRepository getiDataRepository();
}
