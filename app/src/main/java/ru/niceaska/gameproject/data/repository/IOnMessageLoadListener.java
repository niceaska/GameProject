package ru.niceaska.gameproject.data.repository;

import java.util.List;

import ru.niceaska.gameproject.data.model.ListItem;

public interface IOnMessageLoadListener {
    void onDataLoaded(List<ListItem> listItems);
}
