package ru.niceaska.gameproject;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LIST_STATE_KEY = "list state";
    private RecyclerView recyclerView;
    private Parcelable listState;
    private LinearLayoutManager layoutManager;
    final MessagesAdapter messagesAdapter = new MessagesAdapter();


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.dialog_view);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        messagesAdapter.setListObj(meaageTest);
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


    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        listState = layoutManager.onSaveInstanceState();

        state.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) messagesAdapter.getListObj());
        state.putParcelable(LIST_STATE_KEY, listState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null) {
            listState = state.getParcelable(LIST_STATE_KEY);
            messagesAdapter.updateList(state.<ListItem>getParcelableArrayList("key"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }
    }
}
