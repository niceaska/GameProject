package ru.niceaska.gameproject.presentation.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.interactors.GameLoopInteractor;
import ru.niceaska.gameproject.domain.interactors.GameStartInteractor;
import ru.niceaska.gameproject.domain.interactors.SaveGameInteractor;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.presentation.view.MessageListView;
import ru.niceaska.gameproject.rx.IRxSchedulers;

/**
 * Презентер списка сообщений
 */
public class ListFragmentPresenter {

    /**
     * enum типов ответов
     */
    public enum Choice {
        NEGATIVE,
        POSITIVE
    }

    private int lastIndex;
    private List<ListItem> listItems;
    private WeakReference<MessageListView> messageViewReference;
    private CompositeDisposable compositeDisposable;
    private GameStartInteractor gameStartInteractor;
    private GameLoopInteractor gameLoopInteractor;
    private SaveGameInteractor saveGameInteractor;
    private IRxSchedulers rxSchedulers;

    /**
     * Конструктор презентера
     *
     * @param gameStartInteractor интерактор старта игры
     * @param gameLoopInteractor  интерактор основного цикла игры
     * @param saveGameInteractor  интерактор сохранения игры
     * @param rxSchedulers        шедулеры rxjava
     */
    public ListFragmentPresenter(@NonNull GameStartInteractor gameStartInteractor,
                                 @NonNull GameLoopInteractor gameLoopInteractor,
                                 @NonNull SaveGameInteractor saveGameInteractor,
                                 @NonNull IRxSchedulers rxSchedulers) {
        this.rxSchedulers = rxSchedulers;
        this.compositeDisposable = new CompositeDisposable();
        this.gameStartInteractor = gameStartInteractor;
        this.gameLoopInteractor = gameLoopInteractor;
        this.saveGameInteractor = saveGameInteractor;
    }

    /**
     * Аттачит вью к презентеру
     *
     * @param view вью для презентера
     */
    public void attachView(@NonNull MessageListView view) {
        this.messageViewReference = new WeakReference<>(view);
    }

    /**
     * Загружает историю сообщений при старте игры, обновляет ресайклер
     * и при необходимости запускает цикл игры
     */
    public void onGameStart() {
        compositeDisposable.add(gameStartInteractor.loadHistory()
                .map(messageList -> {
                    listItems = messageList;
                    if (messageList.isEmpty() ||
                            !(messageList.get(messageList.size() - 1) instanceof MessageChoices)) {
                        runGameLoop();
                    }
                    return messageList;
                })
                .subscribeOn(rxSchedulers.getIoScheduler())
                .doOnSubscribe(disposable -> {
                    setMessagesAnimation();
                    showProgressBar();
                })
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .subscribe(this::updateMessages, throwable -> {
                })
        );

    }

    private void showProgressBar() {
        MessageListView listView = messageViewReference.get();
        if (listView != null) {
            listView.showLoadingProgressBar();
        }
    }

    private void setMessagesAnimation() {
        boolean isEnabled = gameStartInteractor.isMessageAnimationEnabled();
        MessageListView listView = messageViewReference.get();
        if (listView != null) {
            listView.setUpdateAnimator(isEnabled);
        }
    }

    private void runGameLoop() {
        compositeDisposable.add(gameStartInteractor.loadUserProgress()
                .subscribeOn(rxSchedulers.getIoScheduler())
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .subscribe(integer -> {
                    lastIndex = integer;
                    gameLoop(gameLoopInteractor.getNextIndex(listItems));
                }, throwable -> {
                }));
    }

    private void updateMessages(@NonNull List<ListItem> historyMessages) {
        MessageListView listView = messageViewReference.get();
        if (listView != null) {
            listView.hideLoadingProgressBar();
            listView.updateMessageList(historyMessages);
            listView.scrollToBottom();
        }
    }

