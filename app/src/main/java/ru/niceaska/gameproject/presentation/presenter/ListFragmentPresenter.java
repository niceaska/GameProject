package ru.niceaska.gameproject.presentation.presenter;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.GameLoopInteractor;
import ru.niceaska.gameproject.domain.GameStartInteractor;
import ru.niceaska.gameproject.domain.SaveGameInteractor;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.presentation.view.MessageListFragment;

public class ListFragmentPresenter {


    public enum Choice {
        NEGATIVE,
        POSITIVE
    }


    private int lastIndex;
    private List<ListItem> listItems;
    private WeakReference<MessageListFragment> messageListFragmentWeakReference;
    private CompositeDisposable compositeDisposable;
    private GameStartInteractor gameStartInteractor;
    private GameLoopInteractor gameLoopInteractor;
    private SaveGameInteractor saveGameInteractor;

    public ListFragmentPresenter(GameStartInteractor gameStartInteractor,
                                 GameLoopInteractor gameLoopInteractor,
                                 SaveGameInteractor saveGameInteractor) {
        this.compositeDisposable = new CompositeDisposable();
        this.gameStartInteractor = gameStartInteractor;
        this.gameLoopInteractor = gameLoopInteractor;
        this.saveGameInteractor = saveGameInteractor;
    }

    public void attachView(MessageListFragment messageListFragment) {
        this.messageListFragmentWeakReference = new WeakReference<>(messageListFragment);

    }

    public void onGameStart() {
        compositeDisposable.add(gameStartInteractor.loadHistory()
                .map(messageList -> {
                    listItems = messageList;
                    if (messageList.isEmpty() || !(messageList.get(messageList.size() - 1) instanceof MessageChoices)) {
                        compositeDisposable.add(gameStartInteractor.loadUserProgress()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(integer -> {
                                    lastIndex = integer;
                                    gameLoop(gameLoopInteractor.getNextIndex(listItems));
                                }, throwable -> {
                                }));
                    }
                    return messageList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateMessages, throwable -> {
                }));

    }

    private void updateMessages(List<ListItem> historyMessages) {
        MessageListFragment fragment = messageListFragmentWeakReference.get();
        if (fragment != null) {
            fragment.updateMessageList(historyMessages);
            fragment.scrollToBottom();
        }
    }

    private void gameLoop(final int nextIndex) {
        if (listItems != null && nextIndex < 9) {
            compositeDisposable.add(gameLoopInteractor.loadNewMessage(nextIndex, listItems)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> {
                        MessageListFragment fragment = messageListFragmentWeakReference.get();
                        if (fragment != null && fragment.isAdded()) {
                            fragment.showUserTyping();
                            fragment.showAnimation();
                        }
                    })
                    .delay(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((items) -> {
                        MessageListFragment listFragment = messageListFragmentWeakReference.get();
                        listItems = items;
                        if (listFragment != null) {
                            listFragment.updateMessageList(items);
                            listFragment.scrollToBottom();
                            listFragment.hideUserTyping();
                            listFragment.clearAnimation();
                            setLastIndex(nextIndex);
                            if (!items.isEmpty()) {
                                ListItem item = items.get(items.size() - 1);
                                checkGameState(items, item instanceof MessageChoices);
                            }
                        }
                    }, throwable -> {
                    }));
        }
    }

    private void checkGameState(List<ListItem> itemlist, boolean isGameStopped) {
        this.listItems = itemlist;
        if (!isGameStopped) {
            gameLoop(gameLoopInteractor.getNextIndex(itemlist));
        }
    }

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
        if (!listItems.isEmpty()) {
            final Object item = listItems.get(listItems.size() - 1);
            if (item instanceof MessageChoices) {
                List<ListItem> newList = gameLoopInteractor.updateMessageList((MessageChoices) item, listItems, false);
                messageListFragmentWeakReference.get().updateMessageList(newList);
                this.listItems = newList;
                gameLoop(((MessageChoices) item).getPositiveMessageAnswer());
            }
        }
    }

    private void negativeChoiceCallback(List<ListItem> listItems) {
        if (!listItems.isEmpty()) {
            final Object item = listItems.get(listItems.size() - 1);
            if (item instanceof MessageChoices) {
                List<ListItem> newList = gameLoopInteractor.updateMessageList((MessageChoices) item, listItems, true);
                messageListFragmentWeakReference.get().updateMessageList(newList);
                this.listItems = newList;
                gameLoop(((MessageChoices) item).getNegativeMessageAnswer());
            }
        }
    }

    public void save(List<ListItem> itemList) {
        compositeDisposable.add(saveGameInteractor.saveGame(lastIndex, itemList)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d("SAVE", "saveOnDetachView: "),
                        throwable -> Log.d("SAVE", "saveOnDetachView: " + throwable.getMessage())
                ));
    }

    private void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public void detachView() {
        clearDisposable();
        messageListFragmentWeakReference.clear();
    }

    public void clearDisposable() {
        compositeDisposable.clear();
    }

}
