package com.gmail.merikbest2015.service;

import com.gmail.merikbest2015.UserServiceTestHelper;
import com.gmail.merikbest2015.exception.ApiRequestException;
import com.gmail.merikbest2015.repository.projection.MutedUserProjection;
import com.gmail.merikbest2015.util.TestConstants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import static com.gmail.merikbest2015.constants.ErrorMessage.USER_ID_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MuteUserServiceImplTest extends AbstractServiceTest {

    @Autowired
    private MuteUserService muteUserService;

    @Test
    public void getMutedList_ShouldReturnMutedUserProjections() {
        Page<MutedUserProjection> mutedUserProjection = UserServiceTestHelper.createMutedUserProjections();
        when(muteUserRepository.getUserMuteListById(TestConstants.USER_ID, pageable)).thenReturn(mutedUserProjection);
        assertEquals(mutedUserProjection, muteUserService.getMutedList(pageable));
        verify(muteUserRepository, times(1)).getUserMuteListById(TestConstants.USER_ID, pageable);
    }

    @Test
    public void processMutedList_ShouldMuteUser() {
        when(userRepository.isUserExist(TestConstants.USER_ID)).thenReturn(true);
        when(muteUserRepository.isUserMuted(TestConstants.USER_ID, 2L)).thenReturn(false);
        assertTrue(muteUserService.processMutedList(TestConstants.USER_ID));
        verify(userRepository, times(1)).isUserExist(TestConstants.USER_ID);
        verify(muteUserRepository, times(1)).isUserMuted(TestConstants.USER_ID, 2L);
        verify(muteUserRepository, times(1)).muteUser(TestConstants.USER_ID, 2L);
    }

    @Test
    public void processMutedList_ShouldUnmuteUser() {
        when(userRepository.isUserExist(TestConstants.USER_ID)).thenReturn(true);
        when(muteUserRepository.isUserMuted(TestConstants.USER_ID, 2L)).thenReturn(true);
        assertFalse(muteUserService.processMutedList(TestConstants.USER_ID));
        verify(userRepository, times(1)).isUserExist(TestConstants.USER_ID);
        verify(muteUserRepository, times(1)).isUserMuted(TestConstants.USER_ID, 2L);
        verify(muteUserRepository, times(1)).unmuteUser(TestConstants.USER_ID, 2L);
    }

    @Test
    public void processMutedList_ShouldUserNotFound() {
        when(userRepository.isUserExist(TestConstants.USER_ID)).thenReturn(false);
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> muteUserService.processMutedList(TestConstants.USER_ID));
        assertEquals(String.format(USER_ID_NOT_FOUND, TestConstants.USER_ID), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(userRepository, times(1)).isUserExist(TestConstants.USER_ID);
    }
}
