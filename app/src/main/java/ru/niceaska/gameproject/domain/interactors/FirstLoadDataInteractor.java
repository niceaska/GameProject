package ru.niceaska.gameproject.domain.interactors;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import ru.niceaska.gameproject.domain.IDataRepository;

/**
 * Интерактор для первоначальной загрузки данных
 */
public class FirstLoadDataInteractor {

    private IDataRepository dataRepository;

    public FirstLoadDataInteractor(IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * Загружает и парсит данные из файла сценария в базу данных
     *
     * @return Observable
     */
    public Observable<List> firstLoadData() {
        return dataRepository.firstLoadData();
    }

    /**
     * Создает юзера
     * @return Completable
     */
    public Completable createUser() {
        return dataRepository.createUser();
    }

}