    /**
     * Основной цикл игры
     * @param nextIndex индекс следующего сообщения для загрузки
     */
    private void gameLoop(final int nextIndex) {
        if (listItems != null) {
            compositeDisposable.add(gameLoopInteractor.loadNewMessage(nextIndex, listItems)
                    .subscribeOn(rxSchedulers.getIoScheduler())
                    .doOnSubscribe(disposable -> {
                        MessageListView view = messageViewReference.get();
                        if (view != null) {
                            view.hideLoadingProgressBar();
                            view.showUserTyping();
                            view.showAnimation();
                        }
                    })
                    .delay(3, TimeUnit.SECONDS)
                    .observeOn(rxSchedulers.getMainThreadScheduler())
                    .subscribe((items) -> {
                        listItems = items;
                        MessageListView listFragment = messageViewReference.get();
                        if (listFragment != null) {
                            listFragment.updateMessageList(items);
                            listFragment.scrollToBottom();
                            clearTypingAnimation();
                            setLastIndex(nextIndex);
                            if (!items.isEmpty()) {
                                ListItem item = items.get(items.size() - 1);
                                checkGameState(items, item instanceof MessageChoices);
                            }
                        }
                    }, throwable -> {
                        Log.d("GAME", "gameLoop: " + throwable.getMessage());
                        clearTypingAnimation();
                    }));
        }
    }

    private void clearTypingAnimation() {
        MessageListView listView = messageViewReference.get();
        if (listView != null) {
            listView.hideUserTyping();
            listView.clearAnimation();
        }
    }

    private void checkGameState(@NonNull List<ListItem> itemlist, boolean isGameStopped) {
        this.listItems = itemlist;
        if (!isGameStopped) {
            gameLoop(gameLoopInteractor.getNextIndex(itemlist));
        }
    }

    /**
     * Нужен для выбора соответствующего коллбека по нажатию на кнопку
     * @param choice тип выбора
     * @param listItems список элементов ресайклера
     */
    public void changeListOnClick(@NonNull Choice choice, @NonNull List<ListItem> listItems) {
        switch (choice) {
            case NEGATIVE:
                negativeChoiceCallback(listItems);
                break;
            case POSITIVE:
                positiveChoiceCallback(listItems);
                break;
            default:
                break;
        }
    }

    private void positiveChoiceCallback(@NonNull List<ListItem> listItems) {
        MessageListView messageListView = messageViewReference.get();
        if (messageListView != null && !listItems.isEmpty()) {
            final Object item = listItems.get(listItems.size() - 1);
            if (item instanceof MessageChoices) {
                List<ListItem> newList = gameLoopInteractor.updateMessageList((MessageChoices) item,
                        listItems, false);
                messageListView.updateMessageList(newList);
                this.listItems = newList;
                gameLoop(((MessageChoices) item).getPositiveMessageAnswer());
            }
        }
    }

    private void negativeChoiceCallback(@NonNull List<ListItem> listItems) {
        MessageListView messageListView = messageViewReference.get();
        if (messageListView != null && !listItems.isEmpty()) {
            final Object item = listItems.get(listItems.size() - 1);
            if (item instanceof MessageChoices) {
                List<ListItem> newList = gameLoopInteractor.updateMessageList((MessageChoices) item,
                        listItems, true);
                messageListView.updateMessageList(newList);
                this.listItems = newList;
                gameLoop(((MessageChoices) item).getNegativeMessageAnswer());
            }
        }
    }

    /**
     * Сохранить прогресс игрока в базу данных
     * @param itemList список элементов ресайклера
     */
    public void save(@NonNull List<ListItem> itemList) {
        compositeDisposable.add(saveGameInteractor.saveGame(lastIndex, itemList)
                .subscribeOn(rxSchedulers.getIoScheduler())
                .subscribe());
    }

    private void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    /**
     * Деаттачит вью от презентера
     */
    public void detachView() {
        clearDisposable();
        messageViewReference.clear();
    }

    /**
     * Отчищает диспосаблс
     */
    public void clearDisposable() {
        compositeDisposable.clear();
    }

}
