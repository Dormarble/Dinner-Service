package com.dudungtak.seproject.repository;

import com.dudungtak.seproject.entity.Dish;
import com.dudungtak.seproject.entity.DishElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DishElementRepository extends JpaRepository<DishElement, Long> {
    @Transactional
    void deleteAllByDish(Dish dish);
}
