package ru.niceaska.gameproject.presentation.presenter;

import androidx.room.EmptyResultSetException;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.domain.interactors.MainActivityInteractor;
import ru.niceaska.gameproject.presentation.view.IMainActivity;

public class MainActivityPresenter {

    private WeakReference<IMainActivity> activityWeakReference;
    private MainActivityInteractor activityInteracator;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainActivityPresenter(MainActivityInteractor interacator) {
        this.activityInteracator = interacator;
    }

    /**
     * Аттачит вью к презентеру
     *
     * @param activity вью для презентера
     */
    public void attachView(IMainActivity activity) {
        this.activityWeakReference = new WeakReference<IMainActivity>(activity);
    }

    /**
     * Проверяет первый ли запуск игры и запускает соответствующий фрагмент
     */
    public void gameRun() {
        planNotification();
        compositeDisposable.add(activityInteracator.checkFirstStart().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (activityWeakReference.get() != null) {
                        if (aBoolean) {
                            activityWeakReference.get().showStartAppFragment();
                        } else {
                            activityWeakReference.get().startGame();
                        }
                    }
                }, throwable -> {
                    if (throwable instanceof EmptyResultSetException) {
                        showGameStart();
                    }
                }));
    }

    private void showGameStart() {
        if (activityWeakReference.get() != null) {
            activityWeakReference.get().showStartAppFragment();
        }
    }

    /**
     * Запускает игру заново при выборе соответсвующего пункта меню
     */
    public void restartGame() {
        compositeDisposable.add(activityInteracator.refreshData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> gameRun())
        );
    }

    private void planNotification() {
        IMainActivity activity = activityWeakReference.get();
        if (activity != null) {
            activity.clearNotifications();
            if (activityInteracator.isNotificationOn()) {
                activity.planningNotification();
            }
        }
    }

    public void clearDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    /**
     * Деаттачит вью от презентера
     */
    public void detachView() {
        activityWeakReference.clear();
    }
}
