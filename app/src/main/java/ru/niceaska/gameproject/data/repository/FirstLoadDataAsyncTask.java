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

import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.User;

class FirstLoadDataAsyncTask extends AsyncTask<User, Void, Void> {
    private static final String FILE_NAME = "scenario.json";
    private WeakReference<Activity> activityWeakReference;
    private IOnFirstLoadDataListener listener;
    private DataRepository dataRepository;

    FirstLoadDataAsyncTask(Activity activiy,
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
        try (Reader open = new InputStreamReader(assetManager.open(FILE_NAME));) {
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
