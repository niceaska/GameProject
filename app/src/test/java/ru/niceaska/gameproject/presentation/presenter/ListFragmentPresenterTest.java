package ru.niceaska.gameproject.presentation.presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.interactors.GameLoopInteractor;
import ru.niceaska.gameproject.domain.interactors.GameStartInteractor;
import ru.niceaska.gameproject.domain.interactors.SaveGameInteractor;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;
import ru.niceaska.gameproject.presentation.view.MessageListView;
import ru.niceaska.gameproject.rx.IRxSchedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Мохнаткина Алина
 */
@RunWith(MockitoJUnitRunner.class)
public class ListFragmentPresenterTest {

    @Rule
    public TestSchedulerRule schedulerRule = new TestSchedulerRule();

    @Mock
    private GameStartInteractor gameStartInteractor;

    @Mock
    private GameLoopInteractor gameLoopInteractor;

    @Mock
    private SaveGameInteractor saveGameInteractor;

    @Mock
    private MessageListView listView;

    private TestScheduler testScheduler = schedulerRule.getTestScheduler();

    private ListFragmentPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new ListFragmentPresenter(gameStartInteractor, gameLoopInteractor, saveGameInteractor, rxSchedulersTest);
        presenter.attachView(listView);
    }

    @After
    public void tearDown() throws Exception {
        presenter.clearDisposable();
        presenter.detachView();
    }

    @Test
    public void onGameStart_Choices_AnimationOn() {
        when(gameStartInteractor.loadHistory()).thenReturn(Single.just(createMessageList(true)));
        when(gameStartInteractor.isMessageAnimationEnabled()).thenReturn(true);

        presenter.onGameStart();
        testScheduler.triggerActions();
        InOrder inOrder = Mockito.inOrder(listView);
        inOrder.verify(listView).setUpdateAnimator(true);
        inOrder.verify(listView).updateMessageList(createMessageList(true));
        inOrder.verify(listView).scrollToBottom();

        verifyNoMoreInteractions(listView);
    }

    @Test
    public void onGameStart_Choices_AnimationOff() {
        when(gameStartInteractor.loadHistory()).thenReturn(Single.just(createMessageList(true)));
        when(gameStartInteractor.isMessageAnimationEnabled()).thenReturn(false);

        presenter.onGameStart();
        testScheduler.triggerActions();
        InOrder inOrder = Mockito.inOrder(listView);
        inOrder.verify(listView).setUpdateAnimator(false);
        inOrder.verify(listView).updateMessageList(createMessageList(true));
        inOrder.verify(listView).scrollToBottom();

        verifyNoMoreInteractions(listView);
    }

    @Test
    public void onGameStart_NoChoices_AnimationOff() throws InterruptedException {
        when(gameStartInteractor.loadHistory()).thenReturn(Single.just(createMessageList(false)));
        when(gameStartInteractor.isMessageAnimationEnabled()).thenReturn(false);
        when(gameStartInteractor.loadUserProgress()).thenReturn(Single.just(4));
        when(gameLoopInteractor.loadNewMessage(Mockito.anyInt(), Mockito.anyList())).thenReturn(Single.just(createMessageList(false)));
        when(gameLoopInteractor.getNextIndex(Mockito.anyList())).thenReturn(2);
        presenter.onGameStart();
        testScheduler.triggerActions();
        InOrder inOrder = Mockito.inOrder(listView);
        inOrder.verify(listView).setUpdateAnimator(false);
        inOrder.verify(listView).showUserTyping();
        inOrder.verify(listView).showAnimation();
        testScheduler.advanceTimeBy(4, TimeUnit.SECONDS);
        testScheduler.triggerActions();
        inOrder.verify(listView).updateMessageList(Mockito.anyList());
        inOrder.verify(listView).scrollToBottom();
        inOrder.verify(listView).hideUserTyping();
        inOrder.verify(listView).clearAnimation();
    }

    @Test
    public void onGameStart_NoChoices_AnimationOff_Error() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        when(gameStartInteractor.loadHistory()).thenReturn(Single.just(createMessageList(false)));
        when(gameStartInteractor.isMessageAnimationEnabled()).thenReturn(false);
        when(gameStartInteractor.loadUserProgress()).thenReturn(Single.just(4));
        when(gameLoopInteractor.loadNewMessage(Mockito.anyInt(), Mockito.anyList())).thenReturn(Single.error(new NullPointerException()));
        when(gameLoopInteractor.getNextIndex(Mockito.anyList())).thenReturn(2);
        presenter.onGameStart();
        testScheduler.triggerActions();
        InOrder inOrder = Mockito.inOrder(listView);
        inOrder.verify(listView).setUpdateAnimator(false);
        inOrder.verify(listView).showUserTyping();
        inOrder.verify(listView).showAnimation();
        testScheduler.advanceTimeBy(4, TimeUnit.SECONDS);
        testScheduler.triggerActions();
        verify(listView).updateMessageList(Mockito.anyList());
        verify(listView).scrollToBottom();

        verify(listView).hideUserTyping();
        verify(listView).clearAnimation();

        verifyNoMoreInteractions(listView);
    }


    @Test
    public void changeListOnClick_TestEmpty() {
        presenter.changeListOnClick(ListFragmentPresenter.Choice.NEGATIVE, new ArrayList<>());
        verifyNoMoreInteractions(listView);

        presenter.changeListOnClick(ListFragmentPresenter.Choice.POSITIVE, new ArrayList<>());
        verifyNoMoreInteractions(listView);
    }


    @Test
    public void changeListOnClick_TestRight() {
        when(gameLoopInteractor.loadNewMessage(Mockito.anyInt(), Mockito.anyList())).thenReturn(Single.just(createMessageList(true)));
        when(gameLoopInteractor.updateMessageList(Mockito.any(MessageChoices.class), Mockito.anyList(), Mockito.anyBoolean()))
                .thenReturn(createMessageList(false));
        presenter.changeListOnClick(ListFragmentPresenter.Choice.NEGATIVE, createMessageList(true));
        InOrder inOrder = Mockito.inOrder(listView);
        inOrder.verify(listView).updateMessageList(Mockito.anyList());
        inOrder.verify(listView).showUserTyping();
        inOrder.verify(listView).showAnimation();
        verifyNoMoreInteractions(listView);

    }

    @Test
    public void changeListOnClick_TestRight_Positive() {
        when(gameLoopInteractor.loadNewMessage(Mockito.anyInt(), Mockito.anyList())).thenReturn(Single.just(createMessageList(true)));
        when(gameLoopInteractor.updateMessageList(Mockito.any(MessageChoices.class), Mockito.anyList(), Mockito.anyBoolean()))
                .thenReturn(createMessageList(false));
        presenter.changeListOnClick(ListFragmentPresenter.Choice.POSITIVE, createMessageList(true));
        InOrder inOrder = Mockito.inOrder(listView);
        inOrder.verify(listView).updateMessageList(Mockito.anyList());
        inOrder.verify(listView).showUserTyping();
        inOrder.verify(listView).showAnimation();
        verifyNoMoreInteractions(listView);
    }

    @Test
    public void saveGameTest() {
        when(saveGameInteractor.saveGame(Mockito.anyInt(), Mockito.anyList())).thenReturn(Completable.complete());
        presenter.save(createMessageList(false));
        verify(saveGameInteractor).saveGame(Mockito.anyInt(), Mockito.anyList());
    }

    private List<ListItem> createMessageList(boolean isChoices) {
        List<ListItem> listItems = new ArrayList<>();
        listItems.add(
                new MessageItem(1, "test", false, 7, null)
        );
        listItems.add(
                new MessageItem(2, "test 2", false, 8, null)
        );
        if (isChoices) {
            listItems.add(
                    new MessageChoices(
                            "label 1", "label 2",
                            "choice 1", "choice 2",
                            2, 3
                    )
            );
        }
        return listItems;
    }

    private IRxSchedulers rxSchedulersTest = new IRxSchedulers() {
        @Override
        public Scheduler getMainThreadScheduler() {
            return testScheduler;
        }

        @Override
        public Scheduler getIoScheduler() {
            return testScheduler;
        }
    };

}