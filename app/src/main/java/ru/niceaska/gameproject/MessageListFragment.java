package ru.niceaska.gameproject;

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
import java.util.Arrays;
import java.util.List;

public class MessageListFragment extends Fragment {

    private static final String LIST_STATE_KEY = "list state";
    private RecyclerView recyclerView;
    private Parcelable listState;
    private LinearLayoutManager layoutManager;
    private MessagesAdapter messagesAdapter = new MessagesAdapter();


    private List<ListItem> meaageTest = Arrays.asList(
            new Message("Привет анон я только недавно сюда приехал и заметил странное. ",
                    0,
                    false
            ),
            new Message("Кажется по дороге кто-то идет",
                    0,
                    false
            ),
            new Message("Похоже это какая то старушка. Что делать?",
                    0,
                    false
            ),
            new Choices("Помочь", "Помоги ей перейти дорогу",
                    "Нафиг бабушек", "Игнорировать",
                    new Message("хорошо я помогу", 0, false),
                    new Message("Да и хрен с ней", 0, false))
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_list_fragment, container, false);
        initRecycler(v);
        return v;
    }

    private void initRecycler(View v) {
        recyclerView = v.findViewById(R.id.dialog_view);
        layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        messagesAdapter.setListObj(new ArrayList<ListItem>(meaageTest));
        messagesAdapter.setListener(new ChoiceButtonsHolder() {
            @Override
            public void onPositiveChoice() {
                List<ListItem> listItems = messagesAdapter.getListObj();
                List<ListItem> newList = new ArrayList<ListItem>(listItems);
                if (!newList.isEmpty()) {
                    Object item = newList.get(newList.size() - 1);
                    if (item instanceof Choices) {
                        newList.set(newList.size() - 1, new Message(((Choices) item).getPositiveChoice(), 0, true));
                        newList.add(((Choices) item).getPositiveMessage());
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
                        newList.set(newList.size() - 1, new Message(((Choices) item).getNegativeChoice(), 0, true));
                        newList.add(((Choices) item).getNegativeMessage());
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
