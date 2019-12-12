package ru.niceaska.gameproject.presentation.presenter;

import android.util.Log;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.domain.interactors.FirstLoadDataInteractor;
import ru.niceaska.gameproject.presentation.view.GameStartView;

public class StartAppPresenter {

    private WeakReference<GameStartView> gameStartFragmentWeakReference;
    private FirstLoadDataInteractor firstLoadDataInteractor;
    private CompositeDisposable compositeDisposable;

    private static final String TAG = StartAppPresenter.class.getName();

    public StartAppPresenter(FirstLoadDataInteractor interactor) {
        this.firstLoadDataInteractor = interactor;
        this.compositeDisposable = new CompositeDisposable();
    }

    /**
     * Аттачит вью к презентеру
     *
     * @param gameStartFragment вью для презентера
     */
    public void attachView(GameStartView gameStartFragment) {
        this.gameStartFragmentWeakReference = new WeakReference<>(gameStartFragment);
    }

    /**
     * Создает нового юзера для хранения истории и прогресса
     */
    private void createUser() {
        compositeDisposable.add(
                firstLoadDataInteractor.createUser().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }

    /**
     * Загружает и парсит данные из файла сценария
     */
    public void loadData() {
        compositeDisposable.add(firstLoadDataInteractor.firstLoadData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> showError())
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

    /**
     * Деаттачит вью от презентера
     */
    public void detachView() {
        gameStartFragmentWeakReference.clear();
    }
}
