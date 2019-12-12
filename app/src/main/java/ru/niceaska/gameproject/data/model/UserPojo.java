package ru.niceaska.gameproject.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.common.base.Objects;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPojo userPojo = (UserPojo) o;
        return progress == userPojo.progress &&
                Objects.equal(userId, userPojo.userId) &&
                Objects.equal(name, userPojo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, name, progress);
    }
}
