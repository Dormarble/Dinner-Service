package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.Ingredient;
import com.dudungtak.seproject.enumpackage.IngredientStatus;
import com.dudungtak.seproject.enumpackage.IngredientType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IngredientRepositoryTest {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    @Transactional
    public void create() {
        String name = "테스트 재료";
        IngredientType type = IngredientType.VEGETABLE;
        IngredientStatus status = IngredientStatus.REGISTERED;
        BigDecimal cost = new BigDecimal(1000);
        Integer stock = 10;
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";


        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .type(type)
                .status(status)
                .cost(cost)
                .stock(stock)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .build();

        Ingredient newIngredient = ingredientRepository.save(ingredient);

        Assert.assertNotNull(newIngredient);
        Assert.assertEquals(newIngredient.getName(), name);
    }

    @Test
    public void read() {
        String name = "테스트 재료";

        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(1L);

        optionalIngredient.ifPresent(ingredient -> {
            Assert.assertEquals(ingredient.getName(), name);
        });
    }

    @Test
    @Transactional
    public void update() {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById((1L));

        Assert.assertTrue(optionalIngredient.isPresent());

        optionalIngredient.ifPresent(ingredient -> {
            String newName = "수정된 테스트 재료";
            ingredient.setName(newName);

            Ingredient newIngredient = ingredientRepository.save(ingredient);

            Assert.assertNotNull(newIngredient);
            Assert.assertEquals(newIngredient.getName(), newName);
        });
    }

    @Test
    @Transactional
    public void delete() {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById((1L));

        Assert.assertTrue(optionalIngredient.isPresent());

        optionalIngredient.ifPresent(ingredient -> {
            ingredientRepository.delete(ingredient);
        });

        Optional<Ingredient> deletedIngredient = ingredientRepository.findById(1L);

        Assert.assertFalse(deletedIngredient.isPresent());
    }
}