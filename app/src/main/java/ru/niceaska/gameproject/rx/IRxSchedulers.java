package ru.niceaska.gameproject.rx;

import io.reactivex.Scheduler;

/**
 * @author Мохнаткина Алина
 */
public interface IRxSchedulers {

    Scheduler getMainThreadScheduler();

    Scheduler getIoScheduler();
}
