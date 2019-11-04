package ru.niceaska.gameproject.presentation.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.data.repository.DataRepository;
import ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter;

import static ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter.Choice.NEGATIVE;
import static ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter.Choice.POSITIVE;

public class MessageListFragment extends Fragment implements IMessageListFragment {

    private final int ANIMATION_DURATION = 5500;
    private RecyclerView recyclerView;
    private int gameProgress;
    private ListFragmentPresenter listFragmentPresenter;
    private LinearLayoutManager layoutManager;
    private TypeWriter typeWriter;
    private boolean isSaveState;
    private MessagesAdapter messagesAdapter = new MessagesAdapter();
    private ObjectAnimator animator;

    public static MessageListFragment newInstance() {

        Bundle args = new Bundle();
        MessageListFragment fragment = new MessageListFragment();
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listFragmentPresenter = new ListFragmentPresenter(this, DataRepository.getInstance());
        listFragmentPresenter.onGameStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_list_fragment, container, false);
        recyclerView = v.findViewById(R.id.dialog_view);
        layoutManager = new LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false
        );
        typeWriter = v.findViewById(R.id.textView);
        recyclerView.setLayoutManager(layoutManager);
        initRecyclerListeners();
        return v;
    }


    @Override
    public void showAnimation(int repeatCount) {
        CharSequence startString = requireContext().getResources().getString(R.string.printing);
        StringBuilder stringBuilder = new StringBuilder();
        typeWriter.setAnimatedText(stringBuilder.append(startString).append("..."));
        animator = ObjectAnimator.ofInt(typeWriter, "textVisibility",
                startString.length(), startString.length() + 3);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setStartDelay(200);
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
    }

    @Override
    public void showUserTyping() {
        typeWriter.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUserTyping() {
        typeWriter.setVisibility(View.GONE);

    }

    @Override
    public void clearAnimation() {
        animator.end();
    }

    @Override
    public void scrollToBottom() {
        layoutManager.scrollToPosition(messagesAdapter.getItemCount() - 1);
    }


    private void initRecyclerListeners() {
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
    public void onFinalDestroy() {
        listFragmentPresenter.saveOnDestroy(messagesAdapter.getListObj());
    }


    @Override
    public void onStop() {
        super.onStop();
        if (animator != null) {
            animator.end();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listFragmentPresenter.detachView();
    }
}
