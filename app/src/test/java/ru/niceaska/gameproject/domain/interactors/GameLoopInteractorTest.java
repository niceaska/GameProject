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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Мохнаткина Алина
 */
@RunWith(MockitoJUnitRunner.class)
public class GameLoopInteractorTest {

    private final int NEXT_MESSAGE = 2;
    private final String NEG_CHOICE = "choice 2";
    private final String POS_CHOICE = "choice 1";

    @Mock
    private IDataRepository dataRepository;

    private GameLoopInteractor interactor;

    @Before
    public void setUp() throws Exception {
        interactor = new GameLoopInteractor(dataRepository);
    }

    @Test
    public void getNextIndexTest() {
        assertEquals(NEXT_MESSAGE, interactor.getNextIndex(createMessageItems()));
        assertEquals(1, interactor.getNextIndex(createMessageItems_withChoices()));
        assertEquals(1, interactor.getNextIndex(new ArrayList<>()));
    }

    @Test
    public void loadNewMessage() {
        when(dataRepository.loadNewGameMessage(Mockito.anyInt(), Mockito.anyList())).thenReturn(Single.just(createMessageItems_withChoices()));

        TestObserver testObserver = interactor.loadNewMessage(2, createMessageItems()).test();
        testObserver.assertNoErrors()
                .assertValueCount(1);

        testObserver.dispose();
    }

    @Test
    public void updateMessageList() {
        MessageChoices messageChoices = getMessageChoices();
        List<ListItem> listItemsNegExpected = createMessageItems_withChoices();
        List<ListItem> listItemsPosExpected = createMessageItems_withChoices();
        listItemsNegExpected.set(
                listItemsNegExpected.size() - 1, new MessageItem(
                        listItemsNegExpected.size() - 1, messageChoices.getNegativeChoice(),
                        true, messageChoices.getNegativeMessageAnswer(), null
                )
        );
        listItemsPosExpected.set(
                listItemsNegExpected.size() - 1, new MessageItem(
                        listItemsPosExpected.size() - 1, messageChoices.getPositiveChoice(),
                        true, messageChoices.getPositiveMessageAnswer(), null
                )
        );
        assertThat(interactor.updateMessageList(messageChoices, createMessageItems_withChoices(), true), is(listItemsNegExpected));
        assertThat(interactor.updateMessageList(messageChoices, createMessageItems_withChoices(), false), is(listItemsPosExpected));

    }

    private List<ListItem> createMessageItems_withChoices() {
        List<ListItem> listItems = new ArrayList<>();
        listItems.add(getMessageItem());
        listItems.add(getMessageChoices());
        return listItems;
    }

    private MessageChoices getMessageChoices() {
        return new MessageChoices(
                "label 1", "label 2",
                POS_CHOICE, NEG_CHOICE,
                4, 5
        );
    }

    private List<ListItem> createMessageItems() {
        List<ListItem> listItems = new ArrayList<>();
        listItems.add(getMessageItem());
        listItems.add(getMessageItem());
        return listItems;
    }

    private MessageItem getMessageItem() {
        return new MessageItem(
                1, "Simple message", false, NEXT_MESSAGE, null
        );
    }
}