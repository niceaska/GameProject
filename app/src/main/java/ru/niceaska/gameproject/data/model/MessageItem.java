package ru.niceaska.gameproject.data.model;

public abstract class MessageItem extends ListItem {

    public abstract String getMessage();

    public abstract void setMessage(String message);

    public abstract long getTime();

    public abstract void setTime(long time);

    public abstract boolean isGamer();

    public abstract void setGamer(boolean gamer);

    public abstract int getTimeToNext();

    public abstract void setTimeToNext(int timeToNext);
}
