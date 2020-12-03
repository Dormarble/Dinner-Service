package com.dudungtak.seproject.domain.repository;

import com.dudungtak.seproject.model.entity.OrderElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderElementRepository extends JpaRepository<OrderElement, Long> {
}
