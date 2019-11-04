package ru.niceaska.gameproject.data.model;

public abstract class MessageItem extends ListItem {

    public abstract String getMessage();
    public abstract boolean isGamer();

    public abstract int getNextMessage();

}
