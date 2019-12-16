package ru.niceaska.gameproject.rx;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Реализация интерфейса для предоставления rx java шедулеров
 * @author Мохнаткина Алина
 */
public class RxSchedulersUtils implements IRxSchedulers {

    /**
     * Получить шедулер для работы на мейн треде
     *
     * @return шедулер
     */
    @Override
    public Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * Получить шедулер для операций ввода-вывода и работы с сетью
     * @return шедулер
     */
    @Override
    public Scheduler getIoScheduler() {
        return Schedulers.io();
    }
}
