package ru.niceaska.gameproject.domain.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import ru.niceaska.gameproject.data.model.ListItem;

/**
 * Модель домаин слоя для сообщения игры
 */
public class MessageItem implements ListItem {

    private int id;
    private String message;
    private boolean isGamer;
    private int nextMessage;
    private MessageChoices choices;

    /**
     * Конструктор модели
     *
     * @param id          айди сообщения
     * @param message     текст сообщения
     * @param isGamer     сообщения игрока или же игры
     * @param nextMessage айди следующего сообщения
     * @param choices     выбор (если есть)
     */
    public MessageItem(int id, String message, boolean isGamer,
                       int nextMessage, MessageChoices choices) {
        this.id = id;
        this.message = Preconditions.checkNotNull(message);
        this.isGamer = isGamer;
        this.nextMessage = nextMessage;
        this.choices = choices;
    }

    public String getMessage() {
        return message;
    }

    public boolean isGamer() {
        return isGamer;
    }

    public MessageChoices getChoices() {
        return choices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNextMessage() {
        return nextMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessageItem that = (MessageItem) o;
        return id == that.id &&
                isGamer == that.isGamer &&
                nextMessage == that.nextMessage &&
                Objects.equal(message, that.message) &&
                Objects.equal(choices, that.choices);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, message, isGamer, nextMessage, choices);
    }
}
