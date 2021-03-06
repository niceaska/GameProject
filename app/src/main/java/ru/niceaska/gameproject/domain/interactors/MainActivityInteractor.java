package ru.niceaska.gameproject.domain.interactors;

import androidx.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.niceaska.gameproject.domain.IDataRepository;

/**
 * Класс интерактор активити игры
 */
public class MainActivityInteractor {

    private IDataRepository dataRepository;

    /**
     * Конструкторр интерактора
     *
     * @param dataRepository репозиторий предоставляющий данные
     */
    public MainActivityInteractor(@NonNull IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * Проверка на то первый ли старт приложения
     *
     * @return Single булевго значения - первый ли старт
     */
    public Single<Boolean> checkFirstStart() {
        return dataRepository.checkFirstStart();
    }

    /**
     * Проверяет включены ли уведомления
     * @return булевое значение - true включены, false нет
     */
    public boolean isNotificationOn() {
        return dataRepository.isNotificationEnabled();
    }

    /**
     * Рефрешит прогресс при новом старте игры
     * @return Completable действия
     */
    public Completable refreshUserData() {
        return dataRepository.refreshUserProgress();
    }

}
