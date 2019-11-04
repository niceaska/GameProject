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
import ru.niceaska.gameproject.data.model.MessageItem;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.data.repository.IOnHistoryUpdatedListener;
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


    public void onGameStart() {

        final IOnUserProgressLoadListener progressLoadListener = new IOnUserProgressLoadListener() {
            @Override
            public void onLoadData(int progress) {
                setLastIndex(progress);
                getNextIndex();
                gameLoop(getNextIndex());
            }
        };
        final IOnHistoryUpdatedListener historyUpdatedListener = new IOnHistoryUpdatedListener() {
            @Override
            public void onHistoryLoad(List<HistoryMessage> historyMessages) {
                List<ListItem> messageList = new ArrayList<ListItem>(historyMessages);
                if (!historyMessages.isEmpty()) {
                    ListItem lastItem = messageList.get(messageList.size() - 1);
                    if (((HistoryMessage) lastItem).getChoices() != null) {
                        messageList.add(((HistoryMessage) lastItem).getChoices());
                    }
                }
                listItems = messageList;
                MessageListFragment fragment = messageListFragmentWeakReference.get();
                if (fragment != null) {
                    fragment.updateMessageList(messageList);
                    fragment.scrollToBottom();
                }
                if (historyMessages.isEmpty() || !(listItems.get(listItems.size() - 1) instanceof Choices)) {
                    dataRepository.loadUserProgress(progressLoadListener);
                }
            }
        };
        dataRepository.loadHistory("1", historyUpdatedListener);

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
                                listFragmentPresenter.setLastIndex(nextIndex);
                                if (!listItems.isEmpty()) {
                                    ListItem item = listItems.get(listItems.size() - 1);
                                    listFragmentPresenter.checkGameState(listItems, item instanceof Choices);
                                }
                            }
                        }
                    };
                    dataRepository.loadNewGameMessage(nextIndex, listener, listItems);
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
            handler.postDelayed(task, rand * 1000);
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
                                message.getMessage(), message.isGamer(), message.getNextMessage(), message.getChoices()));
            }
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
                gameLoop(((Choices) item).getPositiveMessageAnswer());
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
                                ((Choices) item).getNegativeChoice(), true,
                                ((Choices) item).getNegativeMessageAnswer(), null)
                );
                messageListFragmentWeakReference.get().updateMessageList(newList);
                gameLoop(((Choices) item).getNegativeMessageAnswer());
                this.listItems = newList;
            }
        }
    }


    private void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public void detachView() {
        messageListFragmentWeakReference.clear();
    }

}
