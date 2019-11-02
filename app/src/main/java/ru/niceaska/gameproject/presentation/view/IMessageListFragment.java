package ru.niceaska.gameproject.presentation.view;

import java.util.List;

import ru.niceaska.gameproject.data.model.ListItem;

public interface IMessageListFragment {
    void showAnimation(int repeatCount);

    void showListData();

    void initRecyclerListeners();

    void initRecycler(List<ListItem> listItems);

    void updateMessageList(List<ListItem> newList);
}
