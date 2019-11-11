package ru.niceaska.gameproject.presentation.presenter;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.User;
import ru.niceaska.gameproject.data.model.UserPojo;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.view.GameStartFragment;

public class StartAppPresenter {
    private WeakReference<GameStartFragment> gameStartFragmentWeakReference;
    private DataRepository dataRepository;
    private CompositeDisposable compositeDisposable;

    private static final String FILE_NAME = "scenario.json";
    private static final String TAG = StartAppPresenter.class.getName();

    public StartAppPresenter(GameStartFragment gameStartFragmentWeakReference, DataRepository dataRepository) {
        this.gameStartFragmentWeakReference = new WeakReference<>(gameStartFragmentWeakReference);
        this.dataRepository = dataRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    public void loadData() {
        User user = new User(new UserPojo("1", "test", 0), new ArrayList<HistoryMessage>());
        if (gameStartFragmentWeakReference.get() != null) {
            Activity activity = gameStartFragmentWeakReference.get().getActivity();
            if (activity != null) {
                AssetManager assetManager = activity.getAssets();
                try {
                    InputStream is = assetManager.open(FILE_NAME);
                    Reader open = new InputStreamReader(is);
                    compositeDisposable.add(dataRepository.firstLoadData(user, open).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(o -> {
                                    },
                                    throwable -> {
                                        Toast.makeText(activity.getApplicationContext(),
                                                activity.getResources().getString(R.string.error_data_loading),
                                                Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "loadData: " + throwable.getMessage());
                                        open.close();
                                    },
                                    () -> {
                                        gameStartFragmentWeakReference.get().beginNewGame();
                                        open.close();
                                    }));
                } catch (IOException e) {
                    Log.d(TAG, "loadData: " + e.getMessage());
                }
            }
        }
    }

    public void unSubscribe() {
        compositeDisposable.clear();
    }

}
