package ru.niceaska.gameproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter {

    private List<Message> listMessages;

    public MessagesAdapter(List<Message> messgaes) {
        this.listMessages = messgaes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.dialog_list_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MessageViewHolder) holder).messageText.setText(listMessages.get(position).getMessage());
    }


    @Override
    public int getItemCount() {
        return listMessages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView messgaeTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            messgaeTime = itemView.findViewById(R.id.text_message_time);
        }
    }
}
