package ru.niceaska.gameproject.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;

/**
 * Конвертер для domain слоя
 */
public class MessageConverter {

    /**
     * Конвертирует из истории в список для отображения
     *
     * @param historyMessageList спиок - история сообщений
     * @return спиок сообщений для отображения
     */
    public List<MessageItem> convertFromHistory(List<HistoryMessage> historyMessageList) {
        int i = 0;
        List<MessageItem> messageItems = new ArrayList<>();
        for (HistoryMessage message : historyMessageList) {
            Choices choices = message.getChoices();
            MessageChoices messageChoices = null;
            if (choices != null) {
                messageChoices = new MessageChoices(
                        choices.getPositiveChoiceLabel(),
                        choices.getNegativeChoiceLabel(),
                        choices.getPositiveChoice(),
                        choices.getNegativeChoice(),
                        choices.getPositiveMessageAnswer(),
                        choices.getNegativeMessageAnswer());
            }
            messageItems.add(new MessageItem(
                    ++i,
                    message.getMessage(),
                    message.isGamer(),
                    message.getNextMessage(),
                    messageChoices
            ));
        }
        return messageItems;
    }

    /**
     * Конвертирует игровое сообщение - сущность дата слоя в сообщение для отображения
     * @param gameMessage игшровое сообщение
     * @return сообщение для отображения
     */
    public MessageItem convertFromGameMessage(GameMessage gameMessage) {
        Choices choices = gameMessage.getChoices();
        MessageChoices messageChoices = null;
        if (choices != null) {
            messageChoices = new MessageChoices(
                    choices.getPositiveChoiceLabel(),
                    choices.getNegativeChoiceLabel(),
                    choices.getPositiveChoice(),
                    choices.getNegativeChoice(),
                    choices.getPositiveMessageAnswer(),
                    choices.getNegativeMessageAnswer());
        }
        MessageItem messageItem = new MessageItem(
                gameMessage.getId(),
                gameMessage.getMessage(),
                gameMessage.isGamer(),
                gameMessage.getNextMessage(),
                messageChoices
        );
        return messageItem;
    }

}
