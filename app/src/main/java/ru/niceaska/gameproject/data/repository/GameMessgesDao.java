package ru.niceaska.gameproject.data.repository;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.niceaska.gameproject.data.model.GameMessage;


@Dao
public interface GameMessgesDao {
    @Query("SELECT * from message")
    List<GameMessage> getAllMessges();

    @Query("SELECT * from message WHERE id = :id")
    GameMessage getById(long id);

    @Insert
    long[] insertMessge(GameMessage... gameMessages);

    @Insert
    long[] insertMessge(List<GameMessage> gameMessages);
}
