package ru.niceaska.gameproject.data.repository;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

import ru.niceaska.gameproject.MyApp;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.model.User;

public class DataRepository {

    private static DataRepository instance;
    private AppDatabase database;

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }


    private DataRepository() {
        database = MyApp.getInstance().getDatabase();
    }

    void insertUserInformation(User user) {
        database.getUserDao().insert(user);
    }

    void insertMessages(List<GameMessage> messageList) {
        database.getGameMessgeDao().insertMessge(messageList);
    }

    void insertMessages(GameMessage... messages) {
        database.getGameMessgeDao().insertMessge(messages);
    }

    int getUserProgress(String userId) {
        return database.getUserDao().getuserProgress(userId);
    }

    GameMessage getMessageById(long id) {
        return database.getGameMessgeDao().getById(id);
    }

    public List<HistoryMessage> getHistory(String userId) {
        User user = getUser(userId);
        return user.savedMessages;
    }

    User getUser(String userId) {
        return database.getUserDao().getUserById(userId);
    }

    private void updateUserInfo(User user) {
        User userCheck = getUser(user.userPojo.getUserId());
        if (userCheck.userPojo.getProgress() > user.userPojo.getProgress()) {
            user.userPojo.setProgress(userCheck.userPojo.getProgress());
        }
        insertUserInformation(user);
    }

    public void saveUserData(User user) {
        new SaveGameDataOnStopTask().execute(user);
    }

    public void loadNewGameMessage(int lastIndex, IOnMessageLoadListener listener, List<ListItem> listItems) {
        new GameLoopAsyncTask(lastIndex, listener).execute(listItems);
    }

    public void loadUserProgress(IOnUserProgressLoadListener listener) {
        new UserProgressAsyncTask(listener).execute();
    }

    public void checkFirstStart(String userId, IOnGameRunCheckListener listener) {
        new GameStartAsyncTask(listener).execute(userId);
    }

    public void firstLoadData(User user, IOnFirstLoadDataListener listener, Activity activity) {
        new FirstLoadData(activity, listener).execute(user);
    }

    static class SaveGameDataOnStopTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            DataRepository dataRepository = new DataRepository();
            dataRepository.updateUserInfo(users[0]);
            return null;
        }
    }

}
