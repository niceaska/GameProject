package ru.niceaska.gameproject.presentation.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter;

import static ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter.Choice.NEGATIVE;
import static ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter.Choice.POSITIVE;

public class MessageListFragment extends Fragment implements IMessageListFragment {

    private static final String LIST_STATE_KEY = "listState";
    private static final String PROGRESS_STATE_KEY = "progressState";
    private final int ANIMATION_DURATION = 5500;
    private RecyclerView recyclerView;
    private int gameProgress;
    private Parcelable listState;
    private ListFragmentPresenter listFragmentPresenter;
    private LinearLayoutManager layoutManager;
    private TypeWriter typeWriter;
    private MessagesAdapter messagesAdapter = new MessagesAdapter();

    public static MessageListFragment newInstance() {

        Bundle args = new Bundle();
        MessageListFragment fragment = new MessageListFragment();
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_list_fragment, container, false);
        listFragmentPresenter = new ListFragmentPresenter(this, new DataRepository());
        recyclerView = v.findViewById(R.id.dialog_view);
        layoutManager = new LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false
        );
        typeWriter = v.findViewById(R.id.textView);
        recyclerView.setLayoutManager(layoutManager);

        return v;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        listState = layoutManager.onSaveInstanceState();
        state.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) messagesAdapter.getListObj());
        state.putParcelable(LIST_STATE_KEY, listState);
        state.putInt(PROGRESS_STATE_KEY, gameProgress);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            gameProgress = savedInstanceState.getInt(PROGRESS_STATE_KEY);
            if (listState != null) {
                layoutManager.onRestoreInstanceState(listState);
            }
            listFragmentPresenter.onGameStart(gameProgress, true,
                    savedInstanceState.<ListItem>getParcelableArrayList("key"));
        } else {
            listFragmentPresenter.onGameStart(gameProgress, false, new ArrayList<ListItem>());
        }
    }


    @Override
    public void showAnimation(int repeatCount) {
        CharSequence startString = requireContext().getResources().getString(R.string.printing);
        StringBuilder stringBuilder = new StringBuilder();
        typeWriter.setAnimatedText(stringBuilder.append(startString).append("..."));
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(typeWriter, "textVisibility",
                startString.length(), startString.length() + 3);
        objectAnimator.setRepeatCount(repeatCount);
        objectAnimator.setStartDelay(700);
        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                typeWriter.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                typeWriter.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                typeWriter.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();

    }

    @Override
    public void showListData() {

    }

    @Override
    public void initRecycler(List<ListItem> listItems) {
        messagesAdapter.setListObj(listItems);
    }

    @Override
    public void initRecyclerListeners() {
        messagesAdapter.setListener(new ChoiceButtonsHolder() {
            @Override
            public void onPositiveChoice() {
                listFragmentPresenter.changeListOnClick(POSITIVE, messagesAdapter.getListObj());
            }

            @Override
            public void onNegativeChoice() {
                listFragmentPresenter.changeListOnClick(NEGATIVE, messagesAdapter.getListObj());

            }
        });
        recyclerView.setAdapter(messagesAdapter);
    }


    @Override
    public void updateMessageList(List<ListItem> newList) {
        messagesAdapter.updateList(newList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listFragmentPresenter.saveOnDestroy(messagesAdapter.getListObj());
    }

    public void setGameProgress(int gameProgress) {
        this.gameProgress = gameProgress;
    }
}
