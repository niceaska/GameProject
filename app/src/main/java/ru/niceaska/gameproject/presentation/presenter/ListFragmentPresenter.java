package ru.niceaska.gameproject.presentation.presenter;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.data.repository.IOnMessageLoadListener;
import ru.niceaska.gameproject.data.repository.IOnUserProgressLoadListener;
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
    private Handler handler;

    public ListFragmentPresenter(MessageListFragment messageListFragmentWeakReference,
                                 DataRepository dataRepository) {
        this.messageListFragmentWeakReference = new WeakReference<>(messageListFragmentWeakReference);
        this.dataRepository = dataRepository;
        this.handler = new Handler();
    }


    public void onGameStart(int lastIndex, boolean isRestored, List<ListItem> listItems) {
        this.lastIndex = lastIndex;
        if (!isRestored) {
            List<HistoryMessage> messages = dataRepository.getHistory("1");
            List<ListItem> messageList = new ArrayList<ListItem>(messages);
            messageListFragmentWeakReference.get().initRecycler(messageList);
            messageListFragmentWeakReference.get().initRecyclerListeners();
            this.listItems = messageList;
            IOnUserProgressLoadListener listener = new IOnUserProgressLoadListener() {
                @Override
                public void onLoadData(int progress) {
                    setLastIndex(progress);
                    messageListFragmentWeakReference.get().setGameProgress(progress);
                    gameLoop();
                }
            };
            dataRepository.loadUserProgress(listener);
        } else {
            messageListFragmentWeakReference.get().initRecycler(listItems);
            messageListFragmentWeakReference.get().initRecyclerListeners();
            this.listItems = listItems;
            if (listItems.isEmpty() || !(listItems.get(listItems.size() - 1) instanceof Choices)) {
                gameLoop();
            }
        }
    }

    private void gameLoop() {
        if (listItems != null && lastIndex < 4) {
            final ListFragmentPresenter listFragmentPresenter = this;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    IOnMessageLoadListener listener = new IOnMessageLoadListener() {
                        @Override
                        public void onDataLoaded(List<ListItem> listItems) {
                            MessageListFragment fragment = listFragmentPresenter.messageListFragmentWeakReference.get();
                            if (fragment != null) {
                                fragment.updateMessageList(listItems);
                                fragment.scrollToBottom();
                                fragment.hideUserTyping();
                                fragment.clearAnimation();
                                listFragmentPresenter.setLastIndex(lastIndex + 1);
                                fragment.setGameProgress(lastIndex + 1);
                                if (!listItems.isEmpty()) {
                                    ListItem item = listItems.get(listItems.size() - 1);
                                    listFragmentPresenter.checkGameState(listItems, item instanceof Choices);
                                }
                            }
                        }
                    };
                    dataRepository.loadNewGameMessage(lastIndex, listener, listItems);
                }
            };
            runDelayedMessage(task);
        }
    }

    private void runDelayedMessage(Runnable task) {
        final Random random = new Random();
        final int rand = random.nextInt(5) + 5;
        MessageListFragment fragment = messageListFragmentWeakReference.get();
        if (fragment != null && fragment.isAdded()) {
            fragment.showUserTyping();
            fragment.showAnimation(rand);
            handler.postDelayed(task, rand * 2500);
        }
    }

    public void saveOnDestroy(List<ListItem> listItems) {

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
                                message.getMessage(), message.getTime(), message.isGamer()));
            }
        }
        ListItem lastItem = listItems.get(listItems.size() - 1);
        if (lastItem instanceof Choices) {
            historyMessagesList.remove(historyMessagesList.size() - 1);
            lastIndex--;
        }
        UserPojo userPojo = new UserPojo("1", "test", lastIndex);
        User user = new User(userPojo, historyMessagesList);
        dataRepository.saveUserData(user);
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
            gameLoop();
        }
    }

    private void positiveChoiceCallback(List<ListItem> listItems) {
        final List<ListItem> newList = new ArrayList<ListItem>(listItems);
        if (!newList.isEmpty()) {
            final Object item = newList.get(newList.size() - 1);
            if (item instanceof Choices) {
                newList.set(newList.size() - 1,
                        new HistoryMessage(String.valueOf(newList.size() - 1), "1",
                                ((Choices) item).getPositiveChoice(), 0, true)
                );
                messageListFragmentWeakReference.get().updateMessageList(newList);
                runDelayedMessage(new Runnable() {
                    @Override
                    public void run() {
                        newList.add(
                                new HistoryMessage(String.valueOf(newList.size()), "1",
                                        ((Choices) item).getPositiveMessageAnswer(), 0, false)
                        );
                        messageListFragmentWeakReference.get().updateMessageList(newList);
                        messageListFragmentWeakReference.get().hideUserTyping();
                        messageListFragmentWeakReference.get().clearAnimation();
                        messageListFragmentWeakReference.get().scrollToBottom();
                        gameLoop();
                    }
                });
                this.listItems = newList;
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
                                ((Choices) item).getNegativeChoice(), 0, true)
                );
                messageListFragmentWeakReference.get().updateMessageList(newList);
                runDelayedMessage(new Runnable() {
                    @Override
                    public void run() {
                        newList.add(
                                new HistoryMessage(String.valueOf(newList.size()), "1",
                                        ((Choices) item).getNegativeMessageAnswer(), 0, false)
                        );
                        messageListFragmentWeakReference.get().updateMessageList(newList);
                        messageListFragmentWeakReference.get().hideUserTyping();
                        messageListFragmentWeakReference.get().clearAnimation();
                        messageListFragmentWeakReference.get().scrollToBottom();
                        gameLoop();
                    }
                });
                this.listItems = newList;
            }
        }
    }


    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }


}
