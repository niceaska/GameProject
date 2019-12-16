package ru.niceaska.gameproject.presentation.view.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import ru.niceaska.gameproject.R;

/**
 * Фрагмент настроек
 */
public class GamePreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.prefBackground));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.game_prefs, null);

    }
}
