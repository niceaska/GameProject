package ru.niceaska.gameproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Message extends ListItem implements Parcelable {

    private String message;
    private long time;
    private boolean isGamer;
    private int timeToNext;

    public Message(String message, long time, boolean isGamer) {
        this.message = message;
        this.time = time;
        this.isGamer = isGamer;
        this.timeToNext = 0;
    }

    protected Message(Parcel in) {
        message = in.readString();
        time = in.readLong();
        isGamer = in.readByte() != 0;
        timeToNext = in.readInt();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

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

    public int getTimeToNext() {
        return timeToNext;
    }

    public void setTimeToNext(int timeToNext) {
        this.timeToNext = timeToNext;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeLong(time);
        dest.writeByte((byte) (isGamer ? 1 : 0));
        dest.writeInt(timeToNext);
    }
}
