package ru.niceaska.gameproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.domain.model.MessageItem;

public class DataRepository implements IDataRepository {

    /**
     * id игрока
     */
    public static final String USER_ID = "1";

    /**
     * имя игрока
     */
    public static final String USER_NAME = "test";
    private static final String FILE_NAME = "scenario.json";
    private static final int START_MESSAGE_ID = 1;

    private AppDatabase database;
    private Context context;
    private MessageConverter messageConverter = new MessageConverter();
    private SaveMessageConverter saveMessageConverter = new SaveMessageConverter();
    private InputStreamReader reader;
    private SharedPreferences preferences;

    public DataRepository(@NonNull AppDatabase database,
                          @NonNull Context context,
                          @NonNull SharedPreferences preferences) {
        this.database = database;
        this.context = context;
        this.preferences = preferences;
    }

    private Completable insertUserInformation(@NonNull User user) {
        return Completable.fromRunnable(() -> database.getUserDao().insert(user));
    }

    private Completable insertMessages(@NonNull List<GameMessage> messageList) {
        return database.getGameMessgeDao().insertMessge(messageList);
    }

    private Single<GameMessage> getMessageById(long id) {
        return database.getGameMessgeDao().getById(id);

    }

    /**
     * Сохранить информацию о прогрессе игрока
     *
     * @param lastIndex    прогресс игрока
     * @param messageItems сообщения которые будут добавлены в историю
     * @return Completable совершенного действия
     */
    @Override
    public Completable saveUserData(int lastIndex, @NonNull List<ListItem> messageItems) {
        UserPojo userPojo = new UserPojo(USER_ID, USER_NAME, lastIndex);
        User user = new User(userPojo, saveMessageConverter.convertToHistory(messageItems));
        return insertUserInformation(user);
    }

    /**
     * Загружает следующее сообщение и формирует список для отображения
     *
     * @param nextIndex индекс следующего сообщения
     * @param listItems список сообщений
     * @return сингл совершенного действия
     */
    @Override
    public Single<List<ListItem>> loadNewGameMessage(int nextIndex, @NonNull List<ListItem> listItems) {
        return getMessageById(nextIndex)
                .map(gameMessage -> messageConverter.convertFromGameMessage(gameMessage))
                .map(gameMessage -> {
                    List<ListItem> items = new ArrayList<>(listItems);
                    if (gameMessage != null) {
                        items.add(gameMessage);
                        if (gameMessage.getChoices() != null) {
                            items.add(gameMessage.getChoices());
                        }
                    }
                    return items;
                });
    }

    /**
     * Загружает прогресс игрока
     *
     * @param userId ид игрока
     * @return сингл совершенного действия
     */
    @Override
    public Single<Integer> loadUserProgress(@NonNull String userId) {
        return database.getUserDao().getuserProgress(userId);
    }

    /**
     * Проверяет первый ли запуск приложения
     *
     * @return сингл проверки
     */
    @Override
    public Single<Boolean> checkFirstStart() {
        return database.getUserDao().getUserById(USER_ID)
                .map(user -> user == null || user.savedMessages.isEmpty());
    }

    /**
     * Загружает сценарий из ассетов и заполняет базу данных
     *
     * @return Observable
     */
    @Override
    public Observable<List> firstLoadData() {
        return Observable.fromCallable(() -> {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(FILE_NAME);
            reader = new InputStreamReader(is);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<GameMessage>>() {
            }.getType();
            return gson.<List<GameMessage>>fromJson(reader, listType);
        }).doFinally(() -> reader.close())
                .flatMap((gameMessages) -> insertMessages(gameMessages).toObservable());

    }

    /**
     * Создает пользователя и загружает его в дб
     *
     * @return Completable
     */
    @Override
    public Completable createUser() {
        User user = new User(new UserPojo(USER_ID, USER_NAME, 0), new ArrayList<HistoryMessage>());
        return insertUserInformation(user);
    }

    /**
     * Загружает историю сообщений игрока
     *
     * @param userId ид игрока
     * @return сингл
     */
    @Override
    public Single<List<MessageItem>> loadHistory(@NonNull String userId) {
        return database.getUserDao().getUserById(userId)
                .map(user -> messageConverter.convertFromHistory(user.savedMessages));
    }

    /**
     * Удаляет прогресс игрока при начале новой игры
     *
     * @return Completable
     */
    @Override
    public Completable refreshUserProgress() {
        return Completable.fromAction(() -> database.getUserDao().delete());
    }

    /**
     * Проверяет включены ли уведомления
     *
     * @return булевое значение - true включены, false нет
     */
    @Override
    public boolean isNotificationEnabled() {
        return preferences.getBoolean(
                context.getString(R.string.pref_notification_key), true
        );
    }

    /**
     * Проверяет включена ли анимация для сообщений
     *
     * @return булевое значение - true включена, false нет
     */
    @Override
    public boolean isMessageAnimationEnabled() {
        return preferences.getBoolean(
                context.getString(R.string.pref_enable_anim_key), true
        );
    }

    /**
     * Проверяет загружены ли уже сообщения в базу данных
     *
     * @return сингл булевого значения - true загружены false нет
     */
    @Override
    public Single<Boolean> checkIfMessagesExist() {
        return database.getGameMessgeDao().getById(START_MESSAGE_ID).map(gameMessage -> gameMessage != null);
    }
}
