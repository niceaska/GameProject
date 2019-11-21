package ru.niceaska.gameproject.domain;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.domain.model.MessageItem;

public class GameStartInteractor {

    private DataRepository dataRepository;

    public GameStartInteractor(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Single<List<ListItem>> loadHistory() {
        return dataRepository.loadHistory("1")
                .map(historyMessages -> {
                    List<ListItem> messageList = new ArrayList<>(historyMessages);
                    if (!historyMessages.isEmpty()) {
                        ListItem lastItem = messageList.get(messageList.size() - 1);
                        if (((MessageItem) lastItem).getChoices() != null) {
                            messageList.add(((MessageItem) lastItem).getChoices());
                        }
                    }
                    return messageList;
                });
    }

    public Single<Integer> loadUserProgress() {
        return dataRepository.loadUserProgress("1");
    }

}
