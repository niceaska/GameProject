package ru.niceaska.gameproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataRepositoryTest {

    private static final String USER_ID = "userId";

    @Mock
    private SharedPreferences preferences;

    @Mock
    private Context context;

    @Mock
    private AppDatabase database;

    private DataRepository datatRepository;

    @Before
    public void setUp() throws Exception {
        datatRepository = new DataRepository(database, context, preferences);
    }

    @Test
    public void saveUserDataTest() throws Exception {
        UserDao userDao = Mockito.mock(UserDao.class);
        when(database.getUserDao()).thenReturn(userDao);
        doNothing().when(userDao).insert(Mockito.any(User.class));
        TestObserver testObserver = datatRepository.saveUserData(1, createMessageItems())
                .test();

        testObserver.assertValueCount(0)
                .assertNoErrors()
                .assertComplete();

        testObserver.dispose();
    }

    private List<ListItem> createMessageItems() {
        List<ListItem> listItems = new ArrayList<>();
        listItems.add(getMessageItem());
        listItems.add(new MessageChoices(
                "label 1", "label 2",
                "choice 1", "choice 2",
                4, 5
        ));
        return listItems;
    }

    private MessageItem getMessageItem() {
        return new MessageItem(
                1, "Simple message", false, 2, null
        );
    }

    @Test
    public void loadNewGameMessageTest() {
        GameMessgesDao gameMessgesDao = Mockito.mock(GameMessgesDao.class);
        when(database.getGameMessgeDao()).thenReturn(gameMessgesDao);
        when(gameMessgesDao.getById(Mockito.anyLong())).thenReturn(Single.just(getGameMessage()));

        final TestObserver<List<ListItem>> testObserver = datatRepository.loadNewGameMessage(2, createMessageItems()).test();
        testObserver.assertNoErrors()
                .assertValueCount(1);

        testObserver.dispose();
    }

    private GameMessage getGameMessage() {
        return new GameMessage(2, "test", false, 6,
                new Choices(
                        "pos label", "neg label", "pos choice", "neg choice", 1, 2
                )
        );
    }

    @Test
    public void loadUserProgressTest() {
        UserDao userDao = Mockito.mock(UserDao.class);
        when(database.getUserDao()).thenReturn(userDao);
        when(userDao.getuserProgress(Mockito.anyString())).thenReturn(Single.just(5));

        final TestObserver<Integer> testObserver = datatRepository.loadUserProgress(USER_ID).test();
        testObserver.assertNoErrors()
                .assertValueCount(1);
        testObserver.dispose();
    }

    @Test
    public void checkFirstStartTest() {
        UserDao userDao = Mockito.mock(UserDao.class);
        when(database.getUserDao()).thenReturn(userDao);
        when(userDao.getUserById(Mockito.anyString())).thenReturn(Single.just(getUser()));

        final TestObserver<Boolean> testObserver = datatRepository.checkFirstStart().test();
        testObserver.assertNoErrors()
                .assertValueCount(1)
                .assertValue(true);

        testObserver.dispose();
    }

    @Test
    public void checkFirstStartTest_notEmpty() {
        UserDao userDao = Mockito.mock(UserDao.class);
        when(database.getUserDao()).thenReturn(userDao);
        when(userDao.getUserById(Mockito.anyString())).thenReturn(Single.just(getUserNoHistory()));

        final TestObserver<Boolean> testObserver = datatRepository.checkFirstStart().test();
        testObserver.assertNoErrors()
                .assertValueCount(1)
                .assertValue(false);

        testObserver.dispose();
    }

    private User getUser() {
        return new User(
                new UserPojo(USER_ID, "test", 3), new ArrayList<>()
        );
    }

    private User getUserNoHistory() {
        List<HistoryMessage> historyMessages = new ArrayList<>();
        historyMessages.add(
                new HistoryMessage("1", USER_ID, "test", false, 7, null)
        );
        return new User(
                new UserPojo(USER_ID, "test", 3), historyMessages
        );
    }

    @Test
    public void createUserTest() {
        UserDao userDao = Mockito.mock(UserDao.class);
        when(database.getUserDao()).thenReturn(userDao);
        doNothing().when(userDao).insert(Mockito.any(User.class));
        TestObserver testObserver = datatRepository.createUser().test();

        testObserver.assertValueCount(0)
                .assertNoErrors()
                .assertComplete();

        testObserver.dispose();
    }

    @Test
    public void loadHistoryTest() {
        UserDao userDao = Mockito.mock(UserDao.class);
        when(database.getUserDao()).thenReturn(userDao);
        when(userDao.getUserById(Mockito.anyString())).thenReturn(Single.just(getUser()));

        final TestObserver<List<MessageItem>> testObserver = datatRepository.loadHistory(USER_ID).test();
        testObserver.assertNoErrors()
                .assertValueCount(1);

        testObserver.dispose();
    }


    private List<GameMessage> getGameMessgaeList() {
        List<GameMessage> gameMessages = new ArrayList<>();
        gameMessages.add(getGameMessage());
        gameMessages.add(getGameMessage());
        return gameMessages;
    }

    @Test
    public void refreshDatabaseTest() {

        UserDao userDao = Mockito.mock(UserDao.class);
        when(database.getUserDao()).thenReturn(userDao);
        doNothing().when(userDao).delete();
        TestObserver testObserver = datatRepository.refreshUserProgress()
                .test();

        testObserver.assertValueCount(0)
                .assertNoErrors()
                .assertComplete();

        testObserver.dispose();
    }

    @Test
    public void isNotificationEnabledTest() {
        when(context.getString(Mockito.anyInt())).thenReturn("");
        when(preferences.getBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(true);
        assertTrue(datatRepository.isNotificationEnabled());

    }

    @Test
    public void isMessageAnimationEnabledTest() {
        when(context.getString(Mockito.anyInt())).thenReturn("");
        when(preferences.getBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(true);
        assertTrue(datatRepository.isMessageAnimationEnabled());
    }

    @Test
    public void checkIfExistTest() {
        GameMessgesDao gameMessgesDao = Mockito.mock(GameMessgesDao.class);
        when(database.getGameMessgeDao()).thenReturn(gameMessgesDao);
        when(gameMessgesDao.getById(Mockito.anyLong())).thenReturn(Single.just(getGameMessage()));

        final TestObserver<Boolean> testObserverTrue = datatRepository.checkIfMessagesExist().test();
        testObserverTrue.assertNoErrors()
                .assertResult(true)
                .assertValueCount(1);
        testObserverTrue.dispose();
    }
}