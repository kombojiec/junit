package com.dmdev.service;

import com.dmdev.UserUtil;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    UserMapper userMapper;
    @Mock
    private UserService userService;

    @Test
    void loginSuccessIfUserExists() {
        UserDto user = userMapper.map(UserUtil.IVAN);
        doReturn(Optional.of(user)).when(userService).login(UserUtil.IVAN.getEmail(), UserUtil.IVAN.getPassword());
        Optional<UserDto> optionalUserDto = userService.login(UserUtil.IVAN.getEmail(), UserUtil.IVAN.getPassword());
        assertThat(optionalUserDto.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void loginFailIfLoginOrPasswordIsWrong() {
        doReturn(Optional.empty()).when(userService).login("random", UserUtil.IVAN.getPassword());
        doReturn(Optional.empty()).when(userService).login(UserUtil.IVAN.getEmail(), "qwerty");
        var wrongLoginUser = userService.login("random", UserUtil.IVAN.getPassword());
        Optional<UserDto> wrongPasswordUser = userService.login(UserUtil.IVAN.getEmail(), "qwerty");
        assertAll(
                () -> assertThat(wrongLoginUser).isEmpty(),
                () -> assertThat(wrongPasswordUser).isEmpty()
        );
    }

    @Test
    void createSuccessIfDataIsCorrect() {
        doReturn(userMapper.map(UserUtil.VASYA)).when(userService).create(UserUtil.NEW_USER_DTO);
        var user = userService.create(UserUtil.NEW_USER_DTO);
        assertThat(user.getId()).isEqualTo(6);

    }

    @Test
    void createFailIfDataIsInvalid() {
        var incorrectCreateUserDto = CreateUserDto.builder()
                .name("Incorrect")
                .birthday("1999:11:25")
                .email("r@mbler.ru")
                .password("qwerty")
                .role("MODERATOR")
                .gender("HERMAPHRODITE")
                .build();
        doThrow(ValidationException.class).when(userService).create(incorrectCreateUserDto);
        assertThrows(ValidationException.class,
                () -> userService.create(incorrectCreateUserDto));
    }

}
