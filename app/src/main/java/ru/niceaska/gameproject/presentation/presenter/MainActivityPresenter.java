package ru.niceaska.gameproject.presentation.presenter;

import androidx.room.EmptyResultSetException;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.view.IMainActivity;

public class MainActivityPresenter {

    private WeakReference<IMainActivity> activityWeakReference;
    private DataRepository dataRepository;

    public MainActivityPresenter(IMainActivity activity, DataRepository dataRepository) {
        this.activityWeakReference = new WeakReference<IMainActivity>(activity);
        this.dataRepository = dataRepository;
    }

    public void gameRun() {
        Disposable disposable = dataRepository.checkFirstStart("1").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        activityWeakReference.get().showStartAppFragment();
                    } else {
                        activityWeakReference.get().startGame();
                    }
                }, throwable -> {
                    if (throwable instanceof EmptyResultSetException) {
                        activityWeakReference.get().showStartAppFragment();
                    }
                });
    }
}
