package ru.niceaska.gameproject.domain.model;

import ru.niceaska.gameproject.data.model.ListItem;

public class MessageChoices implements ListItem {
    private String postiveChoiceLabel;
    private String negativeChoiceLabel;
    private String positiveChoice;
    private String negativeChoice;
    private int positiveMessageAnswer;
    private int negativeMessageAnswer;

    public MessageChoices(String postiveChoiceLabel, String negativeChoiceLabel,
                          String positiveChoice, String negativeChoice,
                          int positiveMessageAnswer, int negativeMessageAnswer) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageChoices that = (MessageChoices) o;

        if (positiveMessageAnswer != that.positiveMessageAnswer) return false;
        if (negativeMessageAnswer != that.negativeMessageAnswer) return false;
        if (postiveChoiceLabel != null ? !postiveChoiceLabel.equals(that.postiveChoiceLabel) : that.postiveChoiceLabel != null)
            return false;
        if (negativeChoiceLabel != null ? !negativeChoiceLabel.equals(that.negativeChoiceLabel) : that.negativeChoiceLabel != null)
            return false;
        if (positiveChoice != null ? !positiveChoice.equals(that.positiveChoice) : that.positiveChoice != null)
            return false;
        return negativeChoice != null ? negativeChoice.equals(that.negativeChoice) : that.negativeChoice == null;
    }

    @Override
    public int hashCode() {
        int result = postiveChoiceLabel != null ? postiveChoiceLabel.hashCode() : 0;
        result = 31 * result + (negativeChoiceLabel != null ? negativeChoiceLabel.hashCode() : 0);
        result = 31 * result + (positiveChoice != null ? positiveChoice.hashCode() : 0);
        result = 31 * result + (negativeChoice != null ? negativeChoice.hashCode() : 0);
        result = 31 * result + positiveMessageAnswer;
        result = 31 * result + negativeMessageAnswer;
        return result;
    }
}
