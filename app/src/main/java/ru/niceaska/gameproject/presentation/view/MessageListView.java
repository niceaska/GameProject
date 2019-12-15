package ru.niceaska.gameproject.presentation.view;

import java.util.List;

import ru.niceaska.gameproject.data.model.ListItem;

public interface MessageListView {
    void showAnimation();
    void showUserTyping();
    void hideUserTyping();
    void clearAnimation();
    void scrollToBottom();

    void showLoadingProgressBar();

    void hideLoadingProgressBar();
    void setUpdateAnimator(boolean isMesageAnimation);
    void updateMessageList(List<ListItem> newList);
}
