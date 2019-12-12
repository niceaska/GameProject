package ru.niceaska.gameproject.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.google.common.base.Objects;

import java.util.List;

public class User {
    @Embedded
    public UserPojo userPojo;
    @Relation(parentColumn = "userId", entity = HistoryMessage.class, entityColumn = "user")
    public List<HistoryMessage> savedMessages;

    public User(UserPojo userPojo, List<HistoryMessage> savedMessages) {
        this.userPojo = userPojo;
        this.savedMessages = savedMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equal(userPojo, user.userPojo) &&
                Objects.equal(savedMessages, user.savedMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userPojo, savedMessages);
    }
}
