package com.gmail.merikbest2015.service.util;

import com.gmail.merikbest2015.dto.response.chat.ChatTweetResponse;
import com.gmail.merikbest2015.exception.ApiRequestException;
import com.gmail.merikbest2015.service.AbstractServiceTest;
import com.gmail.merikbest2015.util.TestConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static com.gmail.merikbest2015.constants.ErrorMessage.INCORRECT_CHAT_MESSAGE_LENGTH;
import static com.gmail.merikbest2015.constants.ErrorMessage.TWEET_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ChatServiceHelperTest extends AbstractServiceTest {

    @Autowired
    private ChatServiceHelper chatServiceHelper;

    @Test
    public void getChatTweet() {
        when(tweetClient.getChatTweet(TestConstants.TWEET_ID)).thenReturn(new ChatTweetResponse());
        assertEquals(new ChatTweetResponse(), chatServiceHelper.getChatTweet(TestConstants.TWEET_ID));
        verify(tweetClient, times(1)).getChatTweet(TestConstants.TWEET_ID);
    }

    @Test
    public void checkChatMessageLength() {
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> chatServiceHelper.checkChatMessageLength(""));
        assertEquals(INCORRECT_CHAT_MESSAGE_LENGTH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void isTweetExists() {
        when(tweetClient.isTweetExists(TestConstants.TWEET_ID)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> chatServiceHelper.isTweetExists(TestConstants.TWEET_ID));
        assertEquals(TWEET_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
