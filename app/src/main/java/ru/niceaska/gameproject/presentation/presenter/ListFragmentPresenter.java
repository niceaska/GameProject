package ru.niceaska.gameproject.presentation.presenter;

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
    private WeakReference<MessageListView> messageListFragmentWeakReference;
    private CompositeDisposable compositeDisposable;
    private GameStartInteractor gameStartInteractor;
    private GameLoopInteractor gameLoopInteractor;
    private SaveGameInteractor saveGameInteractor;
    private IRxSchedulers rxSchedulers;

    public ListFragmentPresenter(GameStartInteractor gameStartInteractor,
                                 GameLoopInteractor gameLoopInteractor,
                                 SaveGameInteractor saveGameInteractor,
                                 IRxSchedulers rxSchedulers) {
        this.rxSchedulers = rxSchedulers;
        this.compositeDisposable = new CompositeDisposable();
        this.gameStartInteractor = gameStartInteractor;
        this.gameLoopInteractor = gameLoopInteractor;
        this.saveGameInteractor = saveGameInteractor;
    }

    /**
     * Аттачит вью к презентеру
     *
     * @param messageListFragment вью для презентера
     */
    public void attachView(MessageListView messageListFragment) {
        this.messageListFragmentWeakReference = new WeakReference<>(messageListFragment);

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
                .doOnSubscribe(disposable -> setMessagesAnimation())
                .observeOn(rxSchedulers.getMainThreadScheduler())
                .subscribe(this::updateMessages, throwable -> {
                })
        );

    }

    private void setMessagesAnimation() {
        boolean isEnabled = gameStartInteractor.isMessageAnimationEnabled();
        if (messageListFragmentWeakReference.get() != null) {
            messageListFragmentWeakReference.get().setUpdateAnimator(isEnabled);
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

    private void updateMessages(List<ListItem> historyMessages) {
        MessageListView fragment = messageListFragmentWeakReference.get();
        if (fragment != null) {
            fragment.updateMessageList(historyMessages);
            fragment.scrollToBottom();
        }
    }

    /**
     * Основной цикл игры
     * @param nextIndex индекс следующего сообщения для загрузки
     */
    private void gameLoop(final int nextIndex) {
        if (listItems != null && nextIndex < 120) {
            compositeDisposable.add(gameLoopInteractor.loadNewMessage(nextIndex, listItems)
                    .subscribeOn(rxSchedulers.getIoScheduler())
                    .doOnSubscribe(disposable -> {
                        MessageListView fragment = messageListFragmentWeakReference.get();
                        if (fragment != null) {
                            fragment.showUserTyping();
                            fragment.showAnimation();
                        }
                    })
                    .delay(3, TimeUnit.SECONDS)
                    .observeOn(rxSchedulers.getMainThreadScheduler())
                    .subscribe((items) -> {
                        MessageListView listFragment = messageListFragmentWeakReference.get();
                        listItems = items;
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
                        clearTypingAnimation();
                    }));
        }
    }

    private void clearTypingAnimation() {
        MessageListView listView = messageListFragmentWeakReference.get();
        if (listView != null) {
            listView.hideUserTyping();
            listView.clearAnimation();
        }
    }

    private void checkGameState(List<ListItem> itemlist, boolean isGameStopped) {
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
    public void changeListOnClick(Choice choice, List<ListItem> listItems) {
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

    private void positiveChoiceCallback(List<ListItem> listItems) {
        MessageListView messageListView = messageListFragmentWeakReference.get();
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

    private void negativeChoiceCallback(List<ListItem> listItems) {
        MessageListView messageListView = messageListFragmentWeakReference.get();
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
    public void save(List<ListItem> itemList) {
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
        messageListFragmentWeakReference.clear();
    }

    public void clearDisposable() {
        compositeDisposable.clear();
    }

}
