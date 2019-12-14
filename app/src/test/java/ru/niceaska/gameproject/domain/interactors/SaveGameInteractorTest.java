package ru.niceaska.gameproject.domain.interactors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;
import ru.niceaska.gameproject.domain.IDataRepository;

import static org.mockito.Mockito.when;

/**
 * @author Мохнаткина Алина
 */
@RunWith(MockitoJUnitRunner.class)
public class SaveGameInteractorTest {

    @Mock
    private IDataRepository dataRepository;

    private SaveGameInteractor interactor;

    @Before
    public void setUp() throws Exception {
        interactor = new SaveGameInteractor(dataRepository);
    }

    @Test
    public void saveGame() {
        when(dataRepository.saveUserData(Mockito.anyInt(), Mockito.anyList())).thenReturn(Completable.complete());

        TestObserver testObserver = interactor.saveGame(1, new ArrayList<>()).test();

        testObserver.assertNoValues()
                .assertNoErrors()
                .assertComplete();

        testObserver.dispose();
    }
}