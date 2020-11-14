package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.Staff;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class StaffRepositoryTest {
    @Autowired
    StaffRepository staffRepository;

    @Test
    @Transactional
    public void create() {
        String name = "테스트 직원";
        String email = "testStaff01@gmail.com";
        String password = "1234";
        String gender = "남성";
        String address = "서울시 동대문구";
        String phoneNumber = "010-0000-0000";
        String job = "요리사";
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        Staff staff = Staff.builder()
                .name(name)
                .email(email)
                .password(password)
                .gender(gender)
                .address(address)
                .phoneNumber(phoneNumber)
                .job(job)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .build();

        Staff newStaff = staffRepository.save(staff);

        Assert.assertNotNull(newStaff);
        Assert.assertEquals(newStaff.getName(), name);
    }

    @Test
    public void read() {
        Long id = 1L;

        Optional<Staff> optionalStaff = staffRepository.findById(id);

        Assert.assertTrue(optionalStaff.isPresent());
        optionalStaff.ifPresent(staff -> Assert.assertEquals(staff.getId(), id));
    }

    @Test
    @Transactional
    public void update() {
        Long id = 1L;
        String newName = "수정된 테스트 직원";

        Optional<Staff> optionalStaff = staffRepository.findById(id);

        Assert.assertNotNull(optionalStaff.isPresent());

        optionalStaff.ifPresent(style -> {
            style.setName(newName);

            Staff updatedStaff = staffRepository.save(style);

            Assert.assertNotNull(updatedStaff);
            Assert.assertEquals(updatedStaff.getName(), newName);
        });
    }

    @Test
    @Transactional
    public void delete() {
        Long id = 1L;

        Optional<Staff> optionalStaff = staffRepository.findById(id);

        Assert.assertNotNull(optionalStaff.isPresent());

        optionalStaff.ifPresent(style -> {
            staffRepository.delete(style);

            Optional<Staff> deletedStaff = staffRepository.findById(style.getId());

            Assert.assertFalse(deletedStaff.isPresent());
        });
    }
}