package ru.niceaska.gameproject.data.repository;

import java.util.List;

import ru.niceaska.gameproject.data.model.HistoryMessage;

public interface IOnHistoryUpdatedListener {
    void onHistoryLoad(List<HistoryMessage> historyMessages);
}
