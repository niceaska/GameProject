package ru.niceaska.gameproject.domain;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.model.MessageItem;

/**
 * Интерфейс репозитория для предоставления данных
 */
public interface IDataRepository {

    /**
     * Сохранить информацию о прогрессе игрока
     *
     * @param lastIndex    прогресс игрока
     * @param messageItems сообщения которые будут добавлены в историю
     * @return Completable совершенного действия
     */
    Completable saveUserData(int lastIndex, List<ListItem> messageItems);

    /**
     * Загружает следующее сообщение и формирует список для отображения
     *
     * @param nextIndex индекс следующего сообщения
     * @param listItems список сообщений
     * @return сингл совершенного действия
     */
    Single<List<ListItem>> loadNewGameMessage(int nextIndex, List<ListItem> listItems);

    /**
     * Загружает прогресс игрока
     *
     * @param userId ид игрока
     * @return сингл совершенного действия
     */
    Single<Integer> loadUserProgress(String userId);

    /**
     * Проверяет первый ли запуск приложения
     *
     * @return сингл проверки
     */
    Single<Boolean> checkFirstStart();

    /**
     * Загружает сценарий из ассетов и заполняет базу данных
     *
     * @return Observable
     */
    Observable<List> firstLoadData();

    /**
     * Загружает историю сообщений игрока
     *
     * @param userId ид игрока
     * @return сингл
     */
    Single<List<MessageItem>> loadHistory(String userId);

    /**
     * Создает пользователя и загружает его в дб
     *
     * @return Completable
     */
    Completable createUser();

    /**
     * Удаляет прогресс игрока при начале новой игры
     *
     * @return Completable
     */
    Completable refreshUserProgress();

    /**
     * Проверяет включены ли уведомления
     *
     * @return булевое значение - true включены, false нет
     */
    boolean isNotificationEnabled();

    /**
     * Проверяет включена ли анимация для сообщений
     *
     * @return булевое значение - true включена, false нет
     */
    boolean isMessageAnimationEnabled();

    /**
     * Проверяет загружены ли уже сообщения в базу данных
     *
     * @return сингл булевого значения - true загружены false нет
     */
    Single<Boolean> checkIfMessagesExist();
}
