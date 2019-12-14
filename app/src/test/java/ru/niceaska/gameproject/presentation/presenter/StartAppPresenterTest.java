package ru.niceaska.gameproject.presentation.presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.domain.interactors.FirstLoadDataInteractor;
import ru.niceaska.gameproject.presentation.view.GameStartView;
import ru.niceaska.gameproject.rx.IRxSchedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Мохнаткина Алина
 */
@RunWith(MockitoJUnitRunner.class)
public class StartAppPresenterTest {

    @Mock
    private FirstLoadDataInteractor loadDataInteractor;

    @Mock
    private GameStartView startView;

    private StartAppPresenter startAppPresenter;

    @Before
    public void setUp() throws Exception {
        startAppPresenter = new StartAppPresenter(loadDataInteractor, rxSchedulersTest);
        startAppPresenter.attachView(startView);
    }

    @Test()
    public void loadDataTest_Error() throws Exception {
        startAppPresenter.attachView(startView);
        when(loadDataInteractor.firstLoadData()).thenReturn(Observable.error(new NullPointerException()));

        startAppPresenter.loadData();
        verify(startView).showErrorToast();

        verifyNoMoreInteractions(startView);
    }

    @Test
    public void loadDataTest() {
        when(loadDataInteractor.createUser()).thenReturn(Completable.complete());
        when(loadDataInteractor.firstLoadData()).thenReturn(Observable.just(new ArrayList()));
        startAppPresenter.loadData();
        verify(loadDataInteractor).createUser();
        verify(startView).beginNewGame();
        ;

        verifyNoMoreInteractions(startView);
    }

    @Test(expected = NullPointerException.class)
    public void detachTest() {
        startAppPresenter.detachView();
        startAppPresenter.loadData();
    }


    @After
    public void cleanAll() {
        startAppPresenter.unSubscribe();
        startAppPresenter.detachView();
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