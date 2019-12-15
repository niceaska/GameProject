package ru.niceaska.gameproject.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;

import static ru.niceaska.gameproject.data.repository.DataRepository.USER_ID;

/**
 * Конвертер из домаин слоя в дата
 */
public class SaveMessageConverter {

    /**
     * Конвертирует из списка элементов для отображения в историю сообщений
     *
     * @param listItems спиок элементов ресайклера
     * @return список - историю соообщений
     */
    public List<HistoryMessage> convertToHistory(List<ListItem> listItems) {
        List<HistoryMessage> historyMessagesList = new ArrayList<>();
        for (int i = 0; i < listItems.size(); i++) {
            ListItem listItem = listItems.get(i);
            if (!(listItem instanceof MessageChoices)) {
                MessageItem messageItem = (MessageItem) listItem;
                Choices choices = null;
                MessageChoices messageChoices = messageItem.getChoices();
                if (messageChoices != null) {
                    choices = new Choices(
                            messageChoices.getPositiveChoiceLabel(),
                            messageChoices.getNegativeChoiceLabel(),
                            messageChoices.getPositiveChoice(),
                            messageChoices.getNegativeChoice(),
                            messageChoices.getPositiveMessageAnswer(),
                            messageChoices.getNegativeMessageAnswer()
                    );
                }
                historyMessagesList.add(
                        new HistoryMessage(
                                String.valueOf(i),
                                USER_ID,
                                messageItem.getMessage(),
                                messageItem.isGamer(),
                                messageItem.getNextMessage(),
                                choices
                        )
                );
            }
        }
        return historyMessagesList;
    }
}
