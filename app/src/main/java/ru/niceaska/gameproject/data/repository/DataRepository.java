package ru.niceaska.gameproject.data.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.niceaska.gameproject.MyApp;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;
import ru.niceaska.gameproject.domain.model.MessageItem;

public class DataRepository {

    public static final String USER_ID = "1";
    public static final String USER_NAME = "test";
    private AppDatabase database;
    private MessageConverter messageConverter = new MessageConverter();
    private SaveMessageConverter saveMessageConverter = new SaveMessageConverter();


    public DataRepository() {
        database = MyApp.getInstance().getDatabase();
    }

    private Completable insertUserInformation(User user) {
        return Completable.fromRunnable(() -> database.getUserDao().insert(user));
    }

    private Completable insertMessages(List<GameMessage> messageList) {
        return database.getGameMessgeDao().insertMessge(messageList);
    }

    private Single<MessageItem> getMessageById(long id) {
        return database.getGameMessgeDao().getById(id)
                .map(gameMessage -> messageConverter.convertFromGameMessage(gameMessage));
    }


    public Completable saveUserData(int lastIndex, List<ListItem> messageItems) {
        UserPojo userPojo = new UserPojo(USER_ID, USER_NAME, lastIndex);
        User user = new User(userPojo, saveMessageConverter.convertToHistory(messageItems));
        return insertUserInformation(user);
    }

    public Single<List<ListItem>> loadNewGameMessage(int nextIndex, List<ListItem> listItems) {
        return getMessageById(nextIndex).map(gameMessage -> {
            List<ListItem> items = new ArrayList<>(listItems);
            if (gameMessage != null) {
                items.add(gameMessage);
                if (gameMessage.getChoices() != null) {
                    items.add(gameMessage.getChoices());
                }
            }
            return items;
        });
    }

    public Single<Integer> loadUserProgress(String userId) {
        return database.getUserDao().getuserProgress(userId);
    }

    public Single<Boolean> checkFirstStart(String userId) {
        return database.getUserDao().getUserById(userId).map(user -> user == null || user.savedMessages.isEmpty());
    }

    public Observable<Object> firstLoadData(Reader open) {
        User user = new User(new UserPojo(USER_ID, USER_NAME, 0), new ArrayList<HistoryMessage>());
        return Observable.fromCallable(() -> {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<GameMessage>>() {
            }.getType();
            return gson.<List<GameMessage>>fromJson(open, listType);
        }).flatMap((gameMessages) -> insertMessages(gameMessages).toObservable())
                .mergeWith(insertUserInformation(user));
    }

    public Single<List<MessageItem>> loadHistory(String userId) {
        return database.getUserDao().getUserById(userId).map(user -> messageConverter.convertFromHistory(user.savedMessages));
    }


}
