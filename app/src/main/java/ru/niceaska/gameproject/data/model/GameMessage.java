package ru.niceaska.gameproject.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "message")
public class GameMessage extends ListItem implements Parcelable {

    @Expose
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("message")
    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "is_gamer")
    private boolean isGamer;

    @ColumnInfo(name = "time_to_next")
    @SerializedName("timeToNext")
    private int timeToNext;

    @SerializedName("choices")
    @Embedded
    private Choices choices;

    public GameMessage(String message, long time, boolean isGamer, Choices choices) {
        this.message = message;
        this.time = time;
        this.isGamer = isGamer;
        this.choices = choices;
        this.timeToNext = 0;
    }

    @Ignore
    protected GameMessage(Parcel in) {
        message = in.readString();
        time = in.readLong();
        isGamer = in.readByte() != 0;
        timeToNext = in.readInt();
    }

    public static final Creator<GameMessage> CREATOR = new Creator<GameMessage>() {
        @Override
        public GameMessage createFromParcel(Parcel in) {
            return new GameMessage(in);
        }

        @Override
        public GameMessage[] newArray(int size) {
            return new GameMessage[size];
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

    public Choices getChoices() {
        return choices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChoices(Choices choices) {
        this.choices = choices;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameMessage that = (GameMessage) o;

        if (id != that.id) return false;
        if (time != that.time) return false;
        if (isGamer != that.isGamer) return false;
        if (timeToNext != that.timeToNext) return false;
        if (!message.equals(that.message)) return false;
        return choices != null ? choices.equals(that.choices) : that.choices == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + message.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (isGamer ? 1 : 0);
        result = 31 * result + timeToNext;
        result = 31 * result + (choices != null ? choices.hashCode() : 0);
        return result;
    }
}
