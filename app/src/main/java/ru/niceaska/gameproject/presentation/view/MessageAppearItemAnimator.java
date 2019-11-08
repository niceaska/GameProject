package ru.niceaska.gameproject.presentation.view;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        } else if (holder instanceof MessagesAdapter.ButtonChoicesViewHolder) {
            holder.itemView.setTranslationX(-1000f);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.itemView, "translationX", -1000f, 0f);
            objectAnimator.setDuration(1500);
            objectAnimator.setInterpolator(new AccelerateInterpolator());
            objectAnimator.start();
            return true;
        }
        return super.animateAdd(holder);
    }

    @Override
    public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
        if (viewHolder instanceof MessagesAdapter.MessageViewHolder) {
            viewHolder.itemView.setVisibility(View.GONE);
            return true;
        } else if (viewHolder instanceof MessagesAdapter.ButtonChoicesViewHolder) {
            viewHolder.itemView.setVisibility(View.GONE);
            return true;
        }
        return super.animateDisappearance(viewHolder, preLayoutInfo, postLayoutInfo);
    }
}
