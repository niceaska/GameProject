package ru.niceaska.gameproject.presentation.presenter;

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
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.view.GameStartFragment;

public class StartAppPresenter {
    WeakReference<GameStartFragment> gameStartFragmentWeakReference;
    DataRepository dataRepository;

/*    private List<GameMessage> meaageTest = Arrays.asList(
            new GameMessage("Привет анон я только недавно сюда приехал и заметил странное. ",
                    0,
                    false,
                    null
            ),
            new GameMessage("Кажется по дороге кто-то идет",
                    0,
                    false,
                    null),
            new GameMessage("Похоже это какая то старушка. Что делать?",
                    0,
                    false,
                    new Choices("Помочь", "Игнорировать",
                            "Помоги ей перейти дорогу", "Нафиг бабушек",
                            "хорошо я помогу", "Да и хрен с ней")
            ));*/

    public StartAppPresenter(GameStartFragment gameStartFragmentWeakReference, DataRepository dataRepository) {
        this.gameStartFragmentWeakReference = new WeakReference<>(gameStartFragmentWeakReference);
        this.dataRepository = dataRepository;
    }

    public void loadData() {
        User user = new User(new UserPojo("1", "test", 0), new ArrayList<HistoryMessage>());
        new FirstLoadData(gameStartFragmentWeakReference, dataRepository).execute(user);
    }

    static class FirstLoadData extends AsyncTask<User, Void, Void> {
        WeakReference<GameStartFragment> gameStartFragmentWeakReference;
        WeakReference<DataRepository> dataRepository;

        public FirstLoadData(WeakReference<GameStartFragment> gameStartFragmentWeakReference, DataRepository dataRepository) {
            this.gameStartFragmentWeakReference = gameStartFragmentWeakReference;
            this.dataRepository = new WeakReference<>(dataRepository);
        }

        @Override
        protected Void doInBackground(User... users) {
            Activity activity = gameStartFragmentWeakReference.get().getActivity();

            if (activity == null) return null;

            AssetManager assetManager = activity.getAssets();
            dataRepository.get().insertUserInformation(users[0]);
            try (Reader open = new InputStreamReader(assetManager.open("scenario.json"));) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<GameMessage>>() {
                }.getType();
                List<GameMessage> gameMessages = gson.fromJson(open, listType);
                dataRepository.get().insertMessages(gameMessages);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            gameStartFragmentWeakReference.get().beginNewGame();
        }
    }
}
