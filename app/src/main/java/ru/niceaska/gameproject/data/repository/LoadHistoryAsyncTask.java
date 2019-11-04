package ru.niceaska.gameproject.data.repository;

import android.os.AsyncTask;

import java.util.List;

import ru.niceaska.gameproject.data.model.HistoryMessage;

public class LoadHistoryAsyncTask extends AsyncTask<String, Void, List<HistoryMessage>> {
    private IOnHistoryUpdatedListener listener;
    private DataRepository dataRepository;

    public LoadHistoryAsyncTask(IOnHistoryUpdatedListener listener) {
        this.listener = listener;
        this.dataRepository = DataRepository.getInstance();
    }

    @Override
    protected List<HistoryMessage> doInBackground(String... strings) {
        List<HistoryMessage> messages = dataRepository.getHistory(strings[0]);
        return messages;
    }

    @Override
    protected void onPostExecute(List<HistoryMessage> historyMessages) {
        listener.onHistoryLoad(historyMessages);
    }
}
