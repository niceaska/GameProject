package ru.niceaska.gameproject.data.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;


@Entity(tableName = "history",
        foreignKeys = @ForeignKey(entity = UserPojo.class,
                parentColumns = "userId",
                childColumns = "user",
                onDelete = ForeignKey.CASCADE),
        primaryKeys = {"id", "user"})
public class HistoryMessage extends MessageItem {
    @NonNull
    @ColumnInfo(name = "id", index = true)
    private String id;
    @NonNull
    @ColumnInfo(name = "user", index = true)
    private String user;
    @ColumnInfo(name = "message")
    private String message;
    @ColumnInfo(name = "is_gamer")
    private boolean isGamer;
    @ColumnInfo(name = "next_message")
    private int nextMessage;
    @Embedded
    private Choices choices;


    public HistoryMessage(@NonNull String id, @NonNull String user, String message,
                          boolean isGamer, int nextMessage, Choices choices) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.isGamer = isGamer;
        this.nextMessage = nextMessage;
        this.choices = choices;
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


    public boolean isGamer() {
        return isGamer;
    }

    public void setGamer(boolean gamer) {
        isGamer = gamer;
    }

    public Choices getChoices() {
        return choices;
    }

    public int getNextMessage() {
        return nextMessage;
    }

    public void setNextMessage(int nextMessage) {
        this.nextMessage = nextMessage;
    }

    public void setChoices(Choices choices) {
        this.choices = choices;
    }
}
