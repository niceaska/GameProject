package ru.niceaska.gameproject.domain;

import java.io.Reader;

import io.reactivex.Observable;
import ru.niceaska.gameproject.data.repository.DataRepository;

public class FirstLoadDataInteractor {

    private DataRepository dataRepository;

    public FirstLoadDataInteractor(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Observable<Object> firstLoadData(Reader open) {
        return dataRepository.firstLoadData(open);
    }
}

