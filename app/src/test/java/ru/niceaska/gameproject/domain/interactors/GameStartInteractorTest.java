package ru.niceaska.gameproject.domain.interactors;

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
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.IDataRepository;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Мохнаткина Алина
 */
@RunWith(MockitoJUnitRunner.class)
public class GameStartInteractorTest {

    private final String NEG_CHOICE = "choice 2";
    private final String POS_CHOICE = "choice 1";

    @Mock
    private IDataRepository dataRepository;

    private GameStartInteractor interactor;

    @Before
    public void setUp() throws Exception {
        interactor = new GameStartInteractor(dataRepository);
    }

    @Test
    public void loadHistoryTest() {
        when(dataRepository.loadHistory(Mockito.anyString())).thenReturn(Single.just(createMessageItemList()));

        TestObserver<List<ListItem>> testObserver = interactor.loadHistory().test();

        testObserver.assertNoErrors()
                .assertValueCount(1)
                .assertValue(createExpectedListItems());

        testObserver.dispose();
    }

    @Test
    public void loadUserProgressTest() {
        when(dataRepository.loadUserProgress(Mockito.anyString())).thenReturn(Single.just(8));
        TestObserver<Integer> testObserver = interactor.loadUserProgress().test();

        testObserver.assertNoErrors()
                .assertValueCount(1)
                .assertValue(8);

        testObserver.dispose();
    }

    @Test
    public void isMessageAnimationEnabledTest() {
        when(dataRepository.isMessageAnimationEnabled()).thenReturn(true);
        assertTrue(interactor.isMessageAnimationEnabled());
    }

    private List<ListItem> createExpectedListItems() {
        List<ListItem> listItems = new ArrayList<>(createMessageItemList());
        listItems.add(((MessageItem) listItems.get(listItems.size() - 1)).getChoices());
        return listItems;
    }

    private List<MessageItem> createMessageItemList() {
        List<MessageItem> messageItems = new ArrayList<>();
        messageItems.add(getMessageItem());
        messageItems.add(getMessageItem_withChoices());
        return messageItems;
    }

    private MessageItem getMessageItem() {
        return new MessageItem(
                1, "Simple message", false, 2, null
        );
    }

    private MessageItem getMessageItem_withChoices() {
        return new MessageItem(
                1, "Simple message", false, 2, getMessageChoices()
        );
    }

    private MessageChoices getMessageChoices() {
        return new MessageChoices(
                "label 1", "label 2",
                POS_CHOICE, NEG_CHOICE,
                4, 5
        );
    }
}