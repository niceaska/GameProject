package ru.niceaska.gameproject.data.repository;

import android.os.AsyncTask;

import ru.niceaska.gameproject.data.model.User;

class GameStartAsyncTask extends AsyncTask<String, Void, Boolean> {

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
