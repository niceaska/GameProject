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
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.MyApp;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.model.User;

public class DataRepository {

    private static DataRepository instance;
    private AppDatabase database;


    public DataRepository() {
        database = MyApp.getInstance().getDatabase();
    }

    private Completable insertUserInformation(User user) {
        return Completable.fromRunnable(() -> database.getUserDao().insert(user));
    }

    private Completable insertMessages(List<GameMessage> messageList) {
        return database.getGameMessgeDao().insertMessge(messageList);
    }

    private Single<GameMessage> getMessageById(long id) {
        return database.getGameMessgeDao().getById(id);
    }


    public Completable saveUserData(User user) {
        return insertUserInformation(user).subscribeOn(Schedulers.io());
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

        //new GameLoopAsyncTask(lastIndex, listener).execute(listItems);
    }

    public Single<Integer> loadUserProgress(String userId) {
        return database.getUserDao().getuserProgress(userId);
    }

    public Single<Boolean> checkFirstStart(String userId) {
        return database.getUserDao().getUserById(userId).map(user -> user == null || user.savedMessages.isEmpty());
    }

    public Observable<Object> firstLoadData(User user, Reader open) {
        return Observable.fromCallable(() -> {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<GameMessage>>() {
            }.getType();
            return gson.<List<GameMessage>>fromJson(open, listType);
        }).flatMap((gameMessages) -> insertMessages(gameMessages).toObservable())
                .mergeWith(insertUserInformation(user));
    }

    public Single<List<HistoryMessage>> loadHistory(String userId) {
        return database.getUserDao().getUserById(userId).map(user -> user.savedMessages);
    }


}
