package ru.niceaska.gameproject.presentation.view;

/**
 * Интерфейс стартового вью приложения
 */
public interface GameStartView {

    /**
     * Начать новую игру
     */
    void beginNewGame();

    /**
     * Показать тост с ошибкой
     */
    void showErrorToast();
}
