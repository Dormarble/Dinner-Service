package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.DishElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishElementRepository extends JpaRepository<DishElement, Long> {
}
