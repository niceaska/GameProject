package ru.niceaska.gameproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Choices extends ListItem implements Parcelable {

    private String postiveChoiceLabel;
    private String negativeChoiceLabel;
    private String positiveChoice;
    private String negativeChoice;
    private Message positiveMessage;
    private Message negativeMessage;

    public Choices(String postiveChoiceLabel, String positiveChoice,
                   String negativeChoice, String negativeChoiceLabel,
                   Message positiveMessage, Message negativeMessage) {
        this.postiveChoiceLabel = postiveChoiceLabel;
        this.negativeChoiceLabel = negativeChoiceLabel;
        this.positiveMessage = positiveMessage;
        this.negativeMessage = negativeMessage;
        this.positiveChoice = positiveChoice;
        this.negativeChoice = negativeChoice;
    }

    protected Choices(Parcel in) {
        postiveChoiceLabel = in.readString();
        negativeChoiceLabel = in.readString();
        positiveChoice = in.readString();
        negativeChoice = in.readString();
        positiveMessage = in.readParcelable(Message.class.getClassLoader());
        negativeMessage = in.readParcelable(Message.class.getClassLoader());
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

    public Message getPositiveMessage() {
        return positiveMessage;
    }

    public Message getNegativeMessage() {
        return negativeMessage;
    }

    public String getPositiveChoice() {
        return positiveChoice;
    }

    public String getNegativeChoice() {
        return negativeChoice;
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
        dest.writeParcelable(positiveMessage, flags);
        dest.writeParcelable(negativeMessage, flags);
    }
}
