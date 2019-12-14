package ru.niceaska.gameproject.domain.interactors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import ru.niceaska.gameproject.domain.IDataRepository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Мохнаткина Алина
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityInteractorTest {


    @Mock
    private IDataRepository dataRepository;

    private MainActivityInteractor interactor;

    @Before
    public void setUp() {
        interactor = new MainActivityInteractor(dataRepository);
    }

    @Test
    public void checkFirstStartTest_True() {
        when(dataRepository.checkFirstStart()).thenReturn(Single.just(true));

        TestObserver<Boolean> testObserver = interactor.checkFirstStart().test();

        testObserver.assertValueCount(1)
                .assertNoErrors()
                .assertNever(false)
                .assertValue(true);

        testObserver.dispose();
    }

    @Test
    public void checkFirstStartTest_False() {
        when(dataRepository.checkFirstStart()).thenReturn(Single.just(false));

        TestObserver<Boolean> testObserver = interactor.checkFirstStart().test();

        testObserver.assertValueCount(1)
                .assertNoErrors()
                .assertNever(true)
                .assertValue(false);

        testObserver.dispose();
    }

    @Test
    public void isNotificationOnTest() {
        when(dataRepository.isNotificationEnabled()).thenReturn(true);
        assertTrue(interactor.isNotificationOn());

        when(dataRepository.isNotificationEnabled()).thenReturn(false);
        assertFalse(interactor.isNotificationOn());
    }

    @Test
    public void refreshDataTest() {
        when(dataRepository.refreshDatabase()).thenReturn(Completable.complete());

        TestObserver testObserver = interactor.refreshData().test();

        testObserver.assertNoValues()
                .assertNoErrors()
                .assertComplete();

        testObserver.dispose();
    }
}