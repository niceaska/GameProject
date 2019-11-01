package ru.niceaska.gameproject.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;


@Entity(tableName = "history",
        foreignKeys = @ForeignKey(entity = UserPojo.class,
                parentColumns = "userId",
                childColumns = "user",
                onDelete = ForeignKey.CASCADE),
        primaryKeys = {"id", "user"})
public class HistoryMessage extends ListItem implements Parcelable {
    @NonNull
    @ColumnInfo(name = "id", index = true)
    private String id;
    @NonNull
    @ColumnInfo(name = "user", index = true)
    private String user;
    @ColumnInfo(name = "message")
    private String message;
    @ColumnInfo(name = "time")
    private long time;
    @ColumnInfo(name = "is_gamer")
    private boolean isGamer;
    @Ignore
    private long timeToNext;

    public HistoryMessage(String id, String user, String message, long time, boolean isGamer) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.time = time;
        this.isGamer = isGamer;
        this.timeToNext = 0;
    }

    protected HistoryMessage(Parcel in) {
        id = in.readString();
        user = in.readString();
        message = in.readString();
        time = in.readLong();
        isGamer = in.readByte() != 0;
        timeToNext = in.readLong();
    }

    public static final Creator<HistoryMessage> CREATOR = new Creator<HistoryMessage>() {
        @Override
        public HistoryMessage createFromParcel(Parcel in) {
            return new HistoryMessage(in);
        }

        @Override
        public HistoryMessage[] newArray(int size) {
            return new HistoryMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(message);
        dest.writeLong(time);
        dest.writeByte((byte) (isGamer ? 1 : 0));
        dest.writeLong(timeToNext);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public long getTimeToNext() {
        return timeToNext;
    }

    public void setTimeToNext(long timeToNext) {
        this.timeToNext = timeToNext;
    }
}
