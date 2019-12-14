package ru.niceaska.gameproject.presentation.presenter;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import ru.niceaska.gameproject.domain.interactors.MainActivityInteractor;
import ru.niceaska.gameproject.presentation.view.IMainActivity;
import ru.niceaska.gameproject.rx.IRxSchedulers;

public class MainActivityPresenter {

    private WeakReference<IMainActivity> activityWeakReference;
    private MainActivityInteractor activityInteracator;
    private IRxSchedulers iRxSchedulers;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainActivityPresenter(MainActivityInteractor interacator, IRxSchedulers iRxSchedulers) {
        this.activityInteracator = interacator;
        this.iRxSchedulers = iRxSchedulers;
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
        compositeDisposable.add(activityInteracator.checkFirstStart().subscribeOn(iRxSchedulers.getIoScheduler())
                .observeOn(iRxSchedulers.getMainThreadScheduler())
                .doOnSubscribe(disposable -> planNotification())
                .subscribe(aBoolean -> {
                    if (activityWeakReference.get() != null) {
                        if (aBoolean) {
                            showGameStart();
                        } else {
                            activityWeakReference.get().startGame();
                        }
                    }
                }, throwable -> {
                    showGameStart();
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
                .subscribeOn(iRxSchedulers.getIoScheduler())
                .observeOn(iRxSchedulers.getMainThreadScheduler())
                .subscribe(this::gameRun)
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
