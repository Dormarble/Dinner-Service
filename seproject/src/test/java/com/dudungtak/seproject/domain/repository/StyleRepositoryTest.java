package com.dudungtak.seproject.domain.repository;

import com.dudungtak.seproject.model.entity.Style;
import com.dudungtak.seproject.model.enumpackage.StyleStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class StyleRepositoryTest {
    @Autowired
    private StyleRepository styleRepository;
//
//    @Test
//    @Transactional
//    public void create() {
//        String name = "테스트 스타일";
//        BigDecimal price = new BigDecimal(100000);
//        StyleStatus status = StyleStatus.REGISTERED;
//        String content = "테스트 스타일 내용";
//        LocalDateTime createdAt = LocalDateTime.now();
//        String createdBy = "AdminServer";
//
//        Style style = Style.builder()
//                .name(name)
//                .price(price)
//                .status(status)
//                .content(content)
//                .createdAt(createdAt)
//                .createdBy(createdBy)
//                .build();
//
//        Style newStyle = styleRepository.save(style);
//
//        Assert.assertNotNull(newStyle);
//        Assert.assertEquals(newStyle.getName(), name);
//    }
//
//    @Test
//    public void read() {
//        Long id = 1L;
//
//        Optional<Style> optionalStyle = styleRepository.findById(id);
//
//        Assert.assertTrue(optionalStyle.isPresent());
//        optionalStyle.ifPresent(style -> Assert.assertEquals(style.getId(), id));
//    }
//
//    @Test
//    @Transactional
//    public void updated() {
//        Long id = 1L;
//        String newName = "수정된 테스트 스타일";
//
//        Optional<Style> optionalStyle = styleRepository.findById(id);
//
//        Assert.assertNotNull(optionalStyle.isPresent());
//
//        optionalStyle.ifPresent(style -> {
//            style.setName(newName);
//
//            Style updatedStyle = styleRepository.save(style);
//
//            Assert.assertNotNull(updatedStyle);
//            Assert.assertEquals(updatedStyle.getName(), newName);
//        });
//    }
//
//    @Test
//    @Transactional
//    public void delete() {
//        Long id = 1L;
//
//        Optional<Style> optionalStyle = styleRepository.findById(id);
//
//        Assert.assertNotNull(optionalStyle.isPresent());
//
//        optionalStyle.ifPresent(style -> {
//            styleRepository.delete(style);
//
//            Optional<Style> deletedStyle = styleRepository.findById(style.getId());
//
//            Assert.assertFalse(deletedStyle.isPresent());
//        });
//    }
}