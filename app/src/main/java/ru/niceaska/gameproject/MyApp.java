package ru.niceaska.gameproject;

import android.app.Application;

import ru.niceaska.gameproject.di.components.AppComponent;
import ru.niceaska.gameproject.di.components.DaggerAppComponent;
import ru.niceaska.gameproject.di.modules.AppModule;
import ru.niceaska.gameproject.di.modules.DataModule;

public class MyApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
