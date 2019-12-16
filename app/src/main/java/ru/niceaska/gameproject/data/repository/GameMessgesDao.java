package ru.niceaska.gameproject.data.repository;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.niceaska.gameproject.data.model.GameMessage;

/**
 * Дао для игровых сообщений
 */
@Dao
public interface GameMessgesDao {

    @Query("SELECT * from message")
    List<GameMessage> getAllMessges();

    @Query("SELECT * from message WHERE id = :id")
    Single<GameMessage> getById(long id);

    @Insert
    Completable insertMessge(GameMessage... gameMessages);

    @Insert
    Completable insertMessge(List<GameMessage> gameMessages);
}

