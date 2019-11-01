package ru.niceaska.gameproject.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

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
}
