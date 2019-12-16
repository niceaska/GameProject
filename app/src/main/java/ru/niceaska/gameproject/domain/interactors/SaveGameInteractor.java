package ru.niceaska.gameproject.domain.interactors;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.IDataRepository;

/**
 * Класс-интерактор для сохранения игры
 */
public class SaveGameInteractor {

    private IDataRepository dataRepository;

    /**
     * Конструкторр интерактора
     *
     * @param dataRepository репозиторий предоставляющий данные
     */
    public SaveGameInteractor(@NonNull IDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * Сохраняет историю сообщений и прогресс игрока
     *
     * @param lastInd   индекс прогресса игрока
     * @param listItems лист элементов ресайклера
     * @return Completable действия
     */
    public Completable saveGame(int lastInd, @NonNull List<ListItem> listItems) {
        return dataRepository.saveUserData(lastInd, listItems);
    }

}
