package ru.niceaska.gameproject.data.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import io.reactivex.Single;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;

/**
 * Dao для таблички игрока
 */
@Dao
public abstract class UserDao {
    @Transaction
    @Query("SELECT * FROM UserPojo WHERE userId = :userId")
    public abstract Single<User> getUserById(String userId);

    @Query("SELECT progress FROM UserPojo WHERE userId = :userId")
    public abstract Single<Integer> getuserProgress(String userId);

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

    @Query("DELETE FROM UserPojo")
    public abstract void delete();
}
