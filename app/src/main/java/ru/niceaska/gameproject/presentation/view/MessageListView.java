package ru.niceaska.gameproject.presentation.view;

import java.util.List;

import ru.niceaska.gameproject.data.model.ListItem;

/**
 * Интерфейс вью списка сообщений
 */
public interface MessageListView {
    /**
     * Показать анимацию печатания
     */
    void showAnimation();

    /**
     * Показать строку с информаций о печати
     */
    void showUserTyping();

    /**
     * Скрыть строку с информаций о печати
     */
    void hideUserTyping();

    /**
     * Остановить анимацию
     */
    void clearAnimation();

    /**
     * Прокрутить список к последнему сообщению
     */
    void scrollToBottom();

    /**
     * Показать прогресс бар загрузки
     */
    void showLoadingProgressBar();

    /**
     * Скрыть прогресс бар загрузки
     */
    void hideLoadingProgressBar();

    /**
     * Установить аниматор для элементов списка
     */
    void setUpdateAnimator(boolean isMesageAnimation);

    /**
     * Обновить список элементов ресайклера
     *
     * @param newList новый список
     */
    void updateMessageList(List<ListItem> newList);
}
