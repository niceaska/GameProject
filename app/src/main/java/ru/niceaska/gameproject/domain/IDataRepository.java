package ru.niceaska.gameproject.domain;

import java.io.Reader;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.model.MessageItem;

public interface IDataRepository {
    Completable saveUserData(int lastIndex, List<ListItem> messageItems);

    Single<List<ListItem>> loadNewGameMessage(int nextIndex, List<ListItem> listItems);

    Single<Integer> loadUserProgress(String userId);

    Single<Boolean> checkFirstStart(String userId);

    Observable<Object> firstLoadData(Reader open);

    Single<List<MessageItem>> loadHistory(String userId);
}
