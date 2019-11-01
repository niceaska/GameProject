package ru.niceaska.gameproject.data.repository;

import java.util.List;

import ru.niceaska.gameproject.MyApp;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.User;

public class DataRepository {

    private AppDatabase database;

    public DataRepository() {
        database = MyApp.getInstance().getDatabase();
    }

    public void insertUserInformation(User user) {
        database.getUserDao().insert(user);
    }

    public void insertMessages(List<GameMessage> messageList) {
        database.getGameMessgeDao().insertMessge(messageList);
    }

    public void insertMessages(GameMessage... messages) {
        database.getGameMessgeDao().insertMessge(messages);
    }

    public GameMessage getMessageById(long id) {
        return database.getGameMessgeDao().getById(id);
    }

    public List<HistoryMessage> getHistory(String userId) {
        User user = getUser(userId);
        return user.savedMessages;
    }

    public User getUser(String userId) {
        return database.getUserDao().getUserById(userId);
    }

    private void updateUserInfo(String userId, int progress, List<HistoryMessage> historyMessageList) {
        User user = getUser(userId);
        user.savedMessages = historyMessageList;
        user.userPojo.setProgress(progress);
        insertUserInformation(user);
    }

}