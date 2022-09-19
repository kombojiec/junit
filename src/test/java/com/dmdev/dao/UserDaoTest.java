package com.dmdev.dao;

import com.dmdev.UserUtil;
import com.dmdev.entity.User;
import com.dmdev.mapper.CreateUserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @Mock
    UserDao userDao;
    @Spy
    CreateUserMapper createUserMapper;

    @Test
    void findUserById() {
        doReturn(Optional.of(UserUtil.IVAN)).when(userDao).findById(1);
        Optional<User> user = userDao.findById(1);
        assertAll(
                () -> assertThat(user.isPresent()).isTrue(),
                () -> assertThat(user.get().getId()).isEqualTo(UserUtil.IVAN.getId())
        );
        assertThat(userDao.findById(1)).isPresent();
    }

    @Test
    void findUserByIncorrectId() {
        doReturn(Optional.empty()).when(userDao).findById(0);
        Optional<User> user = userDao.findById(0);
        assertAll(
                () -> assertThat(user.isEmpty()).isTrue(),
                () -> assertThrows(NoSuchElementException.class, () -> user.get())
        );
    }

    @Test
    void findAll() {
        doReturn(List.of(UserUtil.IVAN, UserUtil.VASYA)).when(userDao).findAll();
        var users = userDao.findAll();
        assertAll(
                () -> assertThat(!users.isEmpty()).isTrue(),
                () -> assertThat(users.size()).isEqualTo(2)
        );
    }

    @Test
    void save() {
        User user = createUserMapper.map(UserUtil.NEW_USER_DTO);
        doReturn(UserUtil.VASYA).when(userDao).save(user);
        User vasya = userDao.save(user);
        assertThat(vasya.getId()).isEqualTo(6);
    }

    @Test
    void findByEmailAndPassword() {
        doReturn(Optional.of(UserUtil.VASYA)).when(userDao)
                .findByEmailAndPassword(UserUtil.VASYA.getEmail(), UserUtil.VASYA.getPassword());
        Optional<User> user = userDao.findByEmailAndPassword(UserUtil.VASYA.getEmail(), UserUtil.VASYA.getPassword());

        assertAll(
                () -> assertThat(user.isPresent()).isTrue(),
                () -> assertThat(user.get().getId()).isEqualTo(UserUtil.VASYA.getId())
        );
    }

    @Test
    void findByEmailAndPasswordWithIncorrectData() {
        doReturn(Optional.empty()).when(
                userDao).findByEmailAndPassword("random", UserUtil.VASYA.getPassword());
        doReturn(Optional.empty()).when(
                userDao).findByEmailAndPassword(UserUtil.VASYA.getEmail(), "password");
        var failLoginUser = userDao.findByEmailAndPassword("random", UserUtil.VASYA.getPassword());
        var failPasswordUser = userDao.findByEmailAndPassword(UserUtil.VASYA.getEmail(), "password");
        assertAll(
               () -> assertThat(failLoginUser.isEmpty()).isTrue(),
               () -> assertThat(failPasswordUser.isEmpty()).isTrue()
        );
    }

    @Test
    void deleteUser() {
        doReturn(true).when(userDao).delete(1);
        doReturn(false).when(userDao).delete(0);

        assertAll(
                () -> assertThat(userDao.delete(1)).isEqualTo(true),
                () -> assertThat(userDao.delete(0)).isEqualTo(false)
        );
    }

    @Test
    void update() {
        doNothing().when(userDao).update(UserUtil.VASYA);
        doReturn(Optional.of(UserUtil.VASYA)).when(userDao).findById(UserUtil.VASYA.getId());
        userDao.update(UserUtil.VASYA);
        assertThat(UserUtil.VASYA).isEqualTo(userDao.findById(UserUtil.VASYA.getId()).get());
    }

}













