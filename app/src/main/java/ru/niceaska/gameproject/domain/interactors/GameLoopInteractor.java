package ru.niceaska.gameproject.domain.interactors;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;

/**
 * Класс интерактор игровой логики
 */
public class GameLoopInteractor {

    private IDataRepository dataRepository;

    public GameLoopInteractor(@NonNull IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * Получить следующий индекс для загрузки
     *
     * @param listItems список объектов для отображения
     * @return индекс следующего сообщения
     */
    public int getNextIndex(@NonNull List<ListItem> listItems) {
        int nextIndex = 1;
        if (!listItems.isEmpty() && listItems.get(listItems.size() - 1) instanceof MessageItem) {
            nextIndex = ((MessageItem) listItems.get(listItems.size() - 1)).getNextMessage();
        }
        return nextIndex;
    }

    /**
     * Загружает следующее сообщение и формирует спиок для отображения
     * @param nextIndex индекс сообщения для загрузки
     * @param listItems список объектов для отображения
     * @return список для отображения
     */
    public Single<List<ListItem>> loadNewMessage(int nextIndex, @NonNull List<ListItem> listItems) {
        return dataRepository.loadNewGameMessage(nextIndex, listItems);
    }

    /**
     * Обновить список сообщений при выборе одного из вариантов ответа
     * @param messageChoices варианты ответа
     * @param listItems список объектов для отображения
     * @param isNegative маркер отрицательного ответа
     * @return список объектов для отображения
     */
    public List<ListItem> updateMessageList(@NonNull MessageChoices messageChoices,
                                            @NonNull List<ListItem> listItems,
                                            boolean isNegative) {
        final List<ListItem> newList = new ArrayList<ListItem>(listItems);
        int answer = isNegative ? messageChoices.getNegativeMessageAnswer() : messageChoices.getPositiveMessageAnswer();
        newList.set(newList.size() - 1,
                new MessageItem(newList.size() - 1,
                        isNegative ? messageChoices.getNegativeChoice() : messageChoices.getPositiveChoice(),
                        true, answer, null));
        return newList;
    }
}
