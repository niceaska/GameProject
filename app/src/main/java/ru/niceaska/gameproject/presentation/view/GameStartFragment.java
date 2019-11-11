package ru.niceaska.gameproject.presentation.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.presenter.StartAppPresenter;

public class GameStartFragment extends Fragment implements IGameStartFragment {

    private StartAppPresenter startAppPresenter;
    private DataRepository dataRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_game_layout, container, false);
        final Button startGame = v.findViewById(R.id.start_new_game);
        dataRepository = new DataRepository();
        startAppPresenter = new StartAppPresenter(this, dataRepository);
        startGame.setOnClickListener(v1 -> startAppPresenter.loadData());
        return v;
    }

    @Override
    public void beginNewGame() {
        if (getActivity() instanceof IMainActivity) {
            ((IMainActivity) getActivity()).startGame();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        startAppPresenter.unSubscribe();
    }
}
