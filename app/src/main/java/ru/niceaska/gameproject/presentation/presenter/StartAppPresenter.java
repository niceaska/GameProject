package ru.niceaska.gameproject.presentation.presenter;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import ru.niceaska.gameproject.domain.interactors.FirstLoadDataInteractor;
import ru.niceaska.gameproject.presentation.view.GameStartView;
import ru.niceaska.gameproject.rx.IRxSchedulers;

public class StartAppPresenter {

    private WeakReference<GameStartView> gameStartViewWeakReference;
    private FirstLoadDataInteractor firstLoadDataInteractor;
    private CompositeDisposable compositeDisposable;
    private IRxSchedulers rxSchedulers;

    private static final String TAG = StartAppPresenter.class.getName();

    public StartAppPresenter(@NonNull FirstLoadDataInteractor interactor, @NonNull IRxSchedulers rxSchedulers) {
        this.firstLoadDataInteractor = interactor;
        this.rxSchedulers = rxSchedulers;
        this.compositeDisposable = new CompositeDisposable();
    }

    /**
     * Аттачит вью к презентеру
     *
     * @param view вью для презентера
     */
    public void attachView(@NonNull GameStartView view) {
        this.gameStartViewWeakReference = new WeakReference<>(view);
    }

    /**
     * Создает нового юзера для хранения истории и прогресса
     */
    private void createUser() {
        compositeDisposable.add(
                firstLoadDataInteractor.createUser().subscribeOn(rxSchedulers.getIoScheduler())
                        .observeOn(rxSchedulers.getMainThreadScheduler())
                        .subscribe()
        );
    }

    /**
     * Загружает и парсит данные из файла сценария
     */
    public void loadData() {
        compositeDisposable.add(firstLoadDataInteractor.firstLoadData().subscribeOn(rxSchedulers.getIoScheduler())
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .doOnError(throwable -> showError())
                .subscribe(o -> {
                        },
                        throwable -> {
                        },
                        () -> {
                            createUser();
                            GameStartView gameStartView = gameStartViewWeakReference.get();
                            if (gameStartView != null) {
                                gameStartView.beginNewGame();
                            }
                        }));
    }

    private void showError() {
        GameStartView gameStartView = gameStartViewWeakReference.get();
        if (gameStartView != null) {
            gameStartView.showErrorToast();
        }
    }

    public void unSubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    /**
     * Деаттачит вью от презентера
     */
    public void detachView() {
        gameStartViewWeakReference.clear();
    }
}
