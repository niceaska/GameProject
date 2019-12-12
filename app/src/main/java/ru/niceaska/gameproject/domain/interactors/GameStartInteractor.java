package ru.niceaska.gameproject.domain.interactors;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.domain.model.MessageItem;

import static ru.niceaska.gameproject.data.repository.DataRepository.USER_ID;

/**
 * Класс интерактор старта игры
 */
public class GameStartInteractor {

    private IDataRepository dataRepository;

    public GameStartInteractor(IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * Загружает историю сообщений
     *
     * @return Single списка элементов для отображения
     */
    public Single<List<ListItem>> loadHistory() {
        return dataRepository.loadHistory(USER_ID)
                .map(historyMessages -> {
                    List<ListItem> messageList = new ArrayList<>(historyMessages);
                    if (!historyMessages.isEmpty()) {
                        ListItem lastItem = messageList.get(messageList.size() - 1);
                        if (((MessageItem) lastItem).getChoices() != null) {
                            messageList.add(((MessageItem) lastItem).getChoices());
                        }
                    }
                    return messageList;
                });
    }

    /**
     * Загружает прогресс пользователя
     * @return Single прогресса пользователя
     */
    public Single<Integer> loadUserProgress() {
        return dataRepository.loadUserProgress(USER_ID);
    }

    /**
     * Проверяет включена ли анимация появления сообщений
     *
     * @return булевое значение true - включена, false - выключена
     */
    public boolean isMessageAnimationEnabled() {
        return dataRepository.isMessageAnimationEnabled();
    }

}
