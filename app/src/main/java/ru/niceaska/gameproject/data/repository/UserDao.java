package ru.niceaska.gameproject.data.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;

@Dao
public abstract class UserDao {
    @Transaction
    @Query("SELECT * FROM UserPojo WHERE userId = :userId")
    public abstract User getUserById(String userId);

    @Transaction
    public void insert(User user) {
        insert(user.userPojo);
        for (HistoryMessage message : user.savedMessages) {
            insertMessage(message);
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(UserPojo user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertMessage(HistoryMessage gameMessage);
}