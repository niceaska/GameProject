package ru.niceaska.gameproject.presentation.view;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.model.MessageItem;

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
        if (oldItem instanceof MessageItem && newItem instanceof MessageItem) {
            return ((MessageItem) oldItem).getId() == ((MessageItem) newItem).getId()
                    && ((MessageItem) oldItem).isGamer() == ((MessageItem) newItem).isGamer();
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

}
