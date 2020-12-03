package com.dudungtak.seproject.domain.repository;

import com.dudungtak.seproject.model.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
}
