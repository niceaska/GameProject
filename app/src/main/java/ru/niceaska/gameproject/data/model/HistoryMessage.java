package ru.niceaska.gameproject.data.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.google.common.base.Objects;

/**
 * Класс моделька сообщений истории привязанная к игроку
 */
@Entity(tableName = "history",
        foreignKeys = @ForeignKey(entity = UserPojo.class,
                parentColumns = "userId",
                childColumns = "user",
                onDelete = ForeignKey.CASCADE),
        primaryKeys = {"id", "user"})
public class HistoryMessage {
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

    /**
     * Конструктор модели
     *
     * @param id          айди сообщения
     * @param user        айди игрока
     * @param message     текст сообщения
     * @param isGamer     от кого сообщение
     * @param nextMessage следующее сообщение
     * @param choices     выбор если есть
     */
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

    public String getMessage() {
        return message;
    }

    public boolean isGamer() {
        return isGamer;
    }

    public Choices getChoices() {
        return choices;
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
        HistoryMessage that = (HistoryMessage) o;
        return isGamer == that.isGamer &&
                nextMessage == that.nextMessage &&
                Objects.equal(id, that.id) &&
                Objects.equal(user, that.user) &&
                Objects.equal(message, that.message) &&
                Objects.equal(choices, that.choices);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, user, message, isGamer, nextMessage, choices);
    }
}
