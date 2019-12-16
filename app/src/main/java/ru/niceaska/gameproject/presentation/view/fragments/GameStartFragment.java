package ru.niceaska.gameproject.presentation.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import ru.niceaska.gameproject.MyApp;
import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.di.components.AppComponent;
import ru.niceaska.gameproject.di.components.DaggerFLMscreenComponent;
import ru.niceaska.gameproject.di.components.DaggerFirstLoadComponent;
import ru.niceaska.gameproject.di.components.FLMscreenComponent;
import ru.niceaska.gameproject.di.components.FirstLoadComponent;
import ru.niceaska.gameproject.di.modules.FirstLoadModule;
import ru.niceaska.gameproject.presentation.presenter.StartAppPresenter;
import ru.niceaska.gameproject.presentation.view.GameStartView;
import ru.niceaska.gameproject.presentation.view.IMainActivity;

/**
 * Фрагмент старта приложения
 */
public class GameStartFragment extends Fragment implements GameStartView {

    @Inject
    StartAppPresenter startAppPresenter;

    private AppComponent appComponent;
    private FirstLoadComponent firstLoadComponent;
    private FLMscreenComponent flMscreenComponent;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getAppComponent();
        getDependencies();
    }

    private void getAppComponent() {
        Context applicationContext = requireContext().getApplicationContext();
        if (applicationContext instanceof MyApp) {
            appComponent = ((MyApp) applicationContext).getAppComponent();
        }
    }

    private void getDependencies() {
        firstLoadComponent = DaggerFirstLoadComponent.builder()
                .appComponent(appComponent)
                .firstLoadModule(new FirstLoadModule())
                .build();
        flMscreenComponent = DaggerFLMscreenComponent.builder()
                .firstLoadComponent(firstLoadComponent)
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_game_layout, container, false);
        flMscreenComponent.inject(this);
        startAppPresenter.attachView(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Button startGame = view.findViewById(R.id.start_new_game);
        startGame.setOnClickListener(v1 -> startAppPresenter.startNewGame());
    }

    /**
     * Начать новую игру
     */
    @Override
    public void beginNewGame() {
        if (getActivity() instanceof IMainActivity) {
            ((IMainActivity) getActivity()).startGame();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        startAppPresenter.detachView();
    }

    @Override
    public void onStop() {
        super.onStop();
        startAppPresenter.unSubscribe();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        firstLoadComponent = null;
        flMscreenComponent = null;
    }

    /**
     * Показать тоаст с ошибкой
     */
    @Override
    public void showErrorToast() {
        Toast.makeText(requireContext(),
                getResources().getString(R.string.error_data_loading),
                Toast.LENGTH_LONG).show();
    }
}
