package ru.niceaska.gameproject.presentation.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    static final String LIST_FRAGMENT = "listFragment";
    static final String FIRST_RUN = "firstRun";

    private boolean isFirstRun;
    private MainActivityPresenter mainActivityPresenter;
    private DataRepository dataRepository;
    private Fragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isFirstRun = savedInstanceState == null || savedInstanceState.getByte(FIRST_RUN) == 1;
        dataRepository = DataRepository.getInstance();
        mainActivityPresenter = new MainActivityPresenter(this, dataRepository);
        if (savedInstanceState != null && !isFirstRun) {
            this.listFragment = getSupportFragmentManager().getFragment(savedInstanceState, LIST_FRAGMENT);
            mainActivityPresenter.gameRun(true);
        } else {
            listFragment = MessageListFragment.newInstance();
            mainActivityPresenter.gameRun(false);
            isFirstRun = false;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByte(FIRST_RUN, (byte) (isFirstRun ? 1 : 0));
        if (this.listFragment != null && listFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, LIST_FRAGMENT, listFragment);
        }
    }

    @Override
    public void startGame() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment, listFragment)
                .commit();
    }

    @Override
    public void showStartAppFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment, new GameStartFragment())
                .commit();
    }
}