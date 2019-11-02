package ru.niceaska.gameproject.presentation.presenter;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.view.IMainActivity;

public class MainActivityPresenter {
    WeakReference<IMainActivity> activityWeakReference;
    DataRepository dataRepository;

    public MainActivityPresenter(IMainActivity activity, DataRepository dataRepository) {
        this.activityWeakReference = new WeakReference<IMainActivity>(activity);
        this.dataRepository = dataRepository;
    }

    public void gameRun(boolean isSavedState) {
        if (isSavedState) {
            activityWeakReference.get().startGame();
            return;
        }
        new GameRunAsyncTask(activityWeakReference).execute("1");
    }

    static class GameRunAsyncTask extends AsyncTask<String, Void, Void> {

        private boolean isFirstStart;
        private DataRepository dataRepository;
        private WeakReference<IMainActivity> activityWeakReference;

        public GameRunAsyncTask(WeakReference<IMainActivity> activityWeakReference) {
            this.dataRepository = new DataRepository();
            this.activityWeakReference = activityWeakReference;
        }

        @Override
        protected Void doInBackground(String... strings) {
            User user = dataRepository.getUser(strings[0]);
            isFirstStart = user == null || user.savedMessages.isEmpty();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isFirstStart) {
                activityWeakReference.get().showStartAppFragment();
            } else {
                activityWeakReference.get().startGame();
            }
        }
    }
}
