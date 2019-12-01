package ru.niceaska.gameproject.domain;

import java.io.Reader;

import io.reactivex.Observable;

public class FirstLoadDataInteractor {

    private IDataRepository dataRepository;

    public FirstLoadDataInteractor(IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Observable<Object> firstLoadData(Reader open) {
        return dataRepository.firstLoadData(open);
    }
}

