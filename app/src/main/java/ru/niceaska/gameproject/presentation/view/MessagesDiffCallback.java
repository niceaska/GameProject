package ru.niceaska.gameproject.presentation.view;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.ListItem;

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
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldItem = oldList.get(oldItemPosition);
        Object newItem = newList.get(newItemPosition);
        if (oldItem instanceof GameMessage && newItem instanceof GameMessage) {
            return ((GameMessage) oldItem).isGamer() == ((GameMessage) newItem).isGamer()
                    && ((GameMessage) oldItem).getMessage().equals(((GameMessage) newItem).getMessage());
        } else if (oldItem instanceof Choices && newItem instanceof Choices) {
            return ((Choices) oldItem).getPositiveMessageAnswer().equals(((Choices) newItem).getPositiveMessageAnswer())
                    && ((Choices) oldItem).getNegativeMessageAnswer().equals(((Choices) newItem).getNegativeMessageAnswer());
        }
        return false;
    }
}
