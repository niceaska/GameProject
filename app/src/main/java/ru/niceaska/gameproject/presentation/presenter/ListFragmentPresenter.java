package ru.niceaska.gameproject.presentation.presenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.model.MessageItem;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.view.MessageListFragment;

public class ListFragmentPresenter {


    public enum Choice {
        NEGATIVE,
        POSITIVE
    }

    private int lastIndex;
    private List<ListItem> listItems;
    private WeakReference<MessageListFragment> messageListFragmentWeakReference;
    private DataRepository dataRepository;
    private CompositeDisposable compositeDisposable;

    public ListFragmentPresenter(MessageListFragment messageListFragmentWeakReference,
                                 DataRepository dataRepository) {
        this.messageListFragmentWeakReference = new WeakReference<>(messageListFragmentWeakReference);
        this.dataRepository = dataRepository;
        this.compositeDisposable = new CompositeDisposable();
    }


    public void onGameStart() {
        compositeDisposable.add(dataRepository.loadHistory("1")
                .subscribeOn(Schedulers.io())
                .map(historyMessages -> {
                    List<ListItem> messageList = new ArrayList<>(historyMessages);
                    if (!historyMessages.isEmpty()) {
                        ListItem lastItem = messageList.get(messageList.size() - 1);
                        if (((HistoryMessage) lastItem).getChoices() != null) {
                            messageList.add(((HistoryMessage) lastItem).getChoices());
                        }
                    }
                    listItems = messageList;
                    return messageList;
                }).map(messageList -> {
                    if (messageList.isEmpty() || !(messageList.get(messageList.size() - 1) instanceof Choices)) {
                        compositeDisposable.add(dataRepository.loadUserProgress("1")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(integer -> {
                                    lastIndex = integer;
                                    gameLoop(getNextIndex());
                                }, throwable -> {
                                }));
                    }
                    return messageList;
                })
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

    private int getNextIndex() {
        int nextIndex = 1;
        if (!listItems.isEmpty() && listItems.get(listItems.size() - 1) instanceof MessageItem) {
            nextIndex = ((MessageItem) listItems.get(listItems.size() - 1)).getNextMessage();
        }
        return nextIndex;
    }

    private void gameLoop(final int nextIndex) {
        if (listItems != null && nextIndex < 9) {
            final Random random = new Random();
            final int rand = random.nextInt(5) + 5;
            MessageListFragment fragment = messageListFragmentWeakReference.get();
            if (fragment != null && fragment.isAdded()) {
                fragment.showUserTyping();
                fragment.showAnimation(rand);
            }
            compositeDisposable.add(dataRepository.loadNewGameMessage(nextIndex, listItems)
                    .subscribeOn(Schedulers.io())
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
                                checkGameState(items, item instanceof Choices);
                            }
                        }
                    }, throwable -> {
                    }));
        }
        }


    private void saveOnDestroy(List<ListItem> listItems) {

        if (listItems.isEmpty()) return;

        List<HistoryMessage> historyMessagesList = new ArrayList<>();
        for (int i = 0; i < listItems.size(); i++) {
            ListItem listItem = listItems.get(i);
            if (listItem instanceof HistoryMessage) {
                historyMessagesList.add((HistoryMessage) listItem);
            } else if (listItems.get(i) instanceof GameMessage) {
                GameMessage message = (GameMessage) listItem;
                historyMessagesList.add(
                        new HistoryMessage(String.valueOf(i), "1",
                                message.getMessage(), message.isGamer(), message.getNextMessage(), message.getChoices()));
            }
        }
        UserPojo userPojo = new UserPojo("1", "test", lastIndex);
        User user = new User(userPojo, historyMessagesList);
        dataRepository.saveUserData(user).subscribe();
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

    private void checkGameState(List<ListItem> itemlist, boolean isGameStopped) {
        this.listItems = itemlist;
        if (!isGameStopped) {
            gameLoop(getNextIndex());
        }
    }

    private void positiveChoiceCallback(List<ListItem> listItems) {
        final List<ListItem> newList = new ArrayList<ListItem>(listItems);
        if (!newList.isEmpty()) {
            final Object item = newList.get(newList.size() - 1);
            if (item instanceof Choices) {
                newList.set(newList.size() - 1,
                        new HistoryMessage(String.valueOf(newList.size() - 1), "1",
                                ((Choices) item).getPositiveChoice(), true,
                                ((Choices) item).getNegativeMessageAnswer(), null)
                );
                messageListFragmentWeakReference.get().updateMessageList(newList);
                this.listItems = newList;
                gameLoop(((Choices) item).getPositiveMessageAnswer());
            }
        }
    }

    private void negativeChoiceCallback(List<ListItem> listItems) {
        final List<ListItem> newList = new ArrayList<ListItem>(listItems);
        if (!newList.isEmpty()) {
            final Object item = newList.get(newList.size() - 1);
            if (item instanceof Choices) {
                newList.set(newList.size() - 1,
                        new HistoryMessage(String.valueOf(newList.size() - 1), "1",
                                ((Choices) item).getNegativeChoice(), true,
                                ((Choices) item).getNegativeMessageAnswer(), null)
                );
                messageListFragmentWeakReference.get().updateMessageList(newList);
                this.listItems = newList;
                gameLoop(((Choices) item).getNegativeMessageAnswer());
            }
        }
    }

    public void saveOnDetachView(List<ListItem> itemList) {
        saveOnDestroy(itemList);
        detachView();
        clearDisposable();
    }

    private void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    private void detachView() {
        messageListFragmentWeakReference.clear();
    }

    private void clearDisposable() {
        compositeDisposable.clear();
    }

}
