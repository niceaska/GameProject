package ru.niceaska.gameproject.data.repository;

import android.os.AsyncTask;

class UserProgressAsyncTask extends AsyncTask<Void, Void, Integer> {

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
