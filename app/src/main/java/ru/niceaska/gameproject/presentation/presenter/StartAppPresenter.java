package ru.niceaska.gameproject.presentation.presenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.view.GameStartFragment;

public class StartAppPresenter {
    WeakReference<GameStartFragment> gameStartFragmentWeakReference;
    DataRepository dataRepository;
    private List<GameMessage> meaageTest = Arrays.asList(
            new GameMessage("Привет анон я только недавно сюда приехал и заметил странное. ",
                    0,
                    false,
                    null
            ),
            new GameMessage("Кажется по дороге кто-то идет",
                    0,
                    false,
                    null),
            new GameMessage("Похоже это какая то старушка. Что делать?",
                    0,
                    false,
                    new Choices("Помочь", "Игнорировать",
                            "Помоги ей перейти дорогу", "Нафиг бабушек",
                            "хорошо я помогу", "Да и хрен с ней")
            ));

    public StartAppPresenter(GameStartFragment gameStartFragmentWeakReference, DataRepository dataRepository) {
        this.gameStartFragmentWeakReference = new WeakReference<>(gameStartFragmentWeakReference);
        this.dataRepository = dataRepository;
    }

    public void loadData() {
        dataRepository.insertUserInformation(new User(new UserPojo("1", "Test", 0), new ArrayList<HistoryMessage>()));
        dataRepository.insertMessages(meaageTest);
        gameStartFragmentWeakReference.get().beginNewGame();
    }

}
