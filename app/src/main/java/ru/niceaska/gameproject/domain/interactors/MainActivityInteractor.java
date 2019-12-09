package ru.niceaska.gameproject.domain.interactors;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.niceaska.gameproject.domain.IDataRepository;

public class MainActivityInteractor {

    private IDataRepository dataRepository;

    public MainActivityInteractor(IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Single<Boolean> checkFirstStart() {
        return dataRepository.checkFirstStart();
    }

    public boolean isNotificationOn() {
        return dataRepository.isNotificationEnabled();
    }

    public Completable refreshData() {
        return dataRepository.refreshDatabase();
    }
}
