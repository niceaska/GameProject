package ru.niceaska.gameproject.presentation.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ru.niceaska.gameproject.MyApp;
import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.di.components.AppComponent;
import ru.niceaska.gameproject.di.components.DaggerMainActivityComponent;
import ru.niceaska.gameproject.di.components.DaggerMainActivityInteractorComponent;
import ru.niceaska.gameproject.di.components.MainActivityComponent;
import ru.niceaska.gameproject.di.components.MainActivityInteractorComponent;
import ru.niceaska.gameproject.presentation.presenter.MainActivityPresenter;
import ru.niceaska.gameproject.presentation.view.IMainActivity;
import ru.niceaska.gameproject.presentation.view.NotificationWorker;
import ru.niceaska.gameproject.presentation.view.fragments.GamePreferenceFragment;
import ru.niceaska.gameproject.presentation.view.fragments.GameStartFragment;
import ru.niceaska.gameproject.presentation.view.fragments.MessageListFragment;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private final static int NOTIFICATION_DELAY = 1;

    @Inject
    MainActivityPresenter mainActivityPresenter;

    private MainActivityInteractorComponent mainActivityInteractorComponent;
    private AppComponent appComponent;
    private MainActivityComponent mainComponent;
    private WorkManager workManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAppComponent();
        injectDependencies();
        mainActivityPresenter.attachView(this);
        if (savedInstanceState == null) {
            mainActivityPresenter.gameRun();
        }

    }

    private void injectDependencies() {
        mainActivityInteractorComponent = DaggerMainActivityInteractorComponent.builder()
                .appComponent(appComponent)
                .build();
        mainComponent = DaggerMainActivityComponent.builder()
                .mainActivityInteractorComponent(mainActivityInteractorComponent)
                .build();
        mainComponent.inject(this);
    }

    private void getAppComponent() {
        Context applicationContext = getApplicationContext();
        if (applicationContext instanceof MyApp) {
            appComponent = ((MyApp) applicationContext).getAppComponent();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restart:
                mainActivityPresenter.restartGame();
                break;
            case R.id.show_settings:
                showSettings();
                break;
            case R.id.exit_game:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void showSettings() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.list_fragment, new GamePreferenceFragment())
                    .commit();
        }
    }

    @Override
    public void startGame() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment, MessageListFragment.newInstance())
                .commit();
    }

    /**
     * Запланировать уведомления прм долгом незапуске игры
     */
    @Override
    public void planningNotification() {
        clearNotifications();
        Data workerData = new Data.Builder()
                .putString(NotificationWorker.PLAYER_AWAY_TITLE, getResources().getString(R.string.notification_title))
                .putString(NotificationWorker.PLAYER_AWAY_TEXT, getResources().getString(R.string.help_hero))
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(workerData)
                .setInitialDelay(NOTIFICATION_DELAY, TimeUnit.DAYS)
                .build();
        workManager.enqueue(workRequest);
    }

    /**
     * Удалить запланированные уведомления
     */
    @Override
    public void clearNotifications() {
        workManager = WorkManager.getInstance(MainActivity.this);
        workManager.cancelAllWork();
    }

    /**
     * Показать фрагмент старта игры
     */
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
        mainComponent = null;
        mainActivityInteractorComponent = null;
    }
}