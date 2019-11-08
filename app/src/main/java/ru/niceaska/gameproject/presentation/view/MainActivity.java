package ru.niceaska.gameproject.presentation.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    static final String FIRST_RUN = "firstRun";

    private MainActivityPresenter mainActivityPresenter;
    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataRepository = DataRepository.getInstance();
        mainActivityPresenter = new MainActivityPresenter(this, dataRepository);
        mainActivityPresenter.gameRun();


    }

    @Override
    public void startGame() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment, MessageListFragment.newInstance())
                .commit();
    }

    @Override
    public void showStartAppFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment, new GameStartFragment())
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.list_fragment);
        if (fragment instanceof MessageListFragment) {
            ((MessageListFragment) fragment).onFinalDestroy();
        }
    }

}