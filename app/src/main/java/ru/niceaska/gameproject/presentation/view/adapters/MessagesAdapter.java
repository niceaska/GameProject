package ru.niceaska.gameproject.presentation.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;
import ru.niceaska.gameproject.presentation.view.ChoiceButtonsHolder;

/**
 * Адаптер для списка сообщений
 */
public class MessagesAdapter extends RecyclerView.Adapter {

    private List<ListItem> listObj = new ArrayList<>();
    private ChoiceButtonsHolder listener;

    private static final int MESSAGE_ID = 0;
    private static final int GAMER_ID = 1;
    private static final int CHOICE_ID = 2;


    @Override
    public int getItemViewType(int position) {
        Object item = listObj.get(position);
        if (item instanceof MessageItem) {
            return ((MessageItem) item).isGamer() ? GAMER_ID : MESSAGE_ID;
        } else if (item instanceof MessageChoices) {
            return CHOICE_ID;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_ID) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate( R.layout.dialog_list_item, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == CHOICE_ID) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.button_choice_item, parent, false);
            return new ButtonChoicesViewHolder(view, listener);
        } else if (viewType == GAMER_ID) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dialog_gamer_list_item, parent, false);
            return new MessageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageViewHolder) {
            ((MessageViewHolder) holder).onBind(((MessageItem) listObj.get(position)).getMessage());
        } else if (holder instanceof  ButtonChoicesViewHolder) {
            ((ButtonChoicesViewHolder) holder).onBind((MessageChoices) listObj.get(position));
        } else {
            throw new IllegalArgumentException();
        }
    }


    @Override
    public int getItemCount() {
        return listObj.size();
    }

    public void updateList(List<ListItem> newList) {
        MessagesDiffCallback messagesDiffCallback = new MessagesDiffCallback(listObj, newList);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(messagesDiffCallback);
        setListObj(newList);
        productDiffResult.dispatchUpdatesTo(this);
    }


    public List<ListItem> getListObj() {
        return listObj;
    }

    public void setListObj(List<ListItem> listObj) {
        this.listObj = listObj;
    }


    public void setListener(ChoiceButtonsHolder listener) {
        this.listener = listener;
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView messageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
        }

        void onBind(String text) {
            messageText.setText(text);
        }
    }


    public static class ButtonChoicesViewHolder extends RecyclerView.ViewHolder {

        private Button buttonLeft;
        private Button buttonRight;
        private ChoiceButtonsHolder listener;

        ButtonChoicesViewHolder(@NonNull View itemView, ChoiceButtonsHolder listener) {
            super(itemView);
            buttonLeft = itemView.findViewById(R.id.button_left_choice);
            buttonRight = itemView.findViewById(R.id.button_right_choice);
            this.listener = listener;
        }

        void onBind(final MessageChoices choice) {
            buttonLeft.setText(choice.getNegativeChoiceLabel());
            buttonLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNegativeChoice();
                }
            });
            buttonRight.setText(choice.getPositiveChoiceLabel());
            buttonRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPositiveChoice();
                }
            });
        }
    }
}
