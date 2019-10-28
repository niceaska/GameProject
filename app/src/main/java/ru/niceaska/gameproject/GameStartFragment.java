package ru.niceaska.gameproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameStartFragment extends Fragment implements GameStartApp {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_game_layout, container, false);
        final Button startGame = v.findViewById(R.id.start_new_game);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginNewGame();
            }
        });
        return v;
    }

    @Override
    public void beginNewGame() {
        if (getActivity() instanceof StartGameContract) {
            ((StartGameContract) getActivity()).startGame();
        }
    }
}
