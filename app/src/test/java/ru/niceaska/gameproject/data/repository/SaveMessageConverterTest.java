package ru.niceaska.gameproject.data.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ru.niceaska.gameproject.data.model.Choices;
import ru.niceaska.gameproject.data.model.HistoryMessage;
import ru.niceaska.gameproject.data.model.ListItem;
import ru.niceaska.gameproject.domain.model.MessageChoices;
import ru.niceaska.gameproject.domain.model.MessageItem;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Мохнаткина Алина
 */
public class SaveMessageConverterTest {

    private SaveMessageConverter saveMessageConverter;

    @Before
    public void setUp() throws Exception {
        saveMessageConverter = new SaveMessageConverter();
    }

    @Test
    public void convertToHistoryTest() {
        List<HistoryMessage> expected = createExpectedHistoryMessages();
        List<HistoryMessage> actual = saveMessageConverter.convertToHistory(createMessageItems());
        assertThat(actual, is(expected));
        assertEquals(expected.size(), actual.size());
    }

    private MessageItem getMessageItem() {
        return new MessageItem(
                1, "Simple message", false, 2, new MessageChoices(
                "label 1", "label 2",
                "choice 1", "choice 2",
                4, 5
        ));
    }

    private List<ListItem> createMessageItems() {
        List<ListItem> listItems = new ArrayList<>();
        listItems.add(getMessageItem());
        return listItems;
    }

    private List<HistoryMessage> createExpectedHistoryMessages() {
        List<HistoryMessage> historyMessageList = new ArrayList<>();
        historyMessageList.add(new HistoryMessage(
                "0", "1", "Simple message", false, 2, new Choices(
                "label 1", "label 2",
                "choice 1", "choice 2",
                4, 5
        )));
        return historyMessageList;
    }
}