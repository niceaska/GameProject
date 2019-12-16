package ru.niceaska.gameproject.presentation.view;

/**
 * Интерфейс основной активити приложения
 */
public interface IMainActivity {

    /**
     * Показать фрагмент старта приложения
     */
    void showStartAppFragment();

    /**
     * Начать игру
     */
    void startGame();

    /**
     * Удалить все запланированные уведомления
     */
    void clearNotifications();

    /**
     * Запланировать уведомления
     */
    void planningNotification();
}
