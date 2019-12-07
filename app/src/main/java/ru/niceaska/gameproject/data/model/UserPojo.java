package ru.niceaska.gameproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserPojo {

    @NonNull
    @PrimaryKey
    private String userId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "progress")
    private int progress;

    public UserPojo(String userId, String name, int progress) {
        this.userId = userId;
        this.name = name;
        this.progress = progress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
