package ru.niceaska.gameproject.data.repository;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.ListItem;

class GameLoopAsyncTask extends AsyncTask<List<ListItem>, Void, List<ListItem>> {

    private int nextIndex;
    private DataRepository dataRepository;
    private IOnMessageLoadListener listener;

    GameLoopAsyncTask(int nextIndex, IOnMessageLoadListener listener) {
        this.dataRepository = DataRepository.getInstance();
        this.nextIndex = nextIndex;
        this.listener = listener;
    }


    @Override
    protected List<ListItem> doInBackground(List<ListItem>... lists) {
        List<ListItem> listItems = new ArrayList<>(lists[0]);
        GameMessage gameMessage = dataRepository.getMessageById(nextIndex);
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
