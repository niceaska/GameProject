package ru.niceaska.gameproject.domain.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import ru.niceaska.gameproject.data.model.ListItem;

public class MessageChoices implements ListItem {

    private String positiveChoiceLabel;
    private String negativeChoiceLabel;
    private String positiveChoice;
    private String negativeChoice;
    private int positiveMessageAnswer;
    private int negativeMessageAnswer;

    public MessageChoices(String positiveChoiceLabel, String negativeChoiceLabel,
                          String positiveChoice, String negativeChoice,
                          int positiveMessageAnswer, int negativeMessageAnswer) {
        this.positiveChoiceLabel = Preconditions.checkNotNull(positiveChoiceLabel);
        this.negativeChoiceLabel = Preconditions.checkNotNull(negativeChoiceLabel);
        this.positiveChoice = Preconditions.checkNotNull(positiveChoice);
        this.negativeChoice = Preconditions.checkNotNull(negativeChoice);
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
        MessageChoices that = (MessageChoices) o;
        return positiveMessageAnswer == that.positiveMessageAnswer &&
                negativeMessageAnswer == that.negativeMessageAnswer &&
                Objects.equal(positiveChoiceLabel, that.positiveChoiceLabel) &&
                Objects.equal(negativeChoiceLabel, that.negativeChoiceLabel) &&
                Objects.equal(positiveChoice, that.positiveChoice) &&
                Objects.equal(negativeChoice, that.negativeChoice);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(positiveChoiceLabel, negativeChoiceLabel,
                positiveChoice, negativeChoice, positiveMessageAnswer, negativeMessageAnswer);
    }
}
