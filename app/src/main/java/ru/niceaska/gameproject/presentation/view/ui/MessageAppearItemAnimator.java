package ru.niceaska.gameproject.presentation.view.ui;

import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import ru.niceaska.gameproject.presentation.view.adapters.MessagesAdapter;

/**
 * Item Animator для анимирования элементов списка
 */
public class MessageAppearItemAnimator extends DefaultItemAnimator {

    /**
     * Анимирует добавление в список
     *
     * @param holder холдер списка
     * @return булевое значение включена ли анимация
     */
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        if (holder instanceof MessagesAdapter.MessageViewHolder) {
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f);
            alphaAnimator.setDuration(1000);
            alphaAnimator.setInterpolator(new AccelerateInterpolator());
            alphaAnimator.start();
            return true;
        } else if (holder instanceof MessagesAdapter.ButtonChoicesViewHolder) {
            holder.itemView.setTranslationX(-1200f);
            ObjectAnimator translationX = ObjectAnimator.ofFloat(holder.itemView, "translationX", -1200f, 0f);
            translationX.setDuration(1000);
            translationX.setInterpolator(new AccelerateInterpolator());
            translationX.start();
            return true;
        }
        return super.animateAdd(holder);
    }

    /**
     * Анимирует удаление из списка
     * @param holder холдер списка
     * @return булевое значение включена ли анимация
     */
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        if (holder instanceof MessagesAdapter.ButtonChoicesViewHolder) {
            holder.itemView.setTranslationX(-1200f);
            ObjectAnimator translationX = ObjectAnimator.ofFloat(holder.itemView, "translationX", 0f, 12000f);
            translationX.setDuration(1000);
            translationX.setInterpolator(new AccelerateInterpolator());
            translationX.start();
            return true;
        }
        return super.animateRemove(holder);
    }
}
