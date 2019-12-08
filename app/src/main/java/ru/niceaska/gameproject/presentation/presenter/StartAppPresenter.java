package ru.niceaska.gameproject.presentation.presenter;

import android.util.Log;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.domain.FirstLoadDataInteractor;
import ru.niceaska.gameproject.presentation.view.GameStartFragment;

public class StartAppPresenter {
    private WeakReference<GameStartFragment> gameStartFragmentWeakReference;
    private FirstLoadDataInteractor firstLoadDataInteractor;
    private CompositeDisposable compositeDisposable;

    private static final String TAG = StartAppPresenter.class.getName();

    public StartAppPresenter(FirstLoadDataInteractor interactor) {
        this.firstLoadDataInteractor = interactor;
        this.compositeDisposable = new CompositeDisposable();
    }

    public void attachView(GameStartFragment gameStartFragment) {
        this.gameStartFragmentWeakReference = new WeakReference<>(gameStartFragment);
    }

    public void createUser() {
        compositeDisposable.add(
                firstLoadDataInteractor.createUser().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }

    public void loadData() {
        compositeDisposable.add(firstLoadDataInteractor.firstLoadData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> showError())
                .doOnComplete(() -> firstLoadDataInteractor.closeFile())
                .subscribe(o -> {
                        },
                        throwable -> {
                            Log.d(TAG, "loadData: " + throwable.getMessage());
                        },
                        () -> {
                            createUser();
                            if (gameStartFragmentWeakReference.get() != null) {
                                gameStartFragmentWeakReference.get().beginNewGame();
                            }
                        }));
    }

    private void showError() {
        if (gameStartFragmentWeakReference.get() != null) {
            gameStartFragmentWeakReference.get().showErrorToast();
        }
    }

    public void unSubscribe() {
        compositeDisposable.clear();
    }

    public void detachView() {
        gameStartFragmentWeakReference.clear();
    }
}
