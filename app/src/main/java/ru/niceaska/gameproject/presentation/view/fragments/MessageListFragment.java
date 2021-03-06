package ru.niceaska.gameproject.presentation.view.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import ru.niceaska.gameproject.MyApp;
import ru.niceaska.gameproject.R;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.di.components.AppComponent;
import ru.niceaska.gameproject.di.components.DaggerGameComponent;
import ru.niceaska.gameproject.di.components.DaggerGameScreenComponent;
import ru.niceaska.gameproject.di.components.GameComponent;
import ru.niceaska.gameproject.di.components.GameScreenComponent;
import ru.niceaska.gameproject.di.modules.GameLoopInteractorModule;
import ru.niceaska.gameproject.di.modules.GameStartModule;
import ru.niceaska.gameproject.di.modules.SaveGameInteractorModule;
import ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter;
import ru.niceaska.gameproject.presentation.view.ChoiceButtonsHolder;
import ru.niceaska.gameproject.presentation.view.MessageListView;
import ru.niceaska.gameproject.presentation.view.adapters.MessagesAdapter;
import ru.niceaska.gameproject.presentation.view.ui.MessageAppearItemAnimator;
import ru.niceaska.gameproject.presentation.view.ui.TypeWriter;

import static ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter.Choice.NEGATIVE;
import static ru.niceaska.gameproject.presentation.presenter.ListFragmentPresenter.Choice.POSITIVE;

/**
 * Фрагмент списка сообщений
 */
public class MessageListFragment extends Fragment implements MessageListView {

    private final int ANIMATION_DURATION = 3000;

    @Inject
    ListFragmentPresenter listFragmentPresenter;

    private GameComponent gameComponent;
    private AppComponent appComponent;
    private GameScreenComponent gameScreenComponent;
    private LinearLayoutManager layoutManager;
    private TypeWriter typeWriter;
    private MessagesAdapter messagesAdapter = new MessagesAdapter();
    private ObjectAnimator animator;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    /**
     * Метод для создания инстанса фрагмента
     * Устанавливает флаг сохранения состояния в true
     *
     * @return фрагмент
     */
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

        getAppComponent();
        injectDependencies();
    }

    private void injectDependencies() {
        gameComponent = DaggerGameComponent.builder()
                .appComponent(appComponent)
                .gameStartModule(new GameStartModule())
                .saveGameInteractorModule(new SaveGameInteractorModule())
                .gameLoopInteractorModule(new GameLoopInteractorModule())
                .build();
        gameScreenComponent = DaggerGameScreenComponent.builder()
                .gameComponent(gameComponent)
                .build();
        gameScreenComponent.inject(this);
    }

    private void getAppComponent() {
        Context applicationContext = requireContext().getApplicationContext();
        if (applicationContext instanceof MyApp) {
            appComponent = ((MyApp) applicationContext).getAppComponent();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_list_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.dialog_view);
        progressBar = view.findViewById(R.id.progress_loading);
        layoutManager = new LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false
        );
        typeWriter = view.findViewById(R.id.textView);
        recyclerView.setLayoutManager(layoutManager);
        initRecyclerListeners();
        listFragmentPresenter.attachView(this);
        listFragmentPresenter.onGameStart();
    }

    /**
     * Показать анимацию печатания
     */
    @Override
    public void showAnimation() {
        CharSequence startString = requireContext().getResources().getString(R.string.printing);
        StringBuilder stringBuilder = new StringBuilder();
        initTypingAnimator(startString, stringBuilder);
        animator.start();
    }

    private void initTypingAnimator(CharSequence startString, StringBuilder stringBuilder) {
        typeWriter.setAnimatedText(stringBuilder.append(startString).append("..."));
        animator = ObjectAnimator.ofInt(typeWriter, "textVisibility",
                startString.length(), startString.length() + 4);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setStartDelay(200);
        animator.setDuration(ANIMATION_DURATION);
    }

    /**
     * Показать строку с информаций о печати
     */
    @Override
    public void showUserTyping() {
        typeWriter.setVisibility(View.VISIBLE);
    }

    /**
     * Скрыть строку с информаций о печати
     */
    @Override
    public void hideUserTyping() {
        typeWriter.setVisibility(View.GONE);
    }

    /**
     * Остановить анимацию
     */
    @Override
    public void clearAnimation() {
        if (animator != null) {
            animator.end();
        }
    }

    /**
     *  Прокрутить список к последнему сообщению
     */
    @Override
    public void scrollToBottom() {
        layoutManager.scrollToPosition(messagesAdapter.getItemCount() - 1);
    }

    /**
     * Показать прогресс бар загрузки
     */
    @Override
    public void showLoadingProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Скрыть прогресс бар загрузки
     */
    @Override
    public void hideLoadingProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Установить аниматор для элементов списка
     */
    @Override
    public void setUpdateAnimator(boolean isMessageAnimation) {
        if (isMessageAnimation) {
            recyclerView.setItemAnimator(new MessageAppearItemAnimator());
        }
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


    /**
     * Обновить список элементов ресайклера
     * @param newList новый список
     */
    @Override
    public void updateMessageList(List<ListItem> newList) {
        messagesAdapter.updateList(newList);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (animator != null) {
            animator.end();
        }
        listFragmentPresenter.save(messagesAdapter.getListObj());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listFragmentPresenter.clearDisposable();
        listFragmentPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameComponent = null;
        gameScreenComponent = null;
    }
}
