package com.dudungtak.seproject.domain.repository;

import com.dudungtak.seproject.model.entity.Dish;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class DishRepositoryTest {
    @Autowired
    DishRepository dishRepository;

    @Test
    @Transactional
    public void create() {
        String name = "테스트 요리";
        String status = "UNREGISTERED";
        BigDecimal price = new BigDecimal(10000);
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminUser";

        Dish dish = Dish.builder()
                .name(name)
                .status(status)
                .price(price)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .build();

        Dish newDish = dishRepository.save(dish);

        Assert.assertNotNull(newDish);
        Assert.assertEquals(newDish.getName(), dish.getName());
    }

    @Test
    public void read() {
        Long id = 1L;

        Optional<Dish> optional = dishRepository.findById(id);

        Assert.assertTrue(optional.isPresent());
        optional.ifPresent(dish -> Assert.assertEquals(dish.getId(), id));
    }

    @Test
    @Transactional
    public void update() {
        Long id = 1L;
        String updatedName = "수정된 테스트 요리";

        Optional<Dish> optional = dishRepository.findById(id);

        Assert.assertTrue(optional.isPresent());

        optional.ifPresent(dish -> {
            dish.setName(updatedName);
            Dish updatedDish = dishRepository.save(dish);

            Assert.assertNotNull(updatedDish);
            Assert.assertEquals(updatedDish.getName(), updatedName);
        });
    }

    @Test
    @Transactional
    public void delete() {
        Long id = 1L;

        Optional<Dish> optional = dishRepository.findById(id);

        Assert.assertTrue(optional.isPresent());

        optional.ifPresent(dish -> {
            dishRepository.delete(dish);

            Optional<Dish> deletedOptional = dishRepository.findById(id);

            Assert.assertFalse(deletedOptional.isPresent());
        });
    }
}