package ru.niceaska.gameproject.domain;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class FirstLoadDataInteractor {

    private IDataRepository dataRepository;

    public FirstLoadDataInteractor(IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Observable<List> firstLoadData() {
        return dataRepository.firstLoadData();
    }

    public Completable createUser() {
        return dataRepository.createUser();
    }

    public void closeFile() {
        dataRepository.closeFile();
    }
}

