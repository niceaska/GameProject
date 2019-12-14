package ru.niceaska.gameproject.presentation.presenter;

import androidx.room.EmptyResultSetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.domain.interactors.MainActivityInteractor;
import ru.niceaska.gameproject.presentation.view.IMainActivity;
import ru.niceaska.gameproject.rx.IRxSchedulers;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Мохнаткина Алина
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityPresenterTest {

    @Mock
    private IMainActivity activity;

    @Mock
    private MainActivityInteractor interactor;

    private MainActivityPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new MainActivityPresenter(interactor, rxSchedulersTest);
        presenter.attachView(activity);
    }

    @After
    public void tearDown() throws Exception {
        presenter.clearDisposable();
        presenter.detachView();
    }

    @Test
    public void gameRun_TrueTest() {
        when(interactor.checkFirstStart()).thenReturn(Single.just(true));
        when(interactor.isNotificationOn()).thenReturn(true);
        presenter.gameRun();
        InOrder inOrder = Mockito.inOrder(activity);
        inOrder.verify(activity).clearNotifications();
        inOrder.verify(activity).planningNotification();
        inOrder.verify(activity).showStartAppFragment();

        verifyNoMoreInteractions(activity);
    }

    @Test
    public void gameRun_FalseTest() {
        when(interactor.checkFirstStart()).thenReturn(Single.just(false));
        when(interactor.isNotificationOn()).thenReturn(true);
        presenter.gameRun();
        InOrder inOrder = Mockito.inOrder(activity);
        inOrder.verify(activity).clearNotifications();
        inOrder.verify(activity).planningNotification();
        inOrder.verify(activity).startGame();

        verifyNoMoreInteractions(activity);
    }

    @Test
    public void gameRun_ExceptionTest() {
        when(interactor.checkFirstStart()).thenReturn(Single.error(new EmptyResultSetException("error")));
        when(interactor.isNotificationOn()).thenReturn(true);
        presenter.gameRun();
        InOrder inOrder = Mockito.inOrder(activity);
        inOrder.verify(activity).clearNotifications();
        inOrder.verify(activity).planningNotification();
        inOrder.verify(activity).showStartAppFragment();

        verifyNoMoreInteractions(activity);
    }

    @Test
    public void gameRun_NotificationOff_Test() {
        when(interactor.checkFirstStart()).thenReturn(Single.just(true));
        when(interactor.isNotificationOn()).thenReturn(false);

        presenter.gameRun();
        InOrder inOrder = Mockito.inOrder(activity);
        inOrder.verify(activity).clearNotifications();
        inOrder.verify(activity).showStartAppFragment();

        verifyNoMoreInteractions(activity);
    }

    @Test
    public void restartGame() {
        when(interactor.checkFirstStart()).thenReturn(Single.just(true));
        when(interactor.isNotificationOn()).thenReturn(false);
        when(interactor.refreshData()).thenReturn(Completable.complete());

        presenter.restartGame();
        InOrder inOrder = Mockito.inOrder(activity);
        inOrder.verify(activity).clearNotifications();
        inOrder.verify(activity).showStartAppFragment();

        verifyNoMoreInteractions(activity);
    }


    private IRxSchedulers rxSchedulersTest = new IRxSchedulers() {
        @Override
        public Scheduler getMainThreadScheduler() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler getIoScheduler() {
            return Schedulers.trampoline();
        }
    };
}