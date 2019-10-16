package ru.niceaska.gameproject;

public class Message {

    private String message;
    private long time;
    private boolean isGamer;
    private boolean isChoice;
    private int timeToNext;

    public Message(String message, long time, boolean isGamer, boolean isChoice) {
        this.message = message;
        this.time = time;
        this.isGamer = isGamer;
        this.isChoice = isChoice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isGamer() {
        return isGamer;
    }

    public void setGamer(boolean gamer) {
        isGamer = gamer;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }

    public int getTimeToNext() {
        return timeToNext;
    }

    public void setTimeToNext(int timeToNext) {
        this.timeToNext = timeToNext;
    }
}
