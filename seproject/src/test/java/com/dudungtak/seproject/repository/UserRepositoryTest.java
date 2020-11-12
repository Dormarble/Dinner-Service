package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {
        String name = "홍길동";
        String password = "1234";
        String email = "gildong@gmail.com";
        String phoneNumber = "010-1111-1111";
        String address = "서울시 강남구";
        String gender = "남성";
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        User user = User.builder()
                .name(name)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .gender(gender)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .build();

        User newUser = userRepository.save(user);

        Assert.assertNotNull(newUser);
        Assert.assertEquals(user.getName(), name);
        Assert.assertEquals(user.getAddress(), address);
    }

    @Test
    public void read() {
        String name = "홍길동";
        Optional<User> optionalUser = userRepository.findById(1L);

        optionalUser.ifPresent(user -> {
            Assert.assertEquals(user.getName(), name);
        });
    }

    @Test
    @Transactional
    public void update() {
        String newName = "전우치";
        Optional<User> optionalUser = userRepository.findById(1L);

        Assert.assertTrue(optionalUser.isPresent());

        optionalUser.ifPresent(user -> {
            user.setName(newName);

            User newUser = userRepository.save(user);

            Assert.assertNotNull(newUser);
            Assert.assertEquals(newUser.getName(), newName);
        });
    }

    @Test
    @Transactional
    public void delete() {
        Optional<User> optionalUser = userRepository.findById(1L);

        Assert.assertTrue(optionalUser.isPresent());

        optionalUser.ifPresent(user -> {
            userRepository.delete(user);
        });

        Optional<User> deletedUser = userRepository.findById(1L);

        Assert.assertFalse(deletedUser.isPresent());
    }
}