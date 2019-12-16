package ru.niceaska.gameproject.domain.interactors;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.niceaska.gameproject.domain.IDataRepository;

/**
 * Интерактор для первоначальной загрузки данных
 */
public class FirstLoadDataInteractor {

    private IDataRepository dataRepository;

    /**
     * Конструкторр интерактора
     *
     * @param dataRepository репозиторий предоставляющий данные
     */
    public FirstLoadDataInteractor(@NonNull IDataRepository dataRepository) {
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

    /**
     * Проверяет существует ли уже таблица с сообщениями
     *
     * @return сингл булевого значения - существует ли таблица
     */
    public Single<Boolean> checkIfMessagesExist() {
        return dataRepository.checkIfMessagesExist();
    }
}

