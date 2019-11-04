package ru.niceaska.gameproject.presentation.view;

import java.util.List;

import ru.niceaska.gameproject.data.model.ListItem;

public interface IMessageListFragment {
    void showAnimation(int repeatCount);
    void showUserTyping();
    void hideUserTyping();
    void clearAnimation();
    void scrollToBottom();
    void updateMessageList(List<ListItem> newList);

    void onFinalDestroy();
}
