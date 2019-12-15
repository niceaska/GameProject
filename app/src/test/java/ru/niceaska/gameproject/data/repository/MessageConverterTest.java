package ru.niceaska.gameproject.data.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.GameMessage;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Мохнаткина Алина
 */
public class MessageConverterTest {

    private MessageConverter messageConverter;

    @Before
    public void setUp() throws Exception {
        messageConverter = new MessageConverter();
    }

    @Test
    public void convertFromHistory_withChoices() {
        List<MessageItem> expected = getMessageItemsExpected(true);
        List<MessageItem> actual = messageConverter.convertFromHistory(createHistoryMessage(true));
        assertThat(actual.get(0), is(expected.get(0)));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void convertFromHistory_noChoices() {
        List<MessageItem> expected = getMessageItemsExpected(false);
        List<MessageItem> actual = messageConverter.convertFromHistory(createHistoryMessage(false));
        assertThat(actual.get(0), is(expected.get(0)));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void convertFromGameMessage_withChoices() {
        GameMessage gameMessage = getGameMessage(true);
        assertThat(messageConverter.convertFromGameMessage(gameMessage), is(getExpectedMessage(true)));
    }

    @Test
    public void convertFromGameMessage_ChoicesNull() {
        GameMessage gameMessage = getGameMessage(false);
        assertThat(messageConverter.convertFromGameMessage(gameMessage), is(getExpectedMessage(false)));
    }

    private GameMessage getGameMessage(boolean isChoices) {
        return new GameMessage(1, "test", false, 6,
                isChoices ? new Choices(
                        "pos label", "neg label",
                        "pos choice", "neg choice",
                        1, 2
                ) : null
        );
    }

    private MessageItem getExpectedMessage(boolean isChoices) {
        return new MessageItem(1, "test", false, 6,
                isChoices ? new MessageChoices(
                        "pos label", "neg label",
                        "pos choice", "neg choice",
                        1, 2
                ) : null
        );
    }

    private List<MessageItem> getMessageItemsExpected(boolean isChoices) {
        List<MessageItem> messageItems = new ArrayList<>();
        messageItems.add(getExpectedMessage(isChoices));
        return messageItems;
    }

    private List<HistoryMessage> createHistoryMessage(boolean isChoices) {
        Choices choices = isChoices ? new Choices(
                "pos label", "neg label",
                "pos choice", "neg choice",
                1, 2
        ) : null;
        List<HistoryMessage> historyMessages = new ArrayList<>();
        historyMessages.add(new HistoryMessage(
                "1", "user", "test", false, 6, choices
        ));

        return historyMessages;
    }
}