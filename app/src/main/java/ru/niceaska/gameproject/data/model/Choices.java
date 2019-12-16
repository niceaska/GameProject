package ru.niceaska.gameproject.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.common.base.Objects;

/**
 * Модель выбора игрока
 */
@Entity
public class Choices {


    @ColumnInfo(name = "positive_label")
    private String positiveChoiceLabel;
    @ColumnInfo(name = "negative_label")
    private String negativeChoiceLabel;
    @ColumnInfo(name = "postive_choice")
    private String positiveChoice;
    @ColumnInfo(name = "negative_choice")
    private String negativeChoice;
    @ColumnInfo(name = "positive_answer")
    private int positiveMessageAnswer;
    @ColumnInfo(name = "negative_answer")
    private int negativeMessageAnswer;

    /**
     * Конструктор для создания модели
     *
     * @param positiveChoiceLabel   Заголовок кнопки
     * @param negativeChoiceLabel   Заголовок второй кнопки
     * @param positiveChoice        Сообщение игрока при выборе соответсвующей кнопки
     * @param negativeChoice        Сообщение игрока при выборе соответсвующей кнопки
     * @param positiveMessageAnswer номер следущего сообщения при данном выборе
     * @param negativeMessageAnswer номер следущего сообщения при данном выборе
     */
    public Choices(String positiveChoiceLabel, String negativeChoiceLabel, String positiveChoice,
                   String negativeChoice, int positiveMessageAnswer, int negativeMessageAnswer) {
        this.positiveChoiceLabel = positiveChoiceLabel;
        this.negativeChoiceLabel = negativeChoiceLabel;
        this.positiveChoice = positiveChoice;
        this.negativeChoice = negativeChoice;
        this.positiveMessageAnswer = positiveMessageAnswer;
        this.negativeMessageAnswer = negativeMessageAnswer;
    }


    public String getPositiveChoiceLabel() {
        return positiveChoiceLabel;
    }


    public String getNegativeChoiceLabel() {
        return negativeChoiceLabel;
    }


    public String getPositiveChoice() {
        return positiveChoice;
    }


    public String getNegativeChoice() {
        return negativeChoice;
    }


    public int getPositiveMessageAnswer() {
        return positiveMessageAnswer;
    }


    public int getNegativeMessageAnswer() {
        return negativeMessageAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Choices choices = (Choices) o;
        return positiveMessageAnswer == choices.positiveMessageAnswer &&
                negativeMessageAnswer == choices.negativeMessageAnswer &&
                Objects.equal(positiveChoiceLabel, choices.positiveChoiceLabel) &&
                Objects.equal(negativeChoiceLabel, choices.negativeChoiceLabel) &&
                Objects.equal(positiveChoice, choices.positiveChoice) &&
                Objects.equal(negativeChoice, choices.negativeChoice);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(positiveChoiceLabel, negativeChoiceLabel, positiveChoice,
                negativeChoice, positiveMessageAnswer, negativeMessageAnswer);
    }
}
