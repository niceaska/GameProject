package ru.niceaska.gameproject;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

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
        if (oldItem instanceof Message && newItem instanceof Message) {
            return ((Message) oldItem).isGamer() == ((Message) newItem).isGamer()
                    && ((Message) oldItem).getMessage().equals(((Message) newItem).getMessage());
        } else if (oldItem instanceof Choices && newItem instanceof Choices) {
            return ((Choices) oldItem).getPositiveMessage().equals(((Choices) newItem).getPositiveMessage())
                    && ((Choices) oldItem).getNegativeMessage().equals(((Choices) newItem).getNegativeMessage());
        }
        return false;
    }
}
