package ru.niceaska.gameproject.presentation.presenter;

import androidx.room.EmptyResultSetException;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.presentation.view.IMainActivity;

public class MainActivityPresenter {

    private WeakReference<IMainActivity> activityWeakReference;
    private IDataRepository dataRepository;
    private Disposable disposable;

    public MainActivityPresenter(IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public void attachView(IMainActivity activity) {
        this.activityWeakReference = new WeakReference<IMainActivity>(activity);
    }

    public void gameRun() {
        planNotification();
        disposable = dataRepository.checkFirstStart(DataRepository.USER_ID).subscribeOn(Schedulers.io())
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
                        if (activityWeakReference.get() != null) {
                            activityWeakReference.get().showStartAppFragment();
                        }
                    }
                });
    }

    private void planNotification() {
        IMainActivity activity = activityWeakReference.get();
        if (activity != null) {
            activity.planningNotification();
        }
    }

    public void clearDisposable() {
        disposable.dispose();
    }

    public void detachView() {
        activityWeakReference.clear();
    }
}
