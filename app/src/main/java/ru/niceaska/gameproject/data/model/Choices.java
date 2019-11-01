package ru.niceaska.gameproject.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity
public class Choices extends ListItem implements Parcelable {


    @ColumnInfo(name = "postive_label")
    private String postiveChoiceLabel;
    @ColumnInfo(name = "negative_label")
    private String negativeChoiceLabel;
    @ColumnInfo(name = "postive_choice")
    private String positiveChoice;
    @ColumnInfo(name = "negative_choice")
    private String negativeChoice;
    @ColumnInfo(name = "positive_answer")
    private String positiveMessageAnswer;
    @ColumnInfo(name = "negative_answer")
    private String negativeMessageAnswer;

    public Choices(String postiveChoiceLabel, String negativeChoiceLabel, String positiveChoice, String negativeChoice, String positiveMessageAnswer, String negativeMessageAnswer) {
        this.postiveChoiceLabel = postiveChoiceLabel;
        this.negativeChoiceLabel = negativeChoiceLabel;
        this.positiveChoice = positiveChoice;
        this.negativeChoice = negativeChoice;
        this.positiveMessageAnswer = positiveMessageAnswer;
        this.negativeMessageAnswer = negativeMessageAnswer;
    }

    @Ignore
    protected Choices(Parcel in) {
        postiveChoiceLabel = in.readString();
        negativeChoiceLabel = in.readString();
        positiveChoice = in.readString();
        negativeChoice = in.readString();
        positiveMessageAnswer = in.readString();
        negativeMessageAnswer = in.readString();
    }


    public static final Creator<Choices> CREATOR = new Creator<Choices>() {
        @Override
        public Choices createFromParcel(Parcel in) {
            return new Choices(in);
        }

        @Override
        public Choices[] newArray(int size) {
            return new Choices[size];
        }
    };

    public String getPostiveChoiceLabel() {
        return postiveChoiceLabel;
    }

    public String getNegativeChoiceLabel() {
        return negativeChoiceLabel;
    }

    public String getPositiveMessageAnswer() {
        return positiveMessageAnswer;
    }

    public String getNegativeMessageAnswer() {
        return negativeMessageAnswer;
    }

    public String getPositiveChoice() {
        return positiveChoice;
    }

    public String getNegativeChoice() {
        return negativeChoice;
    }


    public void setPostiveChoiceLabel(String postiveChoiceLabel) {
        this.postiveChoiceLabel = postiveChoiceLabel;
    }

    public void setNegativeChoiceLabel(String negativeChoiceLabel) {
        this.negativeChoiceLabel = negativeChoiceLabel;
    }

    public void setPositiveChoice(String positiveChoice) {
        this.positiveChoice = positiveChoice;
    }

    public void setNegativeChoice(String negativeChoice) {
        this.negativeChoice = negativeChoice;
    }

    public void setPositiveMessageAnswer(String positiveMessageAnswer) {
        this.positiveMessageAnswer = positiveMessageAnswer;
    }

    public void setNegativeMessageAnswer(String negativeMessageAnswer) {
        this.negativeMessageAnswer = negativeMessageAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postiveChoiceLabel);
        dest.writeString(negativeChoiceLabel);
        dest.writeString(positiveChoice);
        dest.writeString(negativeChoice);
        dest.writeString(positiveMessageAnswer);
        dest.writeString(negativeMessageAnswer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Choices choices = (Choices) o;

        if (postiveChoiceLabel != null ? !postiveChoiceLabel.equals(choices.postiveChoiceLabel) : choices.postiveChoiceLabel != null)
            return false;
        if (negativeChoiceLabel != null ? !negativeChoiceLabel.equals(choices.negativeChoiceLabel) : choices.negativeChoiceLabel != null)
            return false;
        if (positiveChoice != null ? !positiveChoice.equals(choices.positiveChoice) : choices.positiveChoice != null)
            return false;
        if (negativeChoice != null ? !negativeChoice.equals(choices.negativeChoice) : choices.negativeChoice != null)
            return false;
        if (positiveMessageAnswer != null ? !positiveMessageAnswer.equals(choices.positiveMessageAnswer) : choices.positiveMessageAnswer != null)
            return false;
        return negativeMessageAnswer != null ? negativeMessageAnswer.equals(choices.negativeMessageAnswer) : choices.negativeMessageAnswer == null;
    }

    @Override
    public int hashCode() {
        int result = postiveChoiceLabel != null ? postiveChoiceLabel.hashCode() : 0;
        result = 31 * result + (negativeChoiceLabel != null ? negativeChoiceLabel.hashCode() : 0);
        result = 31 * result + (positiveChoice != null ? positiveChoice.hashCode() : 0);
        result = 31 * result + (negativeChoice != null ? negativeChoice.hashCode() : 0);
        result = 31 * result + (positiveMessageAnswer != null ? positiveMessageAnswer.hashCode() : 0);
        result = 31 * result + (negativeMessageAnswer != null ? negativeMessageAnswer.hashCode() : 0);
        return result;
    }
}
