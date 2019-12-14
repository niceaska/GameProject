package ru.niceaska.gameproject.domain.interactors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import ru.niceaska.gameproject.domain.IDataRepository;

import static org.mockito.Mockito.when;

/**
 * @author Мохнаткина Алина
 */
@RunWith(MockitoJUnitRunner.class)
public class FirstLoadDataInteractorTest {

    @Mock
    private IDataRepository dataRepository;

    private FirstLoadDataInteractor interactor;

    @Before
    public void setUp() {
        interactor = new FirstLoadDataInteractor(dataRepository);
    }

    @Test
    public void firstLoadData() {
        when(dataRepository.firstLoadData()).thenReturn(Observable.just(new ArrayList()));

        TestObserver testObserver = interactor.firstLoadData().test();
        testObserver.assertNoErrors()
                .assertValueCount(1);

        testObserver.dispose();
    }

    @Test
    public void createUser() {
        when(dataRepository.createUser()).thenReturn(Completable.complete());
        TestObserver testObserver = interactor.createUser().test();

        testObserver.assertValueCount(0)
                .assertNoErrors()
                .assertComplete();

        testObserver.dispose();
    }
}