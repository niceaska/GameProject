package ru.niceaska.gameproject.presentation.view.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Класс для отображения анимации печатания
 */
public class TypeWriter extends AppCompatTextView {

    private CharSequence animatedText;

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Используется аниматором для отображения анимации печатания
     *
     * @param lengthEnd полная длина текста
     */
    public void setTextVisibility(int lengthEnd) {
        if (lengthEnd > animatedText.length()) {
            lengthEnd = animatedText.length();
        }
        setText(animatedText.subSequence(0, lengthEnd));
    }

    /**
     * Установить текст для анимации
     * @param animatedText текст для анимации
     */
    public void setAnimatedText(CharSequence animatedText) {
        this.animatedText = animatedText;
    }
}