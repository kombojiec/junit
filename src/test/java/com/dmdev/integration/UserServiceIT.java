package com.dmdev.integration;

import com.dmdev.UserUtil;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.exception.ValidationException;
import com.dmdev.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceIT extends IntegrationTestBase {

    private final UserService userService = UserService.getInstance();

    @Test
    void loginSuccessIfUserExists() {
        Optional<UserDto> user = userService.login(UserUtil.IVAN.getEmail(), UserUtil.IVAN.getPassword());
        assertThat(user.isPresent()).isTrue();
    }

    @Test
    void loginFailIfLoginOrPasswordIsWrong() {
        Optional<UserDto> wrongLoginUser = userService.login("random", UserUtil.IVAN.getPassword());
        Optional<UserDto> wrongPasswordUser = userService.login(UserUtil.IVAN.getEmail(), "qwerty");
        assertAll(
                () -> assertThat(wrongLoginUser.isEmpty()).isTrue(),
                () -> assertThat(wrongPasswordUser.isEmpty()).isTrue()
        );
    }

    @Test
    void createSuccessIfDataIsCorrect() {
        var createdUser = userService.create(UserUtil.NEW_USER_DTO);
        assertThat(createdUser.getId()).isNotNull();
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
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> userService.create(incorrectCreateUserDto));
        assertAll(
                () -> assertThat(validationException.getErrors().size()).isEqualTo(3),
                () -> assertThat(validationException.getErrors().get(0).getCode()).isEqualTo("invalid.birthday"),
                () -> assertThat(validationException.getErrors().get(1).getCode()).isEqualTo("invalid.gender"),
                () -> assertThat(validationException.getErrors().get(2).getCode()).isEqualTo("invalid.role")
        );
    }
}
