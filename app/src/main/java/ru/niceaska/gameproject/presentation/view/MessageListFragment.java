package ru.niceaska.gameproject.presentation.view;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.gameproject.MyApp;
import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.repository.AppDatabase;

public class MessageListFragment extends Fragment {

    private static final String LIST_STATE_KEY = "listState";
    private RecyclerView recyclerView;
    private Parcelable listState;
    AppDatabase database;
    private LinearLayoutManager layoutManager;
    private MessagesAdapter messagesAdapter = new MessagesAdapter();




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_list_fragment, container, false);
        database = MyApp.getInstance().getDatabase();
        initRecycler(v);

        return v;
    }

    private void initRecycler(View v) {
        recyclerView = v.findViewById(R.id.dialog_view);
        layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        List<GameMessage> messges = database.getGameMessgeDao().getAllMessges();
        List<ListItem> messageList = new ArrayList<>();
        for (GameMessage message : messges) {
            messageList.add(message);
            if (message.getChoices() != null) {
                messageList.add(message.getChoices());
            }
        }
        messagesAdapter.setListObj(messageList);
        messagesAdapter.setListener(new ChoiceButtonsHolder() {
            @Override
            public void onPositiveChoice() {
                List<ListItem> listItems = messagesAdapter.getListObj();
                List<ListItem> newList = new ArrayList<ListItem>(listItems);
                if (!newList.isEmpty()) {
                    Object item = newList.get(newList.size() - 1);
                    if (item instanceof Choices) {
                        newList.set(newList.size() - 1,
                                new GameMessage(((Choices) item).getPositiveChoice(), 0, true, null)
                        );
                        newList.add(
                                new GameMessage(((Choices) item).getPositiveMessageAnswer(), 0, false, null)
                        );
                    }
                    messagesAdapter.updateList(newList);
                }
            }

            @Override
            public void onNegativeChoice() {
                List<ListItem> listItems = messagesAdapter.getListObj();
                List<ListItem> newList = new ArrayList<ListItem>(listItems);
                if (!newList.isEmpty()) {
                    Object item = newList.get(newList.size() - 1);
                    if (item instanceof Choices) {
                        newList.set(newList.size() - 1,
                                new GameMessage(((Choices) item).getNegativeChoice(), 0, true, null)
                        );
                        newList.add(
                                new GameMessage(((Choices) item).getNegativeMessageAnswer(), 0, false, null)
                        );
                    }
                    messagesAdapter.updateList(newList);
                }
            }
        });
        recyclerView.setAdapter(messagesAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        listState = layoutManager.onSaveInstanceState();
        state.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) messagesAdapter.getListObj());
        state.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            messagesAdapter.updateList(savedInstanceState.<ListItem>getParcelableArrayList("key"));
        }
        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }
    }


}
