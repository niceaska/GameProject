package ru.niceaska.gameproject.data.repository;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

    private void insertUserInformation(User user) {
        database.getUserDao().insert(user);
    }

    private void insertMessages(List<GameMessage> messageList) {
        database.getGameMessgeDao().insertMessge(messageList);
    }

    public void insertMessages(GameMessage... messages) {
        database.getGameMessgeDao().insertMessge(messages);
    }

    private int getUserProgress(String userId) {
        return database.getUserDao().getuserProgress(userId);
    }

    private GameMessage getMessageById(long id) {
        return database.getGameMessgeDao().getById(id);
    }

    public List<HistoryMessage> getHistory(String userId) {
        User user = getUser(userId);
        return user.savedMessages;
    }

    private User getUser(String userId) {
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

    static class UserProgressAsyncTask extends AsyncTask<Void, Void, Integer> {

        private DataRepository dataRepository;
        private IOnUserProgressLoadListener lisstener;

        UserProgressAsyncTask(IOnUserProgressLoadListener lisstener) {
            this.lisstener = lisstener;
            this.dataRepository = DataRepository.getInstance();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return dataRepository.getUserProgress("1");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            lisstener.onLoadData(integer);
        }

    }

    static class GameLoopAsyncTask extends AsyncTask<List<ListItem>, Void, List<ListItem>> {

        private int lastIndex;
        private DataRepository dataRepository;
        private IOnMessageLoadListener listener;

        GameLoopAsyncTask(int lastIndex, IOnMessageLoadListener listener) {
            this.dataRepository = DataRepository.getInstance();
            this.lastIndex = lastIndex;
            this.listener = listener;
        }


        @Override
        protected List<ListItem> doInBackground(List<ListItem>... lists) {
            List<ListItem> listItems = new ArrayList<>(lists[0]);
            GameMessage gameMessage = dataRepository.getMessageById(lastIndex + 1);
            if (gameMessage != null) {
                listItems.add(gameMessage);
                if (gameMessage.getChoices() != null) {
                    listItems.add(gameMessage.getChoices());
                }
            }
            return listItems;
        }

        @Override
        protected void onPostExecute(List<ListItem> listItems) {
            listener.onDataLoaded(listItems);
        }
    }

    static class GameStartAsyncTask extends AsyncTask<String, Void, Boolean> {

        private DataRepository dataRepository;
        private IOnGameRunCheckListener listener;

        public GameStartAsyncTask(IOnGameRunCheckListener listener) {
            this.listener = listener;
            this.dataRepository = DataRepository.getInstance();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            User user = dataRepository.getUser(strings[0]);
            return user == null || user.savedMessages.isEmpty();
        }

        @Override
        protected void onPostExecute(Boolean isFirstStart) {
            listener.onChecked(isFirstStart);
        }
    }

    static class FirstLoadData extends AsyncTask<User, Void, Void> {
        private WeakReference<Activity> activityWeakReference;
        private IOnFirstLoadDataListener listener;
        private DataRepository dataRepository;

        FirstLoadData(Activity activiy,
                      IOnFirstLoadDataListener listener) {
            this.activityWeakReference = new WeakReference<>(activiy);
            this.listener = listener;
            this.dataRepository = DataRepository.getInstance();
        }

        @Override
        protected Void doInBackground(User... users) {
            if (activityWeakReference.get() == null) return null;

            AssetManager assetManager = activityWeakReference.get().getAssets();
            dataRepository.insertUserInformation(users[0]);
            try (Reader open = new InputStreamReader(assetManager.open("scenario.json"));) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<GameMessage>>() {
                }.getType();
                List<GameMessage> gameMessages = gson.fromJson(open, listType);
                dataRepository.insertMessages(gameMessages);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listener.onFirsLoad();
        }
    }
}
