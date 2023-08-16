package com.gmail.merikbest2015.service;

import com.gmail.merikbest2015.TweetServiceTestHelper;
import com.gmail.merikbest2015.dto.request.IdsRequest;
import com.gmail.merikbest2015.dto.response.chat.ChatTweetUserResponse;
import com.gmail.merikbest2015.repository.TweetRepository;
import com.gmail.merikbest2015.repository.projection.ChatTweetProjection;
import com.gmail.merikbest2015.repository.projection.NotificationTweetProjection;
import com.gmail.merikbest2015.repository.projection.TweetProjection;
import com.gmail.merikbest2015.service.impl.TweetClientServiceImpl;
import com.gmail.merikbest2015.util.TestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TweetClientServiceImplTest {

    @Autowired
    private TweetClientServiceImpl tweetClientService;

    @MockBean
    private TweetRepository tweetRepository;

    private static final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    private static final PageRequest pageable = PageRequest.of(0, 20);
    private static final List<Long> ids = List.of(1L, 2L, 3L);
    private static final IdsRequest idsRequest = new IdsRequest(ids);
    private static final List<TweetProjection> tweetProjections = Arrays.asList(
            TweetServiceTestHelper.createTweetProjection(TweetProjection.class),
            TweetServiceTestHelper.createTweetProjection(TweetProjection.class));
    private static final Page<TweetProjection> pageableTweetProjections = new PageImpl<>(tweetProjections, pageable, 20);

    @Test
    public void getTweetsByIds() {
        when(tweetRepository.getTweetListsByIds(ids)).thenReturn(tweetProjections);
        assertEquals(tweetProjections, tweetClientService.getTweetsByIds(idsRequest));
        verify(tweetRepository, times(1)).getTweetListsByIds(ids);
    }

    @Test
    public void getTweetsByUserIds() {
        when(tweetRepository.getTweetsByAuthorIds(ids, pageable)).thenReturn(pageableTweetProjections);
        assertEquals(pageableTweetProjections, tweetClientService.getTweetsByUserIds(idsRequest, pageable));
        verify(tweetRepository, times(1)).getTweetsByAuthorIds(ids, pageable);
    }

    @Test
    public void getTweetById() {
        TweetProjection tweetProjection = TweetServiceTestHelper.createTweetProjection(TweetProjection.class);
        when(tweetRepository.getTweetById(TestConstants.TWEET_ID, TweetProjection.class)).thenReturn(Optional.of(tweetProjection));
        assertEquals(tweetProjection, tweetClientService.getTweetById(TestConstants.TWEET_ID));
        verify(tweetRepository, times(1)).getTweetById(TestConstants.TWEET_ID, TweetProjection.class);
    }

    @Test
    public void getTweetsByIds_Pageable() {
        when(tweetRepository.getTweetsByIds(ids, pageable)).thenReturn(pageableTweetProjections);
        assertEquals(pageableTweetProjections, tweetClientService.getTweetsByIds(idsRequest, pageable));
        verify(tweetRepository, times(1)).getTweetsByIds(ids, pageable);
    }
}
