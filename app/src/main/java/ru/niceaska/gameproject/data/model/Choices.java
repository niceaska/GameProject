package ru.niceaska.gameproject.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class Choices extends ListItem {


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

    public void setPostiveChoiceLabel(String postiveChoiceLabel) {
        this.postiveChoiceLabel = postiveChoiceLabel;
    }

    public String getNegativeChoiceLabel() {
        return negativeChoiceLabel;
    }

    public void setNegativeChoiceLabel(String negativeChoiceLabel) {
        this.negativeChoiceLabel = negativeChoiceLabel;
    }

    public String getPositiveChoice() {
        return positiveChoice;
    }

    public void setPositiveChoice(String positiveChoice) {
        this.positiveChoice = positiveChoice;
    }

    public String getNegativeChoice() {
        return negativeChoice;
    }

    public void setNegativeChoice(String negativeChoice) {
        this.negativeChoice = negativeChoice;
    }

    public int getPositiveMessageAnswer() {
        return positiveMessageAnswer;
    }

    public void setPositiveMessageAnswer(int positiveMessageAnswer) {
        this.positiveMessageAnswer = positiveMessageAnswer;
    }

    public int getNegativeMessageAnswer() {
        return negativeMessageAnswer;
    }

    public void setNegativeMessageAnswer(int negativeMessageAnswer) {
        this.negativeMessageAnswer = negativeMessageAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Choices choices = (Choices) o;

        if (positiveMessageAnswer != choices.positiveMessageAnswer) return false;
        if (negativeMessageAnswer != choices.negativeMessageAnswer) return false;
        if (postiveChoiceLabel != null ? !postiveChoiceLabel.equals(choices.postiveChoiceLabel) : choices.postiveChoiceLabel != null)
            return false;
        if (negativeChoiceLabel != null ? !negativeChoiceLabel.equals(choices.negativeChoiceLabel) : choices.negativeChoiceLabel != null)
            return false;
        if (positiveChoice != null ? !positiveChoice.equals(choices.positiveChoice) : choices.positiveChoice != null)
            return false;
        return negativeChoice != null ? negativeChoice.equals(choices.negativeChoice) : choices.negativeChoice == null;
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
