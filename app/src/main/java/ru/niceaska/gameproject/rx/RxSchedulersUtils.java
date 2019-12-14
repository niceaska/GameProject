package ru.niceaska.gameproject.rx;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Мохнаткина Алина
 */
public class RxSchedulersUtils implements IRxSchedulers {
    @Override
    public Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler getIoScheduler() {
        return Schedulers.io();
    }
}
