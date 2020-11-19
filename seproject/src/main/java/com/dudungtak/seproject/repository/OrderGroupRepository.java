package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.OrderGroup;
import com.dudungtak.seproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGroupRepository extends JpaRepository<OrderGroup, Long> {
    public Page<OrderGroup> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
