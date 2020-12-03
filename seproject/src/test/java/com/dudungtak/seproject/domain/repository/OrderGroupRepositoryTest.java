package com.dudungtak.seproject.domain.repository;

import com.dudungtak.seproject.model.entity.OrderGroup;
import com.dudungtak.seproject.model.entity.Style;
import com.dudungtak.seproject.model.entity.User;
import com.dudungtak.seproject.model.enumpackage.OrderPaymentType;
import com.dudungtak.seproject.model.enumpackage.OrderStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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
        OrderStatus status = OrderStatus.RECEIVED;
        String revAddress = "서울시 동대문구 휘경동";
        OrderPaymentType paymentType = OrderPaymentType.CARD;
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