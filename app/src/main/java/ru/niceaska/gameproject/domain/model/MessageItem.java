package ru.niceaska.gameproject.domain.model;

import ru.niceaska.gameproject.data.model.ListItem;

public class MessageItem extends ListItem {

    private int id;
    private String message;
    private boolean isGamer;
    private int nextMessage;
    private MessageChoices choices;


    public MessageItem(int id, String message, boolean isGamer,
                       int nextMessage, MessageChoices choices) {
        this.id = id;
        this.message = message;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageItem that = (MessageItem) o;

        if (id != that.id) return false;
        if (isGamer != that.isGamer) return false;
        if (nextMessage != that.nextMessage) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return choices != null ? choices.equals(that.choices) : that.choices == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (isGamer ? 1 : 0);
        result = 31 * result + nextMessage;
        result = 31 * result + (choices != null ? choices.hashCode() : 0);
        return result;
    }
}
