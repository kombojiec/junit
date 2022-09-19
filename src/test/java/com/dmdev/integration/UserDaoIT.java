package com.dmdev.integration;

import com.dmdev.dao.UserDao;
import com.dmdev.entity.User;
import com.dmdev.mapper.CreateUserMapper;
import org.junit.jupiter.api.Test;

import static com.dmdev.UserUtil.IVAN;
import static com.dmdev.UserUtil.NEW_USER_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserDaoIT extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();
    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();

    @Test
    void findAll() {
        assertThat(userDao.findAll().size()).isEqualTo(5);
    }

    @Test
    void findById() {
        var existedUser = userDao.findById(1);
        var missingUser = userDao.findById(0);
        assertAll(
                () -> assertThat(existedUser.isPresent()).isTrue(),
                () -> assertThat(missingUser.isEmpty()).isTrue()
        );
    }

    @Test
    void save() {
        User newUser = createUserMapper.map(NEW_USER_DTO);
        User createdUser = userDao.save(newUser);
        assertThat(createdUser.getId()).isNotNull();
    }

    @Test
    void findByEmailAndPassword(){
        var user = userDao.findByEmailAndPassword(IVAN.getEmail(), IVAN.getPassword());
        var notExistsLoginUser = userDao.findByEmailAndPassword("random", IVAN.getPassword());
        var notExistsPasswordUser = userDao.findByEmailAndPassword(IVAN.getEmail(), "random");
        assertAll(
                () -> assertThat(user.isPresent()).isTrue(),
                () -> assertThat(notExistsLoginUser.isPresent()).isFalse(),
                () -> assertThat(notExistsPasswordUser.isPresent()).isFalse()
        );
    }

    @Test
    void delete() {
        var isExisted = userDao.delete(1);
        var isMissing = userDao.delete(1);
        assertAll(
                () -> assertThat(isExisted).isTrue(),
                () -> assertThat(isMissing).isFalse()
        );
    }

    @Test
    void update() {
        User newUser = createUserMapper.map(NEW_USER_DTO);
        User createdUser = userDao.save(newUser);
        createdUser.setName("UpdatedName");
        userDao.update(createdUser);
        var updatedUser = userDao.findById(createdUser.getId());
        assertThat(updatedUser.get().getName()).isEqualTo("UpdatedName");
    }

}
