package ru.niceaska.gameproject.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.common.base.Objects;

@Entity
public class Choices {


    @ColumnInfo(name = "postive_label")
    private String postiveChoiceLabel;
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

    public Choices(String postiveChoiceLabel, String negativeChoiceLabel, String positiveChoice,
                   String negativeChoice, int positiveMessageAnswer, int negativeMessageAnswer) {
        this.postiveChoiceLabel = postiveChoiceLabel;
        this.negativeChoiceLabel = negativeChoiceLabel;
        this.positiveChoice = positiveChoice;
        this.negativeChoice = negativeChoice;
        this.positiveMessageAnswer = positiveMessageAnswer;
        this.negativeMessageAnswer = negativeMessageAnswer;
    }


    public String getPostiveChoiceLabel() {
        return postiveChoiceLabel;
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
                Objects.equal(postiveChoiceLabel, choices.postiveChoiceLabel) &&
                Objects.equal(negativeChoiceLabel, choices.negativeChoiceLabel) &&
                Objects.equal(positiveChoice, choices.positiveChoice) &&
                Objects.equal(negativeChoice, choices.negativeChoice);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(postiveChoiceLabel, negativeChoiceLabel, positiveChoice,
                negativeChoice, positiveMessageAnswer, negativeMessageAnswer);
    }
}
