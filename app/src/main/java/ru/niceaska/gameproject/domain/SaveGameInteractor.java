package ru.niceaska.gameproject.domain;

import java.util.List;

import io.reactivex.Completable;
import ru.niceaska.gameproject.data.model.ListItem;

public class SaveGameInteractor {

    private IDataRepository dataRepository;

    public SaveGameInteractor(IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Completable saveGame(int lastInd, List<ListItem> listItems) {
        return dataRepository.saveUserData(lastInd, listItems);
    }

}
