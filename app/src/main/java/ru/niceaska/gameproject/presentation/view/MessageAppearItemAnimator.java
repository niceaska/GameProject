package ru.niceaska.gameproject.presentation.view;

import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAppearItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        if (holder instanceof MessagesAdapter.MessageViewHolder) {
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f);
            alphaAnimator.setDuration(1500);
            alphaAnimator.setInterpolator(new AccelerateInterpolator());
            alphaAnimator.start();
            return true;
        } else if (holder instanceof MessagesAdapter.ButtonChoicesViewHolder) {
            holder.itemView.setTranslationX(-1200f);
            ObjectAnimator translationX = ObjectAnimator.ofFloat(holder.itemView, "translationX", -1200f, 0f);
            translationX.setDuration(1500);
            translationX.setInterpolator(new AccelerateInterpolator());
            translationX.start();
            return true;
        }
        return super.animateAdd(holder);
    }


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
