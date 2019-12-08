package ru.niceaska.gameproject.presentation.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import ru.niceaska.gameproject.MyApp;
import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.di.components.DaggerMainActivityComponent;
import ru.niceaska.gameproject.di.components.MainActivityComponent;
import ru.niceaska.gameproject.di.modules.MainActivityModule;
import ru.niceaska.gameproject.presentation.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private final static int NOTIFICATION_DELAY = 1;

    MainActivityPresenter mainActivityPresenter;

    private MainActivityComponent mainActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityComponent = DaggerMainActivityComponent.builder()
                .appComponent(MyApp.getInstance().getAppComponent())
                .mainActivityModule(new MainActivityModule())
                .build();
        mainActivityPresenter = mainActivityComponent.getMainActivityPresenter();
        if (savedInstanceState == null) {
            mainActivityPresenter.attachView(this);
            mainActivityPresenter.gameRun();
        }

    }

    @Override
    public void startGame() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment, MessageListFragment.newInstance())
                .commit();
    }

    @Override
    public void planningNotification() {
        WorkManager workManager = WorkManager.getInstance(MainActivity.this);
        workManager.cancelAllWork();
        Data workerData = new Data.Builder()
                .putString(NotificationWorker.PLAYER_AWAY_TITLE, getResources().getString(R.string.notification_title))
                .putString(NotificationWorker.PLAYER_AWAY_TEXT, getResources().getString(R.string.help_hero))
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(workerData)
                .setInitialDelay(NOTIFICATION_DELAY, TimeUnit.HOURS)
                .build();
        workManager.enqueue(workRequest);
    }

    @Override
    public void showStartAppFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment, new GameStartFragment())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityPresenter.clearDisposable();
        mainActivityPresenter.detachView();
        mainActivityComponent = null;

    }
}