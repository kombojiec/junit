package com.dmdev;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;

import java.time.LocalDate;

public class UserUtil {

    public static final User IVAN = User.builder()
            .id(1)
            .name("Ivan")
            .birthday(LocalDate.of(1990, 01, 10))
            .email("ivan@gmail.com")
            .password("111")
            .role(Role.ADMIN)
            .gender(Gender.MALE)
            .build();

    public static final CreateUserDto NEW_USER_DTO = CreateUserDto.builder()
            .name("Vasya")
            .birthday("1988-01-20")
            .email("m@il.ru")
            .password("qwerty")
            .role("USER")
            .gender("MALE")
            .build();

    public static final User VASYA = User.builder()
            .id(6)
            .name("Vasya")
            .birthday(LocalDate.of(1988,01, 20))
            .email("m@il.ru")
            .password("qwerty")
            .role(Role.USER)
            .gender(Gender.MALE)
            .build();

}
