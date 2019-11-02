package ru.niceaska.gameproject.presentation.presenter;

import android.os.AsyncTask;
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
            new UserProgressAsyncTask(this).execute();
        } else {
            messageListFragmentWeakReference.get().initRecycler(listItems);
            messageListFragmentWeakReference.get().initRecyclerListeners();
            this.listItems = listItems;
            if (!(listItems.get(listItems.size() - 1) instanceof Choices)) {
                gameLoop();
            }
        }
    }

    private void gameLoop() {
        if (listItems != null && lastIndex <= 4) {
            final ListFragmentPresenter listFragmentPresenter = this;
            final Random random = new Random();
            final int rand = random.nextInt(5) + 5;
            messageListFragmentWeakReference.get().showAnimation(rand);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GameLoopAsyncTask asyncTask = new GameLoopAsyncTask(getLastIndex(), listFragmentPresenter);
                    asyncTask.execute(listItems);
                }
            }, rand * 5000);
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
        new SaveGameDataOnDestroyTask().execute(user);
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
        List<ListItem> newList = new ArrayList<ListItem>(listItems);
        if (!newList.isEmpty()) {
            Object item = newList.get(newList.size() - 1);
            if (item instanceof Choices) {
                newList.set(newList.size() - 1,
                        new HistoryMessage(String.valueOf(newList.size() - 1), "1",
                                ((Choices) item).getPositiveChoice(), 0, true)
                );
                messageListFragmentWeakReference.get().updateMessageList(newList);
                newList.add(
                        new HistoryMessage(String.valueOf(newList.size()), "1",
                                ((Choices) item).getPositiveMessageAnswer(), 0, false)
                );

                messageListFragmentWeakReference.get().updateMessageList(newList);
                this.listItems = newList;
                gameLoop();
            }
        }
    }

    private void negativeChoiceCallback(List<ListItem> listItems) {
        List<ListItem> newList = new ArrayList<ListItem>(listItems);
        if (!newList.isEmpty()) {
            Object item = newList.get(newList.size() - 1);
            if (item instanceof Choices) {
                newList.set(newList.size() - 1,
                        new HistoryMessage(String.valueOf(newList.size() - 1), "1",
                                ((Choices) item).getNegativeChoice(), 0, true)
                );
                messageListFragmentWeakReference.get().updateMessageList(newList);
                newList.add(
                        new HistoryMessage(String.valueOf(newList.size()), "1",
                                ((Choices) item).getNegativeMessageAnswer(), 0, false)
                );
                messageListFragmentWeakReference.get().updateMessageList(newList);
                this.listItems = newList;
                gameLoop();
            }
        }
    }


    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    static class SaveGameDataOnDestroyTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            DataRepository dataRepository = new DataRepository();
            dataRepository.updateUserInfo(users[0]);
            return null;
        }
    }

    static class UserProgressAsyncTask extends AsyncTask<Void, Void, Integer> {

        WeakReference<ListFragmentPresenter> listFragmentPresenterWeakReference;


        public UserProgressAsyncTask(ListFragmentPresenter listFragmentPresenterWeakReference) {
            this.listFragmentPresenterWeakReference = new WeakReference<>(listFragmentPresenterWeakReference);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return listFragmentPresenterWeakReference.get()
                    .dataRepository.getUserProgress("1");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            listFragmentPresenterWeakReference.get().setLastIndex(integer);
            listFragmentPresenterWeakReference.get()
                    .messageListFragmentWeakReference.get()
                    .setGameProgress(integer);
            listFragmentPresenterWeakReference.get().gameLoop();
        }
    }

    static class GameLoopAsyncTask extends AsyncTask<List<ListItem>, Void, List<ListItem>> {

        int lastIndex;
        boolean gameStop;
        WeakReference<ListFragmentPresenter> listFragmentPresenterWeakReference;
        DataRepository dataRepository;

        public GameLoopAsyncTask(int lastIndex, ListFragmentPresenter listFragmentPresenterWeakReference) {
            this.listFragmentPresenterWeakReference = new WeakReference<>(listFragmentPresenterWeakReference);
            this.lastIndex = lastIndex;
        }

        @Override
        protected void onPreExecute() {
            dataRepository = listFragmentPresenterWeakReference.get().dataRepository;
            gameStop = false;
        }


        @Override
        protected List<ListItem> doInBackground(List<ListItem>... lists) {
            List<ListItem> listItems = new ArrayList<>(lists[0]);
            GameMessage gameMessage = dataRepository.getMessageById(lastIndex + 1);
            if (gameMessage != null) {
                listItems.add(gameMessage);
                if (gameMessage.getChoices() != null) {
                    listItems.add(gameMessage.getChoices());
                    gameStop = true;
                }
            } else {
                gameStop = true;
            }
            return listItems;
        }

        @Override
        protected void onPostExecute(List<ListItem> listItems) {
            listFragmentPresenterWeakReference.get()
                    .messageListFragmentWeakReference.get()
                    .updateMessageList(listItems);
            listFragmentPresenterWeakReference.get().setLastIndex(lastIndex + 1);
            listFragmentPresenterWeakReference.get()
                    .messageListFragmentWeakReference.get()
                    .setGameProgress(lastIndex + 1);
            listFragmentPresenterWeakReference.get().checkGameState(listItems, gameStop);
        }
    }
}
