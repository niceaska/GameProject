package ru.niceaska.gameproject.domain;

import java.util.List;

import io.reactivex.Completable;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.repository.DataRepository;

public class SaveGameInteractor {

    private DataRepository dataRepository;

    public SaveGameInteractor(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Completable saveGame(int lastInd, List<ListItem> listItems) {
        return dataRepository.saveUserData(lastInd, listItems);
    }

}
