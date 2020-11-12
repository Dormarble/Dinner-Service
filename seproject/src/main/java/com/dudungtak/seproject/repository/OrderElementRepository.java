package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.OrderElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderElementRepository extends JpaRepository<OrderElement, Long> {
}
