package ru.niceaska.gameproject.presentation.view;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.model.MessageItem;

public class MessagesDiffCallback extends DiffUtil.Callback {

    private final List<ListItem> oldList;
    private final List<ListItem> newList;


    public MessagesDiffCallback(List<ListItem> oldList, List<ListItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }


    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldItem = oldList.get(oldItemPosition);
        Object newItem = newList.get(newItemPosition);
        if (oldItem instanceof GameMessage && newItem instanceof GameMessage) {
            return ((GameMessage) oldItem).getId() == ((GameMessage) newItem).getId();
        } else if (oldItem instanceof HistoryMessage && newItem instanceof HistoryMessage)
            return ((HistoryMessage) oldItem).getId().equals(((HistoryMessage) newItem).getId());
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldItem = oldList.get(oldItemPosition);
        Object newItem = newList.get(newItemPosition);
        if (oldItem instanceof MessageItem && newItem instanceof MessageItem) {
            return ((MessageItem) oldItem).isGamer() == ((MessageItem) newItem).isGamer()
                    && ((MessageItem) oldItem).getMessage().equals(((MessageItem) newItem).getMessage());
        } else if (oldItem instanceof Choices && newItem instanceof Choices) {
            return ((Choices) oldItem).getPositiveMessageAnswer().equals(((Choices) newItem).getPositiveMessageAnswer())
                    && ((Choices) oldItem).getNegativeMessageAnswer().equals(((Choices) newItem).getNegativeMessageAnswer());
        }
        return false;
    }
}
