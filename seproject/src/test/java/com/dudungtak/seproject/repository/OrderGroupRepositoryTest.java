package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.Ingredient;
import com.dudungtak.seproject.entity.OrderGroup;
import com.dudungtak.seproject.entity.Style;
import com.dudungtak.seproject.entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderGroupRepositoryTest {
    @Autowired
    OrderGroupRepository orderGroupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StyleRepository styleRepository;

    @Test
    @Transactional
    public void create() {
        LocalDateTime orderAt = LocalDateTime.now();
        String status = "REGISTERED";
        String revAddress = "서울시 동대문구 휘경동";
        String paymentType = "CARD";
        BigDecimal totalPrice = BigDecimal.valueOf(15000);
        BigDecimal totalCost = BigDecimal.valueOf(12000);
        String comment = "테스트 주문";
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";
        User user = userRepository.getOne(1L);
        Style style = styleRepository.getOne(1L);

        OrderGroup orderGroup = OrderGroup.builder()
                .orderAt(orderAt)
                .status(status)
                .revAddress(revAddress)
                .paymentType(paymentType)
                .totalPrice(totalPrice)
                .totalCost(totalCost)
                .comment(comment)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .user(user)
                .style(style)
                .build();

        OrderGroup newOrderGroup = orderGroupRepository.save(orderGroup);

        Assert.assertNotNull(newOrderGroup);
        Assert.assertEquals(newOrderGroup.getComment(), comment);
    }

    @Test
    public void read() {
        Long id = 1L;

        Optional<OrderGroup> optional = orderGroupRepository.findById(id);

        Assert.assertNotNull(optional.isPresent());
        optional.ifPresent(orderGroup -> Assert.assertEquals(orderGroup.getId(), id));
    }

    @Test
    @Transactional
    public void update() {
        Long id = 1L;
        String newComment = "수정된 테스트 주문";

        Optional<OrderGroup> optional = orderGroupRepository.findById((id));

        Assert.assertTrue(optional.isPresent());

        optional.ifPresent(orderGroup -> {
            orderGroup.setComment(newComment);

            OrderGroup updatedOrderGroup = orderGroupRepository.save(orderGroup);

            Assert.assertNotNull(updatedOrderGroup);
            Assert.assertEquals(updatedOrderGroup.getComment(), newComment);
        });
    }

    @Test
    @Transactional
    public void delete() {
        Optional<OrderGroup> optional = orderGroupRepository.findById((1L));

        Assert.assertTrue(optional.isPresent());

        optional.ifPresent(ingredient -> {
            orderGroupRepository.delete(ingredient);
        });

        Optional<OrderGroup> deletedOrderGroup = orderGroupRepository.findById(1L);

        Assert.assertFalse(deletedOrderGroup.isPresent());
    }
}