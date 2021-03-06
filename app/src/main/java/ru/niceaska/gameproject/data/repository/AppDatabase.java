package ru.niceaska.gameproject.data.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.UserPojo;

/**
 * База данных
 */
@Database(entities = {GameMessage.class, UserPojo.class, HistoryMessage.class}, version = 9)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GameMessgesDao getGameMessgeDao();

    public abstract UserDao getUserDao();
}
