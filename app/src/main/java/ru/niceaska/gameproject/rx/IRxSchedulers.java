package ru.niceaska.gameproject.rx;

import io.reactivex.Scheduler;

/**
 * Интерфейс для предоставления rxjava шедулеров
 * @author Мохнаткина Алина
 */
public interface IRxSchedulers {

    /**
     * Получить шедулер для работы на мейн треде
     *
     * @return шедулер
     */
    Scheduler getMainThreadScheduler();

    /**
     * Получить шедулер для операций ввода-вывода и работы с сетью
     * @return шедулер
     */
    Scheduler getIoScheduler();
}
