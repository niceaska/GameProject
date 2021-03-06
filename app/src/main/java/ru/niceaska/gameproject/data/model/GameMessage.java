package ru.niceaska.gameproject.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.common.base.Objects;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Моделька игрового сообщения для базы данных
 */
@Entity(tableName = "message")
public class GameMessage {

    @Expose
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("message")
    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "is_gamer")
    private boolean isGamer;

    @SerializedName("next")
    @ColumnInfo(name = "next_message")
    private int nextMessage;

    @SerializedName("choices")
    @Embedded
    private Choices choices;

    /**
     * Конструктор модели
     *
     * @param id          айди сообщения
     * @param message     текст сообщения
     * @param isGamer     сообщения игрока или же игры
     * @param nextMessage айди следующего сообщения
     * @param choices     выбор (если есть)
     */
    public GameMessage(int id,
                       @NonNull String message,
                       boolean isGamer,
                       int nextMessage,
                       @Nullable Choices choices) {
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

    public Choices getChoices() {
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
        GameMessage that = (GameMessage) o;
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
