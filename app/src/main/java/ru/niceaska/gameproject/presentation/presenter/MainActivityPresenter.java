package ru.niceaska.gameproject.presentation.presenter;

import java.lang.ref.WeakReference;

import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.data.repository.IOnGameRunCheckListener;
import ru.niceaska.gameproject.presentation.view.IMainActivity;

public class MainActivityPresenter {
    WeakReference<IMainActivity> activityWeakReference;
    DataRepository dataRepository;

    public MainActivityPresenter(IMainActivity activity, DataRepository dataRepository) {
        this.activityWeakReference = new WeakReference<IMainActivity>(activity);
        this.dataRepository = dataRepository;
    }

    public void gameRun(boolean isSavedState) {
        if (isSavedState) {
            activityWeakReference.get().startGame();
            return;
        }
        IOnGameRunCheckListener listener = new IOnGameRunCheckListener() {
            @Override
            public void onChecked(boolean isFirstRun) {
                if (isFirstRun) {
                    activityWeakReference.get().showStartAppFragment();
                } else {
                    activityWeakReference.get().startGame();
                }
            }
        };
        dataRepository.checkFirstStart("1", listener);
    }


}
