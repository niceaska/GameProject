package ru.niceaska.gameproject.domain;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;

public class GameLoopInteractor {

    private IDataRepository dataRepository;

    public GameLoopInteractor(IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public int getNextIndex(List<ListItem> listItems) {
        int nextIndex = 1;
        if (!listItems.isEmpty() && listItems.get(listItems.size() - 1) instanceof MessageItem) {
            nextIndex = ((MessageItem) listItems.get(listItems.size() - 1)).getNextMessage();
        }
        return nextIndex;
    }

    public Single<List<ListItem>> loadNewMessage(int nextIndex, List<ListItem> listItems) {
        return dataRepository.loadNewGameMessage(nextIndex, listItems);
    }

    public List<ListItem> updateMessageList(MessageChoices messageChoices,
                                            List<ListItem> listItems, boolean isNegative) {
        final List<ListItem> newList = new ArrayList<ListItem>(listItems);
        int answer = isNegative ? messageChoices.getNegativeMessageAnswer() : messageChoices.getPositiveMessageAnswer();
        newList.set(newList.size() - 1,
                new MessageItem(newList.size() - 1,
                        messageChoices.getNegativeChoice(), true,
                        answer, null));
        return newList;
    }
}
