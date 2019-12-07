package ru.niceaska.gameproject.presentation.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TypeWriter extends AppCompatTextView {


    private CharSequence animatedText;

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTextVisibility(int lengthEnd) {
        if (lengthEnd > animatedText.length()) {
            lengthEnd = animatedText.length();
        }
        setText(animatedText.subSequence(0, lengthEnd));
    }

    public void setAnimatedText(CharSequence animatedText) {
        this.animatedText = animatedText;
    }
}