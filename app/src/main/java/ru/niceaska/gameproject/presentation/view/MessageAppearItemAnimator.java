package ru.niceaska.gameproject.presentation.view;

import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAppearItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        if (holder instanceof MessagesAdapter.MessageViewHolder) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f);
            objectAnimator.setDuration(1500);
            objectAnimator.setInterpolator(new AccelerateInterpolator());
            objectAnimator.start();
            return true;
        }
        return super.animateAdd(holder);
    }
}
