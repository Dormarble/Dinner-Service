package com.dudungtak.seproject.domain.repository;

import com.dudungtak.seproject.model.entity.OrderGroup;
import com.dudungtak.seproject.model.entity.User;
import com.dudungtak.seproject.model.enumpackage.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderGroupRepository extends JpaRepository<OrderGroup, Long> {
    Page<OrderGroup> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<OrderGroup> findByStatusOrderByCreatedAtAsc(OrderStatus status);
}
